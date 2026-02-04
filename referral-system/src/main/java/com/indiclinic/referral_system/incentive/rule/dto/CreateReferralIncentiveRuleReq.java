package com.indiclinic.referral_system.incentive.rule.dto;

import com.indiclinic.referral_system.referral.domain.ReceiverType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateReferralIncentiveRuleReq (
        @NotNull(message = "Provider id is required")
        UUID ownerProviderId,

        @NotNull(message = "ReceiverType is required: PROVIDER, LAB, FACILITY")
        ReceiverType referralTargetType,

        @NotBlank(message = "name is required")
        String name,

        String description,

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than zero")
        BigDecimal amount
) {

}
