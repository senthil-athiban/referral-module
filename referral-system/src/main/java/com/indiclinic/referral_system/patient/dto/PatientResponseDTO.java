package com.indiclinic.referral_system.patient.dto;

import com.indiclinic.referral_system.patient.Patient;

import java.util.UUID;

public record PatientResponseDTO(
        UUID id,
        String name,
        String phone,
        String email
) {
    public static PatientResponseDTO from(Patient p) {
        return new PatientResponseDTO(
                p.getId(),
                p.getName(),
                p.getPhone(),
                p.getEmail()
        );
    }
}
