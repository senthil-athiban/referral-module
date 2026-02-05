package com.indiclinic.referral_system.referral.dto;

public class ReferralStats {

    private long total;
    private long accepted;
    private long rejected;
    private long completed;

    protected ReferralStats() {

    }

    public ReferralStats(
            long total,
            long accepted,
            long rejected,
            long completed
    ) {
        this.total = total;
        this.accepted = accepted;
        this.rejected = rejected;
        this.completed = completed;
    }

    public long getTotal() {
        return total;
    }

    public long getAccepted() {
        return accepted;
    }

    public long getRejected() {
        return rejected;
    }

    public long getCompleted() {
        return completed;
    }
}
