package com.indiclinic.referral_system.provider.external.dto;

import com.indiclinic.referral_system.provider.external.ExternalProviderType;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateExternalProviderReq {

    @NotNull (message = "Provider type is required: DOCTOR, LAB, HOSPITAL")
    public ExternalProviderType providerType;

    @NotBlank(message = "Name is required")
    public String name;

    @NotBlank(message = "Phone number is required")
    public String phone;

    public String email;

    public String specialty;
}
