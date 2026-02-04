package com.indiclinic.referral_system.patient;

import com.indiclinic.referral_system.patient.dto.CreatePatientReq;
import com.indiclinic.referral_system.patient.dto.PatientResponseDTO;
import com.indiclinic.referral_system.patient.dto.UpdatePatientReq;
import com.indiclinic.referral_system.provider.ProviderService;
import com.indiclinic.referral_system.provider.dto.CreateProviderReq;
import com.indiclinic.referral_system.provider.dto.ProviderResponseDTO;
import com.indiclinic.referral_system.provider.dto.UpdateProviderReq;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/patient")
public class PatientController {
    private final PatientService service;

    public PatientController(PatientService svc) {
        this.service = svc;
    }

    @GetMapping
    public List<PatientResponseDTO> getAll() {
        return service.getAll().stream().map(PatientResponseDTO::from).toList();
    }


    @GetMapping("{id}")
    public PatientResponseDTO getById(@PathVariable UUID id) {
        return PatientResponseDTO.from(service.findById(id));
    }

    @PostMapping
    public PatientResponseDTO create(@RequestBody @Valid CreatePatientReq req) {
        return PatientResponseDTO.from(service.create(req.name, req.phone, req.email));
    }

    @PutMapping("{id}")
    public PatientResponseDTO update(@PathVariable UUID id, @RequestBody @Valid UpdatePatientReq req) {
        return PatientResponseDTO.from(service.update(id, req.name, req.email, req.active));
    }

}
