package com.indiclinic.referral_system.incentive.record;

import com.indiclinic.referral_system.incentive.record.dto.ReferralIncentiveResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/incentives")
public class ReferralIncentiveController {
    private final ReferralIncentiveService service;

    public ReferralIncentiveController(ReferralIncentiveService service) {
        this.service = service;
    }

    @GetMapping("/earned")
    public List<ReferralIncentiveResponse> earned(
            @RequestParam UUID providerId
    ) {
        return service.incentivesEarnedBy(providerId)
                .stream()
                .map(ReferralIncentiveResponse::from)
                .toList();
    }
}
