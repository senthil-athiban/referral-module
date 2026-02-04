package com.indiclinic.referral_system.incentive.record;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReferralIncentiveRepository extends JpaRepository<ReferralIncentive, UUID> {
    Optional<ReferralIncentive> findByReferralId(UUID referralId);
    List<ReferralIncentive> findByBeneficiaryProviderId(UUID providerId);
}
