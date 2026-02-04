package com.indiclinic.referral_system.referral.dto;

import com.indiclinic.referral_system.referral.domain.ReferralAction;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class ReferralWorkflowActionReq {
    @NotNull(message = "Action is required")
    public ReferralAction action;

    @NotNull(message = "Acting provider is required")
    public UUID actedByProviderId;
}
