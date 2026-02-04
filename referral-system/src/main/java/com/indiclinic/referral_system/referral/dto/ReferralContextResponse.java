package com.indiclinic.referral_system.referral.dto;

import com.indiclinic.referral_system.referral.domain.*;

import java.time.Instant;
import java.util.UUID;

public record ReferralContextResponse(

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
        ReferralPriority priority
) {

    /* ---------- nested value object ---------- */
    public record Receiver(
            ReceiverType type,
            UUID providerId,
            UUID externalProviderId,
            UUID locationId
    ) {}

    /* ---------- mapper ---------- */
    public static ReferralContextResponse from(ReferralContext rc) {

        Receiver receiver = new Receiver(
                rc.getReceiverType(),
                rc.getReceiverProviderId(),
                rc.getReceiverExternalProviderId(),
                rc.getReceiverLocationId()
        );

        return new ReferralContextResponse(
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
                rc.getPriority()
        );
    }
}
