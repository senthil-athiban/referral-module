package com.indiclinic.referral_system.provider.external;

import com.indiclinic.referral_system.provider.external.dto.UpdateExternalProviderReq;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "external_provider")
public class ExternalProvider {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "email")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider_type", nullable = false)
    private ExternalProviderType externalProviderType;

    @Column(name = "registration_number")
    private String registrationNumber;

    @Column(name = "specialty")
    private String specialty;

    @Column(name = "location_id")
    private UUID locationId;

    @Column(name = "linked_provider_id")
    private UUID linkedProviderId;

    @Column(nullable = false)
    private Instant createdAt;

    protected ExternalProvider () {

    }

    public static ExternalProvider create(
            ExternalProviderType type,
            String name,
            String phone,
            String email,
            String specialty
    ) {
        ExternalProvider ep = new ExternalProvider();
        ep.externalProviderType = type;
        ep.email = email;
        ep.name = name;
        ep.phone = phone;
        ep.specialty = specialty;
        ep.createdAt = Instant.now();
        return ep;
    }

    public void update(
            String name,
            String phone,
            String email,
            String specialty,
            String registrationNumber,
            UUID locationId
    ) {
        System.out.println(" ===== name ===== " + name);
        if (name != null) this.name = name;
        if (phone != null) this.phone = phone;
        if (email != null) this.email = email;
        if (specialty != null) this.specialty = specialty;
        if (registrationNumber != null) this.registrationNumber = registrationNumber;
        if (locationId != null) this.locationId = locationId;
    }

    public void linkToInternalProvider(UUID linkedProviderId) {
        this.linkedProviderId = linkedProviderId;
    }

    /* -----------  GETTERS & SETTERS -----------*/

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ExternalProviderType getExternalProviderType() {
        return externalProviderType;
    }

    public void setExternalProviderType(ExternalProviderType externalProviderType) {
        this.externalProviderType = externalProviderType;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getSpeciality() {
        return specialty;
    }

    public void setSpeciality(String specialty) {
        this.specialty = specialty;
    }

    public UUID getLocationId() {
        return locationId;
    }

    public void setLocationId(UUID locationId) {
        this.locationId = locationId;
    }

    public UUID getLinkedProviderId() {
        return linkedProviderId;
    }

    public void setLinkedProviderId(UUID linkedProviderId) {
        this.linkedProviderId = linkedProviderId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
