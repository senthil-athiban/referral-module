package com.indiclinic.referral_system.incentive.rule;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReferralIncentiveRuleRepository extends JpaRepository<ReferralIncentiveRule, UUID> {
    List<ReferralIncentiveRule> findByOwnerProviderId(UUID ownerProviderId);

    List<ReferralIncentiveRule> findByOwnerProviderIdAndActiveTrue(UUID ownerProviderId);
}
