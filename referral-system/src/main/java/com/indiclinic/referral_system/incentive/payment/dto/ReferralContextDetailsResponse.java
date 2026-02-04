package com.indiclinic.referral_system.incentive.payment.dto;

import com.indiclinic.referral_system.incentive.record.dto.ReferralIncentiveResponse;
import com.indiclinic.referral_system.referral.dto.ReferralContextResponse;

import java.util.List;

public record ReferralContextDetailsResponse(
        ReferralContextResponse referral,
        ReferralIncentive incentive   // nullable
) {
        /* -------- nested -------- */
    public record ReferralIncentive(
            ReferralIncentiveResponse incentive,
            List<ReferralIncentivePaymentResponse> payments
    ) {}
}
