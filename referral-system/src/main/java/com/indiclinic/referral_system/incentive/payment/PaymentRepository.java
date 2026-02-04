package com.indiclinic.referral_system.incentive.payment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PaymentRepository
        extends JpaRepository<ReferralIncentivePayment, UUID> {

    List<ReferralIncentivePayment> findByIncentiveId(UUID incentiveId);
}
