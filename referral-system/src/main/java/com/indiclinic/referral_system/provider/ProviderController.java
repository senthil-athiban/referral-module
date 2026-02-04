package com.indiclinic.referral_system.provider;

import com.indiclinic.referral_system.provider.ProviderService;
import com.indiclinic.referral_system.provider.dto.CreateProviderReq;
import com.indiclinic.referral_system.provider.dto.ProviderResponseDTO;
import com.indiclinic.referral_system.provider.dto.UpdateProviderReq;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/provider")
public class ProviderController {
    private final ProviderService service;

    public ProviderController(ProviderService svc) {
        this.service = svc;
    }

    @GetMapping
    public List<ProviderResponseDTO> getAll() {
        return service.getAll().stream().map(ProviderResponseDTO::from).toList();
    }


    @GetMapping("{id}")
    public ProviderResponseDTO getById(@PathVariable UUID id) {
        return ProviderResponseDTO.from(service.findById(id));
    }

    @PostMapping
    public ProviderResponseDTO create(@RequestBody @Valid CreateProviderReq req) {
        return ProviderResponseDTO.from(service.create(req.name, req.phone, req.email));
    }

    @PutMapping("{id}")
    public ProviderResponseDTO update(@PathVariable UUID id, @RequestBody @Valid UpdateProviderReq req) {
        return ProviderResponseDTO.from(service.update(id, req.name, req.email, req.active));
    }

}
