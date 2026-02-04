package com.indiclinic.referral_system.patient.dto;

import jakarta.validation.constraints.NotBlank;

public class CreatePatientReq {
    @NotBlank(message = "name is required")
    public String name;

    @NotBlank(message = "email is required")
    public String email;

    @NotBlank(message = "phone is required")
    public String phone;
}
