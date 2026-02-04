package com.indiclinic.referral_system.incentive.payment;

import com.indiclinic.referral_system.common.ApiException;
import com.indiclinic.referral_system.incentive.payment.dto.CreateReferralIncentivePaymentReq;
import com.indiclinic.referral_system.incentive.record.ReferralIncentive;
import com.indiclinic.referral_system.incentive.record.ReferralIncentiveRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@Transactional
public class PaymentService {
    private final ReferralIncentiveRepository incentiveRepository;
    private final PaymentRepository paymentRepository;

    public PaymentService(
            ReferralIncentiveRepository incentiveRepository,
            PaymentRepository paymentRepository
    ) {
        this.incentiveRepository = incentiveRepository;
        this.paymentRepository = paymentRepository;
    }


    public ReferralIncentivePayment recordPayment(
            UUID incentiveId,
            CreateReferralIncentivePaymentReq req
    ) {
        ReferralIncentive incentive = incentiveRepository.findById(incentiveId)
                .orElseThrow(
                        () -> new ApiException("Incentive is not found to add payment")
                );

        BigDecimal totalPaid = paymentRepository.findByIncentiveId(incentiveId)
                .stream()
                .map(ReferralIncentivePayment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal remaining = incentive.getNetAmount().subtract(totalPaid);

        if (req.amount.compareTo(remaining) > 0) {
            throw new ApiException("Payment exceeds remaining incentive amount");
        }

        ReferralIncentivePayment payment = ReferralIncentivePayment.record(
                incentive,
                incentive.getPayerType(),
                incentive.getPayerProviderId(),
                incentive.getPayerExternalProviderId(),
                incentive.getPayerLocationId(),
                req.amount,
                req.paymentMode,
                req.referenceId,
                req.recordedByProviderId
        );

        paymentRepository.save(payment);

        BigDecimal newTotal = totalPaid.add(req.amount);

        if (newTotal.compareTo(incentive.getNetAmount()) == 0) {
            incentive.markSettled();
        } else {
            incentive.markPartiallySettled();
        }

        return payment;
    }

}
