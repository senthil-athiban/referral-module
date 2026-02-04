package com.indiclinic.referral_system.incentive.rule;

import com.indiclinic.referral_system.incentive.rule.dto.CreateReferralIncentiveRuleReq;
import com.indiclinic.referral_system.incentive.rule.dto.ReferralIncentiveRuleResponse;
import com.indiclinic.referral_system.incentive.rule.dto.UpdateReferralIncentiveRuleReq;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/incentive-rule")
public class ReferralIncentiveRuleController {
    private final ReferralIncentiveRuleService service;

    public ReferralIncentiveRuleController(ReferralIncentiveRuleService service) {
        this.service = service;
    }

    @PostMapping
    public ReferralIncentiveRuleResponse createIncentiveRule(
            @Valid @RequestBody CreateReferralIncentiveRuleReq req) {
        return ReferralIncentiveRuleResponse.from(
                service.create(
                        req.ownerProviderId(),
                        req.referralTargetType(),
                        req.name(),
                        req.description(),
                        req.amount()
                )
        );
    }

    @GetMapping
    public List<ReferralIncentiveRuleResponse> list(
            @RequestParam UUID ownerProviderId
    ) {
        return service.listByProvider(ownerProviderId)
                .stream()
                .map(ReferralIncentiveRuleResponse::from)
                .toList();
    }

    @PostMapping("/{id}/deactivate")
    public void deactivate(@PathVariable UUID id, @RequestParam UUID actedBy) {
        service.deactivate(id, actedBy);
    }

    @PatchMapping("/{id}")
    public ReferralIncentiveRuleResponse update(
            @PathVariable UUID id,
            @RequestParam UUID actedBy,
            @RequestBody UpdateReferralIncentiveRuleReq req
    ) {
        return ReferralIncentiveRuleResponse.from(
                service.update(id, actedBy, req)
        );
    }

}
