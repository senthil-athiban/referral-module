package com.indiclinic.referral_system.incentive.rule.dto;

import com.indiclinic.referral_system.incentive.rule.ReferralIncentiveRule;
import com.indiclinic.referral_system.referral.domain.ReceiverType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ReferralIncentiveRuleResponse(
        UUID id,
        UUID ownerProviderId,
        ReceiverType referralTargetType,
        String name,
        String description,
        BigDecimal defaultAmount,
        boolean active,
        Instant createdAt
) {
    public static ReferralIncentiveRuleResponse from(ReferralIncentiveRule r) {
        return new ReferralIncentiveRuleResponse(
                r.getId(),
                r.getOwnerProviderId(),
                r.getReferralTargetType(),
                r.getName(),
                r.getDescription(),
                r.getDefaultAmount(),
                r.isActive(),
                r.getCreatedAt()
        );
    }
}
