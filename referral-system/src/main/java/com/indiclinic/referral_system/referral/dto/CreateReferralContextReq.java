package com.indiclinic.referral_system.referral.dto;

import com.indiclinic.referral_system.referral.domain.ReceiverType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class CreateReferralContextReq {
    @NotNull(message = "Patient is missing")
    public UUID patientId;

    @NotNull(message = "Referred Provider is missing")
    public UUID referrerProviderId;

    @NotNull(message = "Receiver Type is missing")
    public ReceiverType receiverType;

    public UUID receiverProviderId;

    public UUID receiverExternalProviderId;

    public UUID appliedIncentiveRuleId;

    public String clinicalSummary;

    public String clinicalNotes;

    public UUID createdByProviderId;
}
