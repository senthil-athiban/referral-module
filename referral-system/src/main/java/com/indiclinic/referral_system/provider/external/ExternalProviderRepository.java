package com.indiclinic.referral_system.provider.external;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExternalProviderRepository extends JpaRepository<ExternalProvider, UUID> {
    Optional<ExternalProvider> findByPhone(String phone);
    Optional<ExternalProvider> findByEmail(String email);
}
