package com.indiclinic.referral_system.incentive.payment.dto;

import com.indiclinic.referral_system.incentive.payment.PaymentMode;
import com.indiclinic.referral_system.incentive.payment.ReferralIncentivePayment;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ReferralIncentivePaymentResponse(
        UUID id,
        UUID incentiveId,
        BigDecimal amount,
        PaymentMode paymentMode,
        String referenceId,
        Instant paidAt,
        UUID recordedByProviderId
) {
    public static ReferralIncentivePaymentResponse from(
            ReferralIncentivePayment p
    ) {
        return new ReferralIncentivePaymentResponse(
                p.getId(),
                p.getIncentiveId(),
                p.getAmount(),
                p.getPaymentMode(),
                p.getReferenceId(),
                p.getPaidAt(),
                p.getRecordedByProviderId()
        );
    }
}