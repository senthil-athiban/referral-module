package com.indiclinic.referral_system.provider.dto;

import com.indiclinic.referral_system.provider.Provider;

import java.util.UUID;

public record ProviderResponseDTO(
        UUID id,
        String name,
        String phone,
        String email
) {
    public static ProviderResponseDTO from(Provider p) {
        return new ProviderResponseDTO(
                p.getId(),
                p.getName(),
                p.getPhone(),
                p.getEmail()
        );
    }
}
