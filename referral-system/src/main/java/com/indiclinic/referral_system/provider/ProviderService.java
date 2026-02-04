package com.indiclinic.referral_system.provider;

import com.indiclinic.referral_system.common.ApiException;
import com.indiclinic.referral_system.provider.external.ExternalProviderService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ProviderService {
    private final ProviderRepository repository;
    private final ExternalProviderService externalProviderService;

    public ProviderService(ProviderRepository repository, ExternalProviderService externalProviderService) {
            this.repository = repository;
            this.externalProviderService = externalProviderService;
    }

    public Provider create(String name, String phone, String email) {
        repository.findByPhone(phone).ifPresent(p -> {
            throw new ApiException("Provider with phone already exists");
        });

        Provider provider = new Provider(name,phone, email);

        repository.save(provider);

        /* Link External provider with internal provider */
        externalProviderService.linkIfMatches(provider);
        return provider;
    }

    public Provider findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ApiException("Provider not found"));
    }

    public Provider findByPhone(String phone) {
        System.out.println("=== phone === :" + phone);
        return repository.findByPhone(phone)
                .orElseThrow(() -> new ApiException("Provider not found"));
    }

    public Provider update(UUID id, String name, String phone, boolean active) {
        Provider providerToUpdate = findById(id);
        providerToUpdate.updateDetails(name, phone, active);

        /* Link External provider with internal provider */
        if (phone != null) {
            externalProviderService.linkIfMatches(providerToUpdate);
        }
        return providerToUpdate;
    };

    public List<Provider> getAll() {
        return repository.findAll();
    }
}
