package com.indiclinic.referral_system.referral.service;

import com.indiclinic.referral_system.common.ApiException;
import com.indiclinic.referral_system.referral.domain.ReferralAction;
import com.indiclinic.referral_system.referral.domain.ReferralContext;
import com.indiclinic.referral_system.referral.domain.ReferralStatus;
import com.indiclinic.referral_system.referral.repository.ReferralContextRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReferralWorkflowService {
    private final ReferralContextRepository repository;

    public ReferralWorkflowService(ReferralContextRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ReferralContext processWorkflow(UUID referralId, UUID actedProviderId, ReferralAction action) {
        ReferralContext rc = repository.findById(referralId).orElseThrow(() -> new ApiException("Referral not found with given referral ID" + referralId));
        switch (action) {
            case ACCEPT -> rc.accept(actedProviderId);
            case REJECT -> rc.reject(actedProviderId);
            case COMPLETE -> rc.complete(actedProviderId);
        }
        return repository.save(rc);
    }
}
