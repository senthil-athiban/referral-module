package com.indiclinic.referral_system.provider.external.dto;

import com.indiclinic.referral_system.provider.external.ExternalProvider;
import com.indiclinic.referral_system.provider.external.ExternalProviderType;

import java.time.Instant;
import java.util.UUID;

public record ExternalProviderResponse(
        UUID id,
        ExternalProviderType providerType,
        String name,
        String phone,
        String email,
        String specialty,
        UUID linkedProviderId,
        Instant createdAt
) {
    public static ExternalProviderResponse from(ExternalProvider ep) {
        return new ExternalProviderResponse(
                ep.getId(),
                ep.getExternalProviderType(),
                ep.getName(),
                ep.getPhone(),
                ep.getEmail(),
                ep.getSpeciality(),
                ep.getLinkedProviderId(),
                ep.getCreatedAt()
        );
    }
}
