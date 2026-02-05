package com.indiclinic.referral_system.referral.dto;

import com.indiclinic.referral_system.incentive.payment.dto.ReferralIncentivePaymentResponse;
import com.indiclinic.referral_system.referral.domain.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ReferralContextWithIncentiveResponse(

        /* identity */
        UUID id,
        Instant createdAt,

        /* subject */
        UUID patientId,

        /* referrer */
        UUID referrerProviderId,
        UUID referrerLocationId,

        /* receiver */
        Receiver receiver,

        /* clinical */
        String reasonConceptCode,
        String clinicalSummary,
        String clinicalNotes,

        /* workflow */
        ReferralStatus status,
        Instant statusUpdatedAt,
        Instant receiverAcknowledgedAt,

        /* meta */
        ReferralSource source,
        ReferralPriority priority,

        /* financial */
        Incentive incentive
) {

    /* ---------- nested value objects ---------- */

    public record Receiver(
            ReceiverType type,
            UUID providerId,
            UUID externalProviderId,
            UUID locationId
    ) {}

    public record Incentive(
            UUID id,
            UUID beneficiaryProviderId,
            UUID ruleId,
            java.math.BigDecimal baseAmount,
            java.math.BigDecimal netAmount,
            com.indiclinic.referral_system.incentive.record.SettlementStatus settlementStatus,
            Instant createdAt,
            List<ReferralIncentivePaymentResponse> payments
    ) {}

    /* ---------- mapper ---------- */

    public static ReferralContextWithIncentiveResponse from(
            ReferralContext rc,
            com.indiclinic.referral_system.incentive.record.ReferralIncentive incentive,
            List<com.indiclinic.referral_system.incentive.payment.ReferralIncentivePayment> payments
    ) {

        Receiver receiver = new Receiver(
                rc.getReceiverType(),
                rc.getReceiverProviderId(),
                rc.getReceiverExternalProviderId(),
                rc.getReceiverLocationId()
        );

        Incentive incentiveDto = null;

        if (incentive != null) {
            incentiveDto = new Incentive(
                    incentive.getId(),
                    incentive.getBeneficiaryProviderId(),
                    incentive.getAppliedRuleId(),
                    incentive.getBaseAmount(),
                    incentive.getNetAmount(),
                    incentive.getSettlementStatus(),
                    incentive.getCreatedAt(),
                    payments.stream()
                            .map(ReferralIncentivePaymentResponse::from)
                            .toList()
            );
        }

        return new ReferralContextWithIncentiveResponse(
                rc.getId(),
                rc.getCreatedAt(),

                rc.getPatientId(),

                rc.getReferrerProviderId(),
                rc.getReferrerLocationId(),

                receiver,

                rc.getReasonConceptCode(),
                rc.getClinicalSummary(),
                rc.getClinicalNotes(),

                rc.getStatus(),
                rc.getStatusUpdatedAt(),
                rc.getReceiverAcknowledgedAt(),

                rc.getSource(),
                rc.getPriority(),

                incentiveDto
        );
    }
}
