package com.indiclinic.referral_system.incentive.rule;

import com.indiclinic.referral_system.common.ApiException;
import com.indiclinic.referral_system.referral.domain.ReceiverType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "referral_incentive_rule")
public class ReferralIncentiveRule {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "owner_provider_id", nullable = false)
    private UUID ownerProviderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "referral_target_type", nullable = false)
    private ReceiverType referralTargetType;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "default_amount", nullable = false)
    private BigDecimal defaultAmount;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected ReferralIncentiveRule() {

    }

    public static ReferralIncentiveRule create(
            UUID ownerProviderId,
            ReceiverType referralTargetType,
            String name,
            String description,
            BigDecimal defaultAmount
    ) {
        if (defaultAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ApiException("Incentive amount must be greater than zero");
        }

        ReferralIncentiveRule rule = new ReferralIncentiveRule();
        rule.ownerProviderId = ownerProviderId;
        rule.referralTargetType = referralTargetType;
        rule.name = name;
        rule.description = description;
        rule.defaultAmount = defaultAmount;
        rule.active = true;
        rule.createdAt = Instant.now();
        return rule;
    }

    public void update(
            ReceiverType referralTargetType,
            String name,
            String description,
            BigDecimal defaultAmount
    ) {
        if (name != null && !name.isBlank()) {
            this.name = name;
        }

        if (description != null) {
            this.description = description;
        }

        if (referralTargetType != null) {
            this.referralTargetType = referralTargetType;
        }

        if (defaultAmount != null) {
            if (defaultAmount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new ApiException("Incentive amount must be greater than zero");
            }
            this.defaultAmount = defaultAmount;
        }
    }


    public void deactivate() {
        this.active = false;
    }

    public void activate() {
        this.active = true;
    }

    public boolean isActive() {
        return this.active == true;
    }

    /* Getters & Setters */

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getOwnerProviderId() {
        return ownerProviderId;
    }

    public void setOwnerProviderId(UUID ownerProviderId) {
        this.ownerProviderId = ownerProviderId;
    }

    public ReceiverType getReferralTargetType() {
        return referralTargetType;
    }

    public void setReferralTargetType(ReceiverType referralTargetType) {
        this.referralTargetType = referralTargetType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getDefaultAmount() {
        return defaultAmount;
    }

    public void setDefaultAmount(BigDecimal defaultAmount) {
        this.defaultAmount = defaultAmount;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
