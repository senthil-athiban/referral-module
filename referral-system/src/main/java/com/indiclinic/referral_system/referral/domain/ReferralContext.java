package com.indiclinic.referral_system.referral.domain;

import com.indiclinic.referral_system.common.ApiException;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "referral_context")
public class ReferralContext {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true)
    private UUID id;

    @Column(name = "patient_id", nullable = false)
    private UUID patientId;

    @Column(name = "referrer_provider_id", nullable = false)
    private UUID referrerProviderId;

    @Column(name = "referrer_location_id")
    private UUID referrerLocationId;

    @Enumerated(EnumType.STRING)
    @Column(name = "receiver_type", nullable = false)
    private ReceiverType receiverType;

    // Receiver
    @Column(name = "receiver_provider_id")
    private UUID receiverProviderId;

    @Column(name = "receiver_external_provider_id")
    private UUID receiverExternalProviderId;

    @Column(name = "receiver_location_id")
    private UUID receiverLocationId;

    // Clinical
    @Column(name = "reason_concept_code")
    private String reasonConceptCode;

    @Column(name = "clinical_summary", columnDefinition = "TEXT")
    private String clinicalSummary;

    @Column(name = "clinical_notes", columnDefinition = "TEXT")
    private String clinicalNotes;


    // Workflow
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReferralStatus status;

    @Column(name = "status_updated_at")
    private Instant statusUpdatedAt;

    @Column(name = "receiver_acknowledged_at")
    private Instant receiverAcknowledgedAt;

    /* meta */
    @Enumerated(EnumType.STRING)
    private ReferralSource source;

    @Enumerated(EnumType.STRING)
    private ReferralPriority priority;

    /* Incentives */
    @Column(name = "applied_incentive_rule_id")
    private UUID appliedIncentiveRuleId;

    /* audit */
    @Column(nullable = false)
    private UUID createdByProviderId;

    @Column(nullable = false)
    private Instant createdAt;

    protected ReferralContext() {

    }

    public static ReferralContext createNew(
            UUID patientId,
            UUID referrerProviderId,
            ReceiverType receiverType,
            UUID receiverProviderId,
            UUID receiverExternalProviderId,
            UUID appliedIncentiveRuleId,
            String clinicalSummary,
            String clinicalNotes,
            UUID createdByProviderId
    ) {
        ReferralContext rc = new ReferralContext();
        rc.patientId = patientId;
        rc.referrerProviderId = referrerProviderId;
        rc.receiverType = receiverType;
        rc.receiverProviderId = receiverProviderId;
        rc.receiverExternalProviderId = receiverExternalProviderId;
        rc.appliedIncentiveRuleId = appliedIncentiveRuleId;
        rc.clinicalSummary = clinicalSummary;
        rc.clinicalNotes = clinicalNotes;
        rc.status = ReferralStatus.SENT;
        rc.statusUpdatedAt = Instant.now();
        rc.createdByProviderId = createdByProviderId;
        rc.createdAt = Instant.now();
        return rc;
    }

    /* -------- workflow transitions -------- */
    public void ensureReceiver(UUID actedByProviderId) {
        if(actedByProviderId == null || !actedByProviderId.equals(receiverProviderId)) {
            throw new ApiException("Only receiver can perform this action");
        }
    }

    public void accept(UUID acceptedProviderId) {
        ensureReceiver(acceptedProviderId);

        if(status != ReferralStatus.SENT) {
            throw new ApiException("Referral cannot be accepted at the current state" + status);
        }
        this.status = ReferralStatus.ACCEPTED;
        this.statusUpdatedAt = Instant.now();
        this.receiverAcknowledgedAt = Instant.now();
    }

    public void reject(UUID rejectedProviderId) {
        ensureReceiver(rejectedProviderId);
        if(status != ReferralStatus.SENT) {
            throw new ApiException("Referral cannot be accepted at the current state" + status);
        }
        this.status = ReferralStatus.REJECTED;
        this.statusUpdatedAt = Instant.now();
        this.receiverAcknowledgedAt = Instant.now();
    }

    public void complete(UUID actedByProviderId) {
        if (status != ReferralStatus.ACCEPTED) {
            throw new ApiException("Only accepted referrals can be completed");
        }

        this.status = ReferralStatus.COMPLETED;
        this.statusUpdatedAt = Instant.now();
    }

    public void changeAppliedIncentiveRule(UUID ruleId) {
        if (status == ReferralStatus.ACCEPTED) {
            throw new ApiException("Cannot change incentive rule after acceptance");
        }
        this.appliedIncentiveRuleId = ruleId;
    }


    /* -------- update referral context -------- */

    public void updateClinical(
            String reasonConceptCode,
            String clinicalSummary,
            String clinicalNotes
    ) {
        ensureEditableForClinical();
        if (reasonConceptCode != null) {
            this.reasonConceptCode = reasonConceptCode;
        }

        if (clinicalSummary != null ) {
            this.clinicalSummary = clinicalSummary;
        }

        if (clinicalNotes != null) {
            this.clinicalNotes = clinicalNotes;
        }
    }

    public void changeReceiver(
            ReceiverType receiverType,
            UUID receiverProviderId,
            UUID receiverExternalProviderId,
            UUID receiverLocationId
    ) {
        ensureEditableForReceiver();
        this.receiverType = receiverType;
        this.receiverProviderId = receiverProviderId;
        this.receiverExternalProviderId = receiverExternalProviderId;
        this.receiverLocationId = receiverLocationId;
    }

    public void updatePriority(ReferralPriority priority) {
        ensureEditableForMeta();
        if (priority != null) {
            this.priority = priority;
        }
    }

    /* -------- guards -------- */
    private void ensureEditableForClinical() {
        if (status == ReferralStatus.ACCEPTED ||
                status == ReferralStatus.REJECTED ||
                status == ReferralStatus.COMPLETED) {
            throw new ApiException("Clinical details cannot be edited after acceptance");
        }
    }

    private void ensureEditableForReceiver() {
        if (receiverAcknowledgedAt != null) {
            throw new ApiException("Receiver already acknowledged this referral");
        }

        if (status == ReferralStatus.ACCEPTED ||
                status == ReferralStatus.REJECTED ||
                status == ReferralStatus.COMPLETED) {
            throw new ApiException("Receiver cannot be changed at this stage");
        }
    }

    private void ensureEditableForMeta() {
        if (status == ReferralStatus.COMPLETED) {
            throw new ApiException("Referral is completed");
        }
    }

    /* -------- getters and setters -------- */
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getPatientId() {
        return patientId;
    }

    public void setPatientId(UUID patientId) {
        this.patientId = patientId;
    }

    public UUID getReferrerProviderId() {
        return referrerProviderId;
    }

    public void setReferrerProviderId(UUID referrerProviderId) {
        this.referrerProviderId = referrerProviderId;
    }

    public UUID getReferrerLocationId() {
        return referrerLocationId;
    }

    public void setReferrerLocationId(UUID referrerLocationId) {
        this.referrerLocationId = referrerLocationId;
    }

    public ReceiverType getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(ReceiverType receiverType) {
        this.receiverType = receiverType;
    }

    public UUID getReceiverProviderId() {
        return receiverProviderId;
    }

    public void setReceiverProviderId(UUID receiverProviderId) {
        this.receiverProviderId = receiverProviderId;
    }

    public UUID getReceiverExternalProviderId() {
        return receiverExternalProviderId;
    }

    public void setReceiverExternalProviderId(UUID receiverExternalProviderId) {
        this.receiverExternalProviderId = receiverExternalProviderId;
    }

    public UUID getReceiverLocationId() {
        return receiverLocationId;
    }

    public void setReceiverLocationId(UUID receiverLocationId) {
        this.receiverLocationId = receiverLocationId;
    }

    public String getReasonConceptCode() {
        return reasonConceptCode;
    }

    public void setReasonConceptCode(String reasonConceptCode) {
        this.reasonConceptCode = reasonConceptCode;
    }

    public String getClinicalSummary() {
        return clinicalSummary;
    }

    public void setClinicalSummary(String clinicalSummary) {
        this.clinicalSummary = clinicalSummary;
    }

    public String getClinicalNotes() {
        return clinicalNotes;
    }

    public void setClinicalNotes(String clinicalNotes) {
        this.clinicalNotes = clinicalNotes;
    }

    public ReferralStatus getStatus() {
        return status;
    }

    public void setStatus(ReferralStatus status) {
        this.status = status;
    }

    public Instant getStatusUpdatedAt() {
        return statusUpdatedAt;
    }

    public void setStatusUpdatedAt(Instant statusUpdatedAt) {
        this.statusUpdatedAt = statusUpdatedAt;
    }

    public Instant getReceiverAcknowledgedAt() {
        return receiverAcknowledgedAt;
    }

    public void setReceiverAcknowledgedAt(Instant receiverAcknowledgedAt) {
        this.receiverAcknowledgedAt = receiverAcknowledgedAt;
    }

    public ReferralSource getSource() {
        return source;
    }

    public void setSource(ReferralSource source) {
        this.source = source;
    }

    public ReferralPriority getPriority() {
        return priority;
    }

    public void setPriority(ReferralPriority priority) {
        this.priority = priority;
    }

    public UUID getCreatedByProviderId() {
        return createdByProviderId;
    }

    public UUID getAppliedIncentiveRuleId() {
        return appliedIncentiveRuleId;
    }

    public void setAppliedIncentiveRuleId(UUID appliedIncentiveRuleId) {
        this.appliedIncentiveRuleId = appliedIncentiveRuleId;
    }

    public void setCreatedByProviderId(UUID createdByProviderId) {
        this.createdByProviderId = createdByProviderId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
