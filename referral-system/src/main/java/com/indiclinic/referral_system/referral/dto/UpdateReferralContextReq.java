package com.indiclinic.referral_system.referral.dto;

import com.indiclinic.referral_system.referral.domain.ReceiverType;
import com.indiclinic.referral_system.referral.domain.ReferralPriority;

import java.util.UUID;

public class UpdateReferralContextReq {

    /* Clinical details */
    public String reasonConceptCode;
    public String clinicalSummary;
    public String clinicalNotes;

    /* Receiver */
    public ReceiverType receiverType;
    public UUID receiverProviderId;
    public UUID receiverExternalProviderId;
    public UUID receiverLocationId;

    /* Incentive */
    public UUID appliedIncentiveRuleId;

    /* meta */
    public ReferralPriority priority;
}
