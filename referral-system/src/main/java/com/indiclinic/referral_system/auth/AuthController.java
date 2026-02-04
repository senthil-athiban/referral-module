package com.indiclinic.referral_system.auth;

import com.indiclinic.referral_system.provider.ProviderService;
import com.indiclinic.referral_system.provider.dto.CreateProviderReq;
import com.indiclinic.referral_system.provider.dto.ProviderResponseDTO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {
    private final ProviderService service;

    public AuthController(ProviderService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public ProviderResponseDTO login(@Valid @RequestBody LoginDTO req) {
        return ProviderResponseDTO.from(service.findByPhone(req.phone));
    }

    @PostMapping("/signup")
    public ProviderResponseDTO signup(@Valid @RequestBody CreateProviderReq req) {
        return ProviderResponseDTO.from(service.create(req.name, req.phone, req.email));
    }
}
