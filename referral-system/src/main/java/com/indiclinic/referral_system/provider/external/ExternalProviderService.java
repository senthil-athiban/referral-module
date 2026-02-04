package com.indiclinic.referral_system.provider.external;

import com.indiclinic.referral_system.common.ApiException;
import com.indiclinic.referral_system.provider.Provider;
import com.indiclinic.referral_system.provider.external.dto.CreateExternalProviderReq;
import com.indiclinic.referral_system.provider.external.dto.UpdateExternalProviderReq;
import com.indiclinic.referral_system.referral.service.ReferralContextService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ExternalProviderService {
    private final ExternalProviderRepository repository;
    private final ReferralContextService referralContextService;

    public ExternalProviderService(ExternalProviderRepository repository, ReferralContextService referralContextService) {
        this.repository = repository;
        this.referralContextService = referralContextService;
    }

    public ExternalProvider getOrCreate(CreateExternalProviderReq req) {
        if (req.phone != null) {
            var existing = repository.findByPhone(req.phone);
            if (existing.isPresent()) return existing.get();
        }

        if (req.email != null) {
            var existing = repository.findByEmail(req.email);
            if (existing.isPresent()) return existing.get();
        }

        ExternalProvider ep = ExternalProvider.create(
                req.providerType,
                req.name,
                req.phone,
                req.email,
                req.specialty
        );

        return repository.save(ep);
    }

    public ExternalProvider update(UUID externalProviderId, UpdateExternalProviderReq req) {
        // Prevent collisions
        if (req.phone != null) {
            repository.findByPhone(req.phone)
                    .filter(other -> !other.getId().equals(externalProviderId))
                    .ifPresent(o -> {
                        throw new ApiException("Phone already used by another external provider");
                    });
        }

        if (req.email != null) {
            repository.findByEmail(req.email)
                    .filter(other -> !other.getId().equals(externalProviderId))
                    .ifPresent(o -> {
                        throw new ApiException("Email already used by another external provider");
                    });
        }

        ExternalProvider ep = findById(externalProviderId);
        ep.update(req.name, req.phone, req.email, req.specialty, req.registrationNumber, req.locationId);
        return ep;
    }

    private void linkAndPromote(ExternalProvider ep, Provider provider) {
        System.out.println("===== EP ID ========" + ep.getId());
        System.out.println("===== provider ID ========" + provider.getId());
        System.out.println("======= STRATING ======= ");
        ep.linkToInternalProvider(provider.getId());

        System.out.println("======= LINKED INTERNAL - EXTERNAL ID ======= ");
        referralContextService.promoteExternalReceiver(
                ep.getId(),
                provider.getId()
        );
        System.out.println("======= UDPATE DONE ======= ");
    }

    public void linkIfMatches(Provider provider) {
        if (provider.getPhone() != null) {
            repository.findByPhone(provider.getPhone())
                    .filter(ep -> ep.getLinkedProviderId() == null)
                    .ifPresent(ep -> linkAndPromote(ep, provider));
        }

        if (provider.getEmail() != null) {
            repository.findByEmail(provider.getEmail())
                    .filter(ep -> ep.getLinkedProviderId() == null)
                    .ifPresent(ep -> linkAndPromote(ep, provider));
        }
    }

    public ExternalProvider findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ApiException("External provider not found"));
    }

    public List<ExternalProvider> findAll() {
        return repository.findAll();
    }
}
