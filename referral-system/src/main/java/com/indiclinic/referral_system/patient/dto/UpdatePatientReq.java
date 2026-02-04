package com.indiclinic.referral_system.patient.dto;

import jakarta.validation.constraints.NotNull;

public class UpdatePatientReq extends CreatePatientReq {
    @NotNull
    public Boolean active;
}
