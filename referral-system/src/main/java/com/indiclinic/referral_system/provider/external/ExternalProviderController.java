package com.indiclinic.referral_system.provider.external;

import com.indiclinic.referral_system.provider.external.dto.CreateExternalProviderReq;
import com.indiclinic.referral_system.provider.external.dto.ExternalProviderResponse;
import com.indiclinic.referral_system.provider.external.dto.UpdateExternalProviderReq;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/external-provider")
public class ExternalProviderController {

    private final ExternalProviderService service;

    public ExternalProviderController(ExternalProviderService service) {
        this.service = service;
    }

    @GetMapping
    public List<ExternalProviderResponse> getAll() {
        return service.findAll().stream().map(ExternalProviderResponse::from).toList();
    }

    @PostMapping
    public ExternalProviderResponse create(@Valid @RequestBody CreateExternalProviderReq req) {
        return ExternalProviderResponse.from(service.getOrCreate(req));
    }

    @GetMapping("/{id}")
    public ExternalProviderResponse get(@PathVariable UUID id) {
        return ExternalProviderResponse.from(
                service.findById(id)
        );
    }

    @PatchMapping("/{id}")
    public ExternalProviderResponse update(@PathVariable UUID id, @RequestBody UpdateExternalProviderReq req) {
        return ExternalProviderResponse.from(service.update(id, req));
    }
}
