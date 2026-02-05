package com.indiclinic.referral_system.referral.api;

import com.indiclinic.referral_system.referral.dto.DoctorNetworkEntry;
import com.indiclinic.referral_system.referral.service.DoctorNetworkService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/network")
public class DoctorNetworkController {

    private final DoctorNetworkService service;

    public DoctorNetworkController(DoctorNetworkService service) {
        this.service = service;
    }

    @GetMapping("/{providerId}")
    public List<DoctorNetworkEntry> getNetwork(
            @PathVariable UUID providerId
    ) {
        return service.getDoctorNetwork(providerId);
    }
}
