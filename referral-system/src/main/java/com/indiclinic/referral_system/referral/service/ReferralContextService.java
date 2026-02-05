package com.indiclinic.referral_system.referral.service;

import com.indiclinic.referral_system.common.ApiException;
import com.indiclinic.referral_system.incentive.payment.PaymentRepository;
import com.indiclinic.referral_system.incentive.payment.PaymentService;
import com.indiclinic.referral_system.incentive.payment.ReferralIncentivePayment;
import com.indiclinic.referral_system.incentive.payment.dto.ReferralContextDetailsResponse;
import com.indiclinic.referral_system.incentive.payment.dto.ReferralIncentivePaymentResponse;
import com.indiclinic.referral_system.incentive.record.ReferralIncentive;
import com.indiclinic.referral_system.incentive.record.ReferralIncentiveRepository;
import com.indiclinic.referral_system.incentive.record.ReferralIncentiveService;
import com.indiclinic.referral_system.incentive.record.dto.ReferralIncentiveResponse;
import com.indiclinic.referral_system.incentive.rule.ReferralIncentiveRule;
import com.indiclinic.referral_system.incentive.rule.ReferralIncentiveRuleRepository;
import com.indiclinic.referral_system.referral.domain.ReceiverType;
import com.indiclinic.referral_system.referral.domain.ReferralContext;
import com.indiclinic.referral_system.referral.dto.CreateReferralContextReq;
import com.indiclinic.referral_system.referral.dto.ReferralContextResponse;
import com.indiclinic.referral_system.referral.dto.ReferralContextWithIncentiveResponse;
import com.indiclinic.referral_system.referral.dto.UpdateReferralContextReq;
import com.indiclinic.referral_system.referral.repository.ReferralContextRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class ReferralContextService {
    private final ReferralContextRepository referralRepository;
    private final ReferralIncentiveService incentiveService;
    private final ReferralIncentiveRuleRepository incentiveRuleRepository;
    private final ReferralIncentiveRepository incentiveRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentService paymentService;

    public ReferralContextService(
            ReferralContextRepository referralRepository,
            ReferralIncentiveService incentiveService,
            ReferralIncentiveRuleRepository incentiveRuleRepository,
            ReferralIncentiveRepository incentiveRepository,
            PaymentRepository paymentRepository,
            PaymentService paymentService
    ) {
        this.referralRepository = referralRepository;
        this.incentiveService = incentiveService;
        this.incentiveRuleRepository = incentiveRuleRepository;
        this.incentiveRepository = incentiveRepository;
        this.paymentRepository = paymentRepository;
        this.paymentService = paymentService;
    }

    public ReferralContext create(CreateReferralContextReq req) {
        boolean hasInternalProvider = req.receiverProviderId != null;
        boolean hasExternalProvider = req.receiverExternalProviderId != null;

        if(hasInternalProvider == hasExternalProvider) {
            throw new ApiException("Provider ID is required. Either it can be Internal or External Provider");
        }

        validateAppliedIncentiveRule(req.appliedIncentiveRuleId, req.referrerProviderId);

        return referralRepository.save(
                ReferralContext.createNew(
                        req.patientId,
                        req.referrerProviderId,
                        req.receiverType,
                        req.receiverProviderId,
                        req.receiverExternalProviderId,
                        req.appliedIncentiveRuleId,
                        req.clinicalSummary,
                        req.clinicalNotes,
                        req.createdByProviderId
                )
        );
    }

    public List<ReferralContext> inboxForProvider(UUID providerId) {
        return referralRepository.findByReceiverProviderId(providerId);
    }

    public List<ReferralContext> findAll() {
        return referralRepository.findAll();
    }

    public ReferralContext findById(UUID id) {
        return referralRepository.findById(id).orElseThrow(() -> new ApiException("Referral is not found for " + id));
    }

    public ReferralContext update(UUID referralId, UpdateReferralContextReq req) {
        ReferralContext referral = findById(referralId);

        if (hasIncentiveRuleUpdate(req)) {
            validateAppliedIncentiveRule(
                    req.appliedIncentiveRuleId,
                    referral.getReferrerProviderId()
            );
            referral.changeAppliedIncentiveRule(req.appliedIncentiveRuleId);
        }

        if (hasClinicalUpdate(req)) {
            referral.updateClinical(req.reasonConceptCode, req.clinicalSummary, req.clinicalNotes);
        }

        if (hasReceiverUpdate(req)) {
            validateReceiverUpdate(req);
            referral.changeReceiver(req.receiverType, req.receiverProviderId, req.receiverExternalProviderId, req.receiverLocationId);
        }

        if (hasPriorityUpdate(req)) {
            referral.updatePriority(req.priority);
        }
        referralRepository.save(referral);
        return referral;
    }

    public List<ReferralContext> findForProvider(
            UUID providerId,
            ReceiverType receiverType
    ) {
        if (receiverType == null) {
            return referralRepository.findAllByProvider(providerId);
        }

        return referralRepository.findAllByProviderAndReceiverType(
                providerId,
                receiverType
        );
    }

    public ReferralContextDetailsResponse getDetails(UUID referralId) {

        ReferralContext referral =
                referralRepository.findById(referralId)
                        .orElseThrow(() -> new ApiException("Referral not found"));

        ReferralContextResponse referralDto =
                ReferralContextResponse.from(referral);

        Optional<ReferralIncentive> incentiveOpt =
                incentiveRepository.findByReferralId(referralId);

        if (incentiveOpt.isEmpty()) {
            return new ReferralContextDetailsResponse(
                    referralDto,
                    null
            );
        }

        ReferralIncentive incentive = incentiveOpt.get();

        List<ReferralIncentivePaymentResponse> payments =
                paymentRepository.findByIncentiveId(incentive.getId())
                        .stream()
                        .map(ReferralIncentivePaymentResponse::from)
                        .toList();

        ReferralContextDetailsResponse.ReferralIncentive incentiveDto =
                new ReferralContextDetailsResponse.ReferralIncentive(
                        ReferralIncentiveResponse.from(incentive),
                        payments
                );

        return new ReferralContextDetailsResponse(
                referralDto,
                incentiveDto
        );
    }

    /* ----- link external provider with newly created provider during onboarding ----- */

    public void promoteExternalReceiver(
            UUID externalProviderId,
            UUID providerId
    ) {
        referralRepository.linkReceiverToInternalProvider(
                externalProviderId,
                providerId
        );
    }


    /* ----- workflow helpers ----- */

    public ReferralContext accept(UUID referralId, UUID actedByProviderId) {
        ReferralContext referral = findById(referralId);
        referral.accept(actedByProviderId);
        referralRepository.save(referral);
        incentiveService.createForAcceptedReferral(referral);
        return referral;
    }

    public ReferralContext reject(UUID referralId, UUID actedByProviderId) {
        ReferralContext referral = findById(referralId);
        referral.reject(actedByProviderId);
        return referralRepository.save(referral);
    }

    /* ----- incentive helpers ----- */

    private boolean hasIncentiveRuleUpdate(UpdateReferralContextReq req) {
        return isValidValue(req.appliedIncentiveRuleId);
    }


    private void validateAppliedIncentiveRule(
            UUID ruleId,
            UUID referrerProviderId
    ) {
        if (ruleId == null) return;

        ReferralIncentiveRule rule =
                incentiveRuleRepository.findById(ruleId)
                        .orElseThrow(() -> new ApiException("Incentive rule not found"));

        if (!rule.getOwnerProviderId().equals(referrerProviderId)) {
            throw new ApiException("Incentive rule does not belong to referrer");
        }

        if (!rule.isActive()) {
            throw new ApiException("Incentive rule is inactive");
        }
    }


    private void validateReceiverUpdate(UpdateReferralContextReq req) {
        if (req.receiverType == null) {
            throw new ApiException("Receiver type is required");
        }

        switch (req.receiverType) {
            case PROVIDER -> {
                if (req.receiverProviderId == null && req.receiverExternalProviderId == null) {
                    throw new ApiException(
                            "Receiver provider ID is required for INTERNAL/EXTERNAL receiver"
                    );
                }
            }
            case LAB -> {
                if (req.receiverLocationId == null) {
                    throw new ApiException(
                            "Location ID is required for LOCATION receiver"
                    );
                }
            }
        }
    }

    private boolean isValidValue(Object value) {
        if (value == null) return false;

        if (value instanceof String s) {
            return !s.isBlank();
        }

        if (value instanceof Collection<?> c) {
            return !c.isEmpty();
        }

        return true;
    }

    private boolean hasClinicalUpdate(UpdateReferralContextReq req) {
        return isValidValue(req.reasonConceptCode)
                || isValidValue(req.clinicalSummary)
                || isValidValue(req.clinicalNotes);
    }

    private boolean hasReceiverUpdate(UpdateReferralContextReq req) {
        return  isValidValue(req.receiverProviderId)
                || isValidValue(req.receiverExternalProviderId)
                || isValidValue(req.receiverLocationId);
    }

    private boolean hasPriorityUpdate(UpdateReferralContextReq req) {
        return isValidValue(req.priority);
    }

    /* DTO based responses */
    public List<ReferralContextWithIncentiveResponse> findAllForProvider(UUID providerId) {

        List<ReferralContext> referrals =
                referralRepository.findAllByProvider(providerId);

        return referrals.stream().map(referral -> {

            Optional<ReferralIncentive> incentiveOpt =
                    incentiveService.findOptionalByReferralId(referral.getId());

            List<ReferralIncentivePayment> payments =
                    incentiveOpt.map(i ->
                            paymentService.findByIncentiveId(i.getId())
                    ).orElse(List.of());

            return ReferralContextWithIncentiveResponse.from(
                    referral,
                    incentiveOpt.orElse(null),
                    payments
            );
        }).toList();
    }


}
