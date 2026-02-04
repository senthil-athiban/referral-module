package com.indiclinic.referral_system.patient;

import com.indiclinic.referral_system.common.ApiException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PatientService {
    private final PatientRepository repository;

    public PatientService(PatientRepository repository) {
        this.repository = repository;
    }

    public Patient create(String name, String phone, String email) {
        repository.findByPhone(phone).ifPresent(p -> {
            throw new ApiException("Patient with phone already exists");
        });

        Patient provider = new Patient(name,phone, email);
        System.out.println("==== provider : "+ provider);
        return repository.save(provider);
    }

    public Patient findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ApiException("Patient not found"));
    }

    public Patient update(UUID id, String name, String phone, boolean active) {
        Patient providerToUpdate = findById(id);
        providerToUpdate.updateDetails(name, phone, active);
        return providerToUpdate;
    };

    public List<Patient> getAll() {
        return repository.findAll();
    }
}
