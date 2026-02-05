package com.indiclinic.referral_system.referral.repository;
import com.indiclinic.referral_system.referral.domain.ReceiverType;
import com.indiclinic.referral_system.referral.domain.ReferralContext;
import com.indiclinic.referral_system.referral.dto.ReferralStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.sql.Ref;
import java.util.List;
import java.util.UUID;

public interface ReferralContextRepository extends JpaRepository<ReferralContext, UUID> {
    // internal provider as referrer
    List<ReferralContext> findByReferrerProviderId(UUID providerId);

    // find referrals based on receiver internal provider
    List<ReferralContext> findByReceiverProviderId(UUID id);

    // find referrals based on receiver external provider
    List<ReferralContext> findByReceiverExternalProviderId(UUID id);

    List<ReferralContext> findByPatientId(UUID id);

    @Query("""
        select r from ReferralContext r
        where r.referrerProviderId = :providerId
        or r.receiverProviderId = :providerId
    """)
    List<ReferralContext> findAllByProvider(UUID providerId);

    @Query("""
        select r from ReferralContext r
            where r.referrerProviderId = :providerId
                or r.receiverProviderId = :providerId
                    and r.receiverType = :receiverType
    """)
    List<ReferralContext> findAllByProviderAndReceiverType(
            UUID providerId,
            ReceiverType receiverType
    );

    @Modifying
    @Query("""
        update ReferralContext r
            set r.receiverProviderId = :internalProviderId
                where r.receiverExternalProviderId = :externalProviderId
                    and r.receiverProviderId is null
    """)
    void linkReceiverToInternalProvider(
            UUID externalProviderId,
            UUID internalProviderId
    );

    @Query("""
        SELECT DISTINCT
            CASE
                WHEN r.referrerProviderId = :providerId THEN r.receiverProviderId
                ELSE r.referrerProviderId
            END
        FROM ReferralContext r
        WHERE r.referrerProviderId = :providerId
           OR r.receiverProviderId = :providerId
    """)
    List<UUID> findNetworkProviderIds(UUID providerId);

    @Query("""
        SELECT new com.indiclinic.referral_system.referral.dto.ReferralStats(
            COUNT(r),
            COALESCE(SUM(CASE WHEN r.status = 'ACCEPTED' THEN 1 ELSE 0 END), 0),
            COALESCE(SUM(CASE WHEN r.status = 'REJECTED' THEN 1 ELSE 0 END), 0),
            COALESCE(SUM(CASE WHEN r.status = 'COMPLETED' THEN 1 ELSE 0 END), 0)
        )
        FROM ReferralContext r
        WHERE r.referrerProviderId = :fromProvider
          AND r.receiverProviderId = :toProvider
    """)
    ReferralStats outgoingStats(UUID fromProvider, UUID toProvider);

    @Query("""
        SELECT new com.indiclinic.referral_system.referral.dto.ReferralStats(
            COUNT(r),
            COALESCE(SUM(CASE WHEN r.status = 'ACCEPTED' THEN 1 ELSE 0 END), 0),
            COALESCE(SUM(CASE WHEN r.status = 'REJECTED' THEN 1 ELSE 0 END), 0),
            COALESCE(SUM(CASE WHEN r.status = 'COMPLETED' THEN 1 ELSE 0 END), 0)
        )
        FROM ReferralContext r
        WHERE r.referrerProviderId = :fromProvider
          AND r.receiverProviderId = :toProvider
    """)
    ReferralStats incomingStats(UUID fromProvider, UUID toProvider);
}
