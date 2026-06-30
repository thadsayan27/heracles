package com.thadsayan.heracles.refreshToken;

import com.thadsayan.heracles.refreshToken.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
    UPDATE RefreshToken rt
    SET rt.revoked = true,
        rt.revokedAt = CURRENT_TIMESTAMP
    WHERE rt.jti = :jti
    AND rt.userId = :userId
    AND rt.revoked = false
    AND rt.expiresAt > CURRENT_TIMESTAMP
""")
    int revokeIfActive(
            @Param("jti") UUID jti,
            @Param("userId") UUID userId
    );

    Optional<RefreshToken> findByJti(UUID jti);

    @Transactional
    @Modifying
    @Query("""
        UPDATE RefreshToken rt
        SET rt.revoked = true,
            rt.revokedAt = CURRENT_TIMESTAMP
        WHERE rt.familyId = :familyId
          AND rt.revoked = false
    """)
    void revokeByFamilyId(UUID familyId);
}
