package com.indiclinic.referral_system.incentive.payment.dto;

import com.indiclinic.referral_system.incentive.payment.PaymentMode;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public class CreateReferralIncentivePaymentReq {

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than zero")
    public BigDecimal amount;

    @NotNull(message = "Payment mode is required")
    public PaymentMode paymentMode;

    public String referenceId;

    @NotNull(message = "Recorded by provider is required")
    public UUID recordedByProviderId;
}