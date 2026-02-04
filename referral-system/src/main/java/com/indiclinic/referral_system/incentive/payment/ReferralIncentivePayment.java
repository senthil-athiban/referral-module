package com.indiclinic.referral_system.incentive.payment;

import com.indiclinic.referral_system.common.ApiException;
import com.indiclinic.referral_system.incentive.record.ReferralIncentive;
import com.indiclinic.referral_system.referral.domain.ReceiverType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "referral_incentive_payment")
public class ReferralIncentivePayment {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "incentive_id", nullable = false)
    private UUID incentiveId;

    @Enumerated(EnumType.STRING)
    @Column(name = "payer_type", nullable = false)
    private ReceiverType payerType;

    @Column(name = "payer_provider_id")
    private UUID payerProviderId;

    @Column(name = "payer_external_provider_id")
    private UUID payerExternalProviderId;

    @Column(name = "payer_location_id")
    private UUID payerLocationId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_mode")
    private PaymentMode paymentMode;

    @Column(name = "reference_id")
    private String referenceId;

    @Column(name = "paid_at", nullable = false)
    private Instant paidAt;

    @Column(name = "recorded_by_provider_id", nullable = false)
    private UUID recordedByProviderId;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected ReferralIncentivePayment() {}

    public static ReferralIncentivePayment record(
            ReferralIncentive incentive,
            ReceiverType payerType,
            UUID payerProviderId,
            UUID payerExternalProviderId,
            UUID payerLocationId,
            BigDecimal amount,
            PaymentMode mode,
            String referenceId,
            UUID recordedByProviderId
    ) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ApiException("Payment amount must be positive");
        }

        ReferralIncentivePayment p = new ReferralIncentivePayment();
        p.incentiveId = incentive.getId();

        p.payerType = payerType;
        p.payerProviderId = payerProviderId;
        p.payerExternalProviderId = payerExternalProviderId;
        p.payerLocationId = payerLocationId;

        p.amount = amount;
        p.paymentMode = mode;
        p.referenceId = referenceId;

        p.paidAt = Instant.now();
        p.recordedByProviderId = recordedByProviderId;
        p.createdAt = Instant.now();

        return p;
    }

    /* ------- Getters & Setters ------- */
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getIncentiveId() {
        return incentiveId;
    }

    public void setIncentiveId(UUID incentiveId) {
        this.incentiveId = incentiveId;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentMode getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(PaymentMode paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public Instant getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(Instant paidAt) {
        this.paidAt = paidAt;
    }

    public UUID getRecordedByProviderId() {
        return recordedByProviderId;
    }

    public void setRecordedByProviderId(UUID recordedByProviderId) {
        this.recordedByProviderId = recordedByProviderId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
