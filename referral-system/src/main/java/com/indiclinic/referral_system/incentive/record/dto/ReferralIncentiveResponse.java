package com.indiclinic.referral_system.incentive.record.dto;

import com.indiclinic.referral_system.incentive.record.ReferralIncentive;
import com.indiclinic.referral_system.incentive.record.SettlementStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ReferralIncentiveResponse(
        UUID id,
        UUID referralId,
        UUID beneficiaryProviderId,
        BigDecimal baseAmount,
        BigDecimal netAmount,
        SettlementStatus settlementStatus,
        Instant createdAt
) {
    public static ReferralIncentiveResponse from(ReferralIncentive i) {
        return new ReferralIncentiveResponse(
                i.getId(),
                i.getReferralId(),
                i.getBeneficiaryProviderId(),
                i.getBaseAmount(),
                i.getNetAmount(),
                i.getSettlementStatus(),
                i.getCreatedAt()
        );
    }
}
