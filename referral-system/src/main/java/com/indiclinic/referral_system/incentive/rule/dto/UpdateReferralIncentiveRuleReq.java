package com.indiclinic.referral_system.incentive.rule.dto;

import com.indiclinic.referral_system.referral.domain.ReceiverType;

import java.math.BigDecimal;

public class UpdateReferralIncentiveRuleReq {

    public ReceiverType referralTargetType;
    public String name;
    public String description;
    public BigDecimal defaultAmount;
}
