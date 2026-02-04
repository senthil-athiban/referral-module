package com.indiclinic.referral_system.incentive.record;

import com.indiclinic.referral_system.common.ApiException;
import com.indiclinic.referral_system.incentive.rule.ReferralIncentiveRule;
import com.indiclinic.referral_system.incentive.rule.ReferralIncentiveRuleRepository;
import com.indiclinic.referral_system.referral.domain.ReferralContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ReferralIncentiveService {
    private final ReferralIncentiveRepository incentiveRepository;
    private final ReferralIncentiveRuleRepository ruleRepository;

    public ReferralIncentiveService(
            ReferralIncentiveRepository incentiveRepository,
            ReferralIncentiveRuleRepository ruleRepository
    ) {
        this.incentiveRepository = incentiveRepository  ;
        this.ruleRepository = ruleRepository;
    }

    public void createForAcceptedReferral(
            ReferralContext referral
    ) {
        if (referral.getAppliedIncentiveRuleId() == null) {
            return;
        }

        if (incentiveRepository.findByReferralId(referral.getId()).isPresent()) {
            return;
        }

        ReferralIncentiveRule rule = ruleRepository.findById(referral.getAppliedIncentiveRuleId()).orElseThrow(
                () -> new ApiException("Applied incentive rule not found")
        );

        ReferralIncentive incentive =
                ReferralIncentive.create(referral, rule);

        incentiveRepository.save(incentive);
    }

    public List<ReferralIncentive> incentivesEarnedBy(UUID providerId) {
        return incentiveRepository.findByBeneficiaryProviderId(providerId);
    }

}
