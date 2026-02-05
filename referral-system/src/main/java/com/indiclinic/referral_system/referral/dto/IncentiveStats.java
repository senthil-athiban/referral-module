package com.indiclinic.referral_system.referral.dto;

import java.math.BigDecimal;

public record IncentiveStats(
        BigDecimal totalAmount,
        BigDecimal paidAmount,
        BigDecimal pendingAmount
) {}
