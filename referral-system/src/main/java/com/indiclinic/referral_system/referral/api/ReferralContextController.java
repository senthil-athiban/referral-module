package com.indiclinic.referral_system.referral.api;
import com.indiclinic.referral_system.incentive.payment.dto.ReferralContextDetailsResponse;
import com.indiclinic.referral_system.referral.domain.ReceiverType;
import com.indiclinic.referral_system.referral.dto.CreateReferralContextReq;
import com.indiclinic.referral_system.referral.dto.ReferralContextResponse;
import com.indiclinic.referral_system.referral.dto.ReferralContextWithIncentiveResponse;
import com.indiclinic.referral_system.referral.dto.UpdateReferralContextReq;
import com.indiclinic.referral_system.referral.service.ReferralContextService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/referral")
public class ReferralContextController {

    private final ReferralContextService service;

    public ReferralContextController(ReferralContextService service) {
        this.service = service;
    }

    @PostMapping
    public ReferralContextResponse create(@Valid  @RequestBody CreateReferralContextReq req) {
        return ReferralContextResponse.from(service.create(req));
    }

//    @GetMapping
//    public List<ReferralContextResponse> getAll() {
//        return service.findAll().stream().map(ReferralContextResponse::from).toList();
//    }

//    @GetMapping
//    public List<ReferralContextResponse> getAll(
//            @RequestParam UUID providerId,
//            @RequestParam(required = false) ReceiverType receiverType
//    ) {
//        return service.findForProvider(providerId, receiverType)
//                .stream()
//                .map(ReferralContextResponse::from)
//                .toList();
//    }

    @GetMapping
    public List<ReferralContextWithIncentiveResponse> getAllByProvider(
            @RequestParam UUID providerId,
            @RequestParam(required = false) ReceiverType receiverType
    ) {
        return service.findAllForProvider(providerId);
    }


    @GetMapping("/inbox/{providerId}")
    public List<ReferralContextResponse> inbox(@PathVariable UUID providerId) {
        return service.inboxForProvider(providerId).stream().map(ReferralContextResponse::from).toList();
    }

    @GetMapping("/{referralId}")
    public List<ReferralContextResponse> getById(@PathVariable UUID referralId) {
        return service.inboxForProvider(referralId).stream().map(ReferralContextResponse::from).toList();
    }

    @PatchMapping("/{referralId}")
    public ReferralContextResponse update(@PathVariable UUID referralId, @Valid @RequestBody UpdateReferralContextReq req){
        return ReferralContextResponse.from(service.update(referralId, req));
    }

    @PostMapping("/{id}/accept")
    public ReferralContextResponse accept(
            @PathVariable UUID id,
            @RequestParam UUID actedBy
    ) {
        return ReferralContextResponse.from(
                service.accept(id, actedBy)
        );
    }

    @PostMapping("/{id}/reject")
    public ReferralContextResponse reject(
            @PathVariable UUID id,
            @RequestParam UUID actedBy
    ) {
        return ReferralContextResponse.from(
                service.reject(id, actedBy)
        );
    }

    @GetMapping("/{id}/details")
    public ReferralContextDetailsResponse getReferralDetails(
            @PathVariable UUID id
    ) {
        return service.getDetails(id);
    }


}
