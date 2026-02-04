package com.indiclinic.referral_system.incentive.record;

import com.indiclinic.referral_system.common.ApiException;
import com.indiclinic.referral_system.incentive.rule.ReferralIncentiveRule;
import com.indiclinic.referral_system.referral.domain.ReceiverType;
import com.indiclinic.referral_system.referral.domain.ReferralContext;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "referral_incentive")
public class ReferralIncentive {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "referral_id", nullable = false, unique = true)
    private UUID referralId;

    @Column(name = "applied_rule_id", nullable = false)
    private UUID appliedRuleId;

    @Column(name = "beneficiary_provider_id", nullable = false)
    private UUID beneficiaryProviderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "payer_type", nullable = false)
    private ReceiverType payerType;

    @Column(name = "payer_provider_id")
    private UUID payerProviderId;

    @Column(name = "payer_external_provider_id")
    private UUID payerExternalProviderId;

    @Column(name = "payer_location_id")
    private UUID payerLocationId;

    @Column(name = "base_amount", nullable = false)
    private BigDecimal baseAmount;

    @Column(name = "negotiated_amount")
    private BigDecimal negotiatedAmount;

    @Column(name = "net_amount", nullable = false)
    private BigDecimal netAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SettlementStatus settlementStatus;

    @Column(nullable = false)
    private Instant createdAt;

    protected ReferralIncentive() {

    }

    public static ReferralIncentive create(
            ReferralContext referral,
            ReferralIncentiveRule rule
    ) {
        ReferralIncentive ri = new ReferralIncentive();
        ri.referralId = referral.getId();
        ri.appliedRuleId = rule.getId();
        ri.beneficiaryProviderId = referral.getReferrerProviderId();

        ri.payerType = referral.getReceiverType();
        ri.payerLocationId = referral.getReceiverLocationId();
        ri.payerProviderId = referral.getReceiverProviderId();
        ri.payerExternalProviderId = referral.getReceiverExternalProviderId();

        ri.baseAmount = rule.getDefaultAmount();
        ri.netAmount = rule.getDefaultAmount();

        ri.settlementStatus = SettlementStatus.PENDING;
        ri.createdAt = Instant.now();
        return ri;
    }

    /* ---------- negotiation ---------- */
    public void negotiate(BigDecimal negotiatedAmount) {
        if (negotiatedAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ApiException("Negotiated amount must be positive");
        }
        this.negotiatedAmount = negotiatedAmount;
        this.netAmount = negotiatedAmount;
    }

    public void markSettled() {
        this.settlementStatus = SettlementStatus.SETTLED;
    }

    public void markPartiallySettled() {
        this.settlementStatus = SettlementStatus.PARTIALLY_SETTLED;
    }

    /* ---------- getters and setters ---------- */

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getReferralId() {
        return referralId;
    }

    public void setReferralId(UUID referralId) {
        this.referralId = referralId;
    }

    public UUID getAppliedRuleId() {
        return appliedRuleId;
    }

    public void setAppliedRuleId(UUID appliedRuleId) {
        this.appliedRuleId = appliedRuleId;
    }

    public UUID getBeneficiaryProviderId() {
        return beneficiaryProviderId;
    }

    public void setBeneficiaryProviderId(UUID beneficiaryProviderId) {
        this.beneficiaryProviderId = beneficiaryProviderId;
    }

    public ReceiverType getPayerType() {
        return payerType;
    }

    public void setPayerType(ReceiverType payerType) {
        this.payerType = payerType;
    }

    public UUID getPayerProviderId() {
        return payerProviderId;
    }

    public void setPayerProviderId(UUID payerProviderId) {
        this.payerProviderId = payerProviderId;
    }

    public UUID getPayerExternalProviderId() {
        return payerExternalProviderId;
    }

    public void setPayerExternalProviderId(UUID payerExternalProviderId) {
        this.payerExternalProviderId = payerExternalProviderId;
    }

    public UUID getPayerLocationId() {
        return payerLocationId;
    }

    public void setPayerLocationId(UUID payerLocationId) {
        this.payerLocationId = payerLocationId;
    }

    public BigDecimal getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(BigDecimal baseAmount) {
        this.baseAmount = baseAmount;
    }

    public BigDecimal getNegotiatedAmount() {
        return negotiatedAmount;
    }

    public void setNegotiatedAmount(BigDecimal negotiatedAmount) {
        this.negotiatedAmount = negotiatedAmount;
    }

    public BigDecimal getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(BigDecimal netAmount) {
        this.netAmount = netAmount;
    }

    public SettlementStatus getSettlementStatus() {
        return settlementStatus;
    }

    public void setSettlementStatus(SettlementStatus settlementStatus) {
        this.settlementStatus = settlementStatus;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
