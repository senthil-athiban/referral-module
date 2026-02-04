package com.indiclinic.referral_system.provider.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UpdateProviderReq extends CreateProviderReq {
    @NotNull
    public Boolean active;
}
