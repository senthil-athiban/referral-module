package com.indiclinic.referral_system.incentive.payment;

import com.indiclinic.referral_system.incentive.payment.dto.CreateReferralIncentivePaymentReq;
import com.indiclinic.referral_system.incentive.payment.dto.ReferralIncentivePaymentResponse;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/incentives")
public class PaymentController {

    private final PaymentService service;

    public PaymentController(
            PaymentService service
    ) {
        this.service = service;
    }

    @PostMapping("/{incentiveId}/payments")
    public ReferralIncentivePaymentResponse record(
            @PathVariable UUID incentiveId,
            @RequestBody CreateReferralIncentivePaymentReq req
    ) {
        return ReferralIncentivePaymentResponse.from(
                service.recordPayment(incentiveId, req)
        );
    }
}
