package com.indiclinic.referral_system.incentive.rule;

import com.indiclinic.referral_system.common.ApiException;
import com.indiclinic.referral_system.incentive.rule.dto.UpdateReferralIncentiveRuleReq;
import com.indiclinic.referral_system.provider.ProviderService;
import com.indiclinic.referral_system.referral.domain.ReceiverType;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Transactional
@Service
public class ReferralIncentiveRuleService {
    private final ReferralIncentiveRuleRepository repository;
    private final ProviderService providerService;

    public ReferralIncentiveRuleService(
            ReferralIncentiveRuleRepository repository,
            ProviderService providerService
    ) {
        this.repository = repository;
        this.providerService = providerService;
    }

    public ReferralIncentiveRule create(
            UUID ownerProviderId,
            ReceiverType referralTargetType,
            String name,
            String description,
            BigDecimal amount
    ) {
        // Validation
        ensureProviderExistence(ownerProviderId);

        // Rule creation
        ReferralIncentiveRule rule = ReferralIncentiveRule.create(
                ownerProviderId,
                referralTargetType,
                name,
                description,
                amount
        );
        System.out.println("==== created rule : ==== " + rule);
        repository.save(rule);
        return rule;
    }

    public ReferralIncentiveRule update(
            UUID ruleId,
            UUID actedByProviderId,
            UpdateReferralIncentiveRuleReq req
    ) {

        ReferralIncentiveRule rule = getOwnedRule(ruleId, actedByProviderId);

        rule.update(
                req.referralTargetType,
                req.name,
                req.description,
                req.defaultAmount
        );

        return repository.save(rule);
    }


    public List<ReferralIncentiveRule> listByProvider(UUID providerId) {
            return repository.findByOwnerProviderId(providerId);
    }

    public void deactivate(UUID ruleId, UUID actedByProviderId) {
        ReferralIncentiveRule rule = getOwnedRule(ruleId, actedByProviderId);
        rule.deactivate();
    }

    public void activate(UUID ruleId, UUID actedByProviderId) {
        ReferralIncentiveRule rule = getOwnedRule(ruleId, actedByProviderId);
        rule.activate();
    }

    private ReferralIncentiveRule getOwnedRule(UUID ruleId, UUID providerId) {
        ReferralIncentiveRule rule = findById(ruleId);

        if (!rule.getOwnerProviderId().equals(providerId)) {
            throw new ApiException("You do not own this incentive rule");
        }
        return rule;
    }

    public ReferralIncentiveRule findById(UUID ruleId) {
        return repository.findById(ruleId)
                .orElseThrow(() -> new ApiException("Incentive rule not found"));
    }

    public void ensureProviderExistence(UUID ownerProviderId) {
        providerService.findById(ownerProviderId);
    }
}
