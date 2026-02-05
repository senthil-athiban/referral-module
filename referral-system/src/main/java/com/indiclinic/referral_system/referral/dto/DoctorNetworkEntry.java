package com.indiclinic.referral_system.referral.dto;

import java.util.UUID;

public record DoctorNetworkEntry(
        UUID providerId,
        String providerName,
        ReferralStats outgoing,
        ReferralStats incoming,
        IncentiveStats incentive
) {}
