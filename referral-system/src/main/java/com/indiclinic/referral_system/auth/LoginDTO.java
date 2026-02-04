package com.indiclinic.referral_system.auth;

import jakarta.validation.constraints.NotBlank;

public class LoginDTO {
    @NotBlank(message = "phone is required")
    public String phone;
}
