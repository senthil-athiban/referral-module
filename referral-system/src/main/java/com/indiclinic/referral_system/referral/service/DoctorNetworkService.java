package com.indiclinic.referral_system.referral.service;

import com.indiclinic.referral_system.common.ApiException;
import com.indiclinic.referral_system.incentive.record.ReferralIncentive;
import com.indiclinic.referral_system.incentive.record.ReferralIncentiveRepository;
import com.indiclinic.referral_system.provider.Provider;
import com.indiclinic.referral_system.provider.ProviderRepository;
import com.indiclinic.referral_system.referral.domain.ReferralContext;
import com.indiclinic.referral_system.referral.domain.ReferralStatus;
import com.indiclinic.referral_system.referral.dto.DoctorNetworkEntry;
import com.indiclinic.referral_system.referral.dto.IncentiveStats;
import com.indiclinic.referral_system.referral.dto.ReferralStats;
import com.indiclinic.referral_system.referral.repository.ReferralContextRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
public class DoctorNetworkService {

    private final ReferralContextRepository referralRepo;
    private final ReferralIncentiveRepository incentiveRepo;
    private final ProviderRepository providerRepo;

    public DoctorNetworkService(
            ReferralContextRepository referralRepo,
            ReferralIncentiveRepository incentiveRepo,
            ProviderRepository providerRepo
    ) {
        this.referralRepo = referralRepo;
        this.incentiveRepo = incentiveRepo;
        this.providerRepo = providerRepo;
    }

    public List<DoctorNetworkEntry> getDoctorNetwork(UUID providerId) {

        // 1️⃣ Find all connected doctors
        List<UUID> networkIds =
                referralRepo.findNetworkProviderIds(providerId)
                        .stream()
                        .filter(id -> id != null)  // Filter out any null IDs
                        .toList();
        System.out.println(" ========== networkIds ========== " + networkIds);

        // 2️⃣ Build stats per doctor
        return networkIds.stream().map(otherProviderId -> {

            Provider other =
                    providerRepo.findById(otherProviderId)
                            .orElseThrow(() ->
                                    new ApiException("Provider not found: " + otherProviderId));

            ReferralStats outgoing =
                    referralRepo.outgoingStats(providerId, otherProviderId);

            ReferralStats incoming =
                    referralRepo.incomingStats(otherProviderId, providerId);

            IncentiveStats incentive =
                    incentiveRepo.incentiveStats(providerId, otherProviderId);

            return new DoctorNetworkEntry(
                    other.getId(),
                    other.getName(),
                    outgoing,
                    incoming,
                    incentive
            );

        }).toList();
    }

    public List<DoctorNetworkEntry> get(UUID providerId) {
        // 1. get referrals as a sender and receiver.
        List<ReferralContext> referrals = referralRepo.findAllByProvider(providerId);

        // Extract unique network provider IDs
        Set<UUID> networkProviderIds = new HashSet<>();
        for (ReferralContext r: referrals) {
            if (r.getReferrerProviderId().equals(providerId) && r.getReceiverProviderId() != null) {
                networkProviderIds.add(r.getReceiverProviderId());
            } else if (r.getReceiverProviderId() != null && r.getReceiverProviderId().equals(providerId) && r.getReferrerProviderId() != null) {
                networkProviderIds.add(r.getReferrerProviderId());
            }
        }

        // Build network entries
        return networkProviderIds.stream().map(otherProviderId -> {
            Provider otherProvider = providerRepo.findById(otherProviderId).orElseThrow(() -> new ApiException("Provider is not found " + otherProviderId));

            List<ReferralContext> outgoingReferrals = referrals.stream()
                    .filter(r -> r.getReferrerProviderId().equals(providerId) && r.getReceiverProviderId().equals(otherProvider.getId()))
                    .toList();

            List<ReferralContext> incomingReferrals = referrals.stream()
                    .filter(r -> r.getReferrerProviderId().equals(otherProvider.getId()) && r.getReceiverProviderId().equals(providerId))
                    .toList();

            ReferralStats outgoing = calculateReferralStats(outgoingReferrals);
            ReferralStats incoming = calculateReferralStats(incomingReferrals);
            IncentiveStats incentive = incentiveRepo.incentiveStats(providerId, otherProviderId);
//            List<ReferralIncentive> incentives = incentiveRepo.findByBeneficiaryProviderIdAndPayerProviderId(providerId, otherProvider.getId());


            return new DoctorNetworkEntry(
                    otherProvider.getId(),
                    otherProvider.getName(),
                    outgoing,
                    incoming,
                    incentive
            );
        }).toList();
    }

//    public IncentiveStats calculateIncentiveStats(List<ReferralIncentive> incentives) {
//        BigDecimal totalAmount = incentives.stream().map(ReferralIncentive::getNetAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
//        BigDecimal paidAmount = incentives.stream().filter(i -> i.getPa);
//    }

    public ReferralStats calculateReferralStats(List<ReferralContext> referrals) {
        long total = referrals.size();
        long accepted = countByStatus(referrals, ReferralStatus.ACCEPTED);
        long rejected = countByStatus(referrals, ReferralStatus.REJECTED);
        long completed = countByStatus(referrals, ReferralStatus.COMPLETED);
        return new ReferralStats(
                total,
                accepted,
                rejected,
                completed
        );
    }

    public long countByStatus(List<ReferralContext> referrals, ReferralStatus status) {
        return referrals.stream().filter(r -> r.getStatus().equals(status)).count();
    }
}
