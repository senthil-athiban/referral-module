package com.indiclinic.referral_system.referral.api;

import com.indiclinic.referral_system.referral.dto.ReferralContextResponse;
import com.indiclinic.referral_system.referral.dto.ReferralWorkflowActionReq;
import com.indiclinic.referral_system.referral.service.ReferralWorkflowService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/referral")
public class ReferralWorkflowController {

    private final ReferralWorkflowService service;

    public ReferralWorkflowController(ReferralWorkflowService service) {
        this.service = service;
    }

    @PostMapping("/{id}/workflow")
    public ReferralContextResponse processWorkflow(@PathVariable UUID id, @Valid @RequestBody ReferralWorkflowActionReq req) {
        return ReferralContextResponse.from(service.processWorkflow(id, req.actedByProviderId, req.action));
    }
}
