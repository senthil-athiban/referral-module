package com.indiclinic.referral_system.referral.service;

import com.indiclinic.referral_system.common.ApiException;
import com.indiclinic.referral_system.incentive.record.ReferralIncentiveRepository;
import com.indiclinic.referral_system.provider.Provider;
import com.indiclinic.referral_system.provider.ProviderRepository;
import com.indiclinic.referral_system.referral.dto.DoctorNetworkEntry;
import com.indiclinic.referral_system.referral.dto.IncentiveStats;
import com.indiclinic.referral_system.referral.dto.ReferralStats;
import com.indiclinic.referral_system.referral.repository.ReferralContextRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
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
}
