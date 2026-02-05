package com.indiclinic.referral_system.incentive.record;

import com.indiclinic.referral_system.referral.dto.IncentiveStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReferralIncentiveRepository extends JpaRepository<ReferralIncentive, UUID> {
    Optional<ReferralIncentive> findByReferralId(UUID referralId);
    List<ReferralIncentive> findByBeneficiaryProviderId(UUID providerId);
    List<ReferralIncentive> findByBeneficiaryProviderIdAndPayerProviderId(UUID beneficiaryProviderId,
                                                                          UUID counterPartyProviderId);

    @Query("""
        SELECT new com.indiclinic.referral_system.referral.dto.IncentiveStats(
            COALESCE(SUM(i.netAmount), 0),
            COALESCE(SUM(p.amount), 0),
            COALESCE(SUM(i.netAmount), 0) - COALESCE(SUM(p.amount), 0)
        )
        FROM ReferralIncentive i
        LEFT JOIN ReferralIncentivePayment p
               ON p.incentiveId = i.id
        WHERE i.beneficiaryProviderId = :beneficiaryProviderId
          AND i.referralId IN (
              SELECT r.id
              FROM ReferralContext r
              WHERE r.receiverProviderId = :counterPartyProviderId
          )
    """)
    IncentiveStats incentiveStats(
            UUID beneficiaryProviderId,
            UUID counterPartyProviderId
    );
}
