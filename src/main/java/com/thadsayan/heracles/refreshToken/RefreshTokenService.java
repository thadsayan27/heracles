package com.thadsayan.heracles.refreshToken;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${heracles.jwt.refresh-expiration-ms}")
    private Long refreshJwtExpiration;

    public RefreshToken createNewRefreshToken(UUID userId) {
        UUID jti = UUID.randomUUID();
        UUID familyId = UUID.randomUUID();
        Instant now = Instant.now();
        Instant expiresAt = now.plusMillis(refreshJwtExpiration);

        return refreshTokenRepository.saveAndFlush(createRefreshToken(jti, familyId, userId, expiresAt, now));
    }

    @Transactional(noRollbackFor = JwtException.class)
    public RefreshToken refresh(UUID oldJti, UUID userId) {

        int updated = refreshTokenRepository.revokeIfActive(oldJti, userId);

        RefreshToken oldToken = refreshTokenRepository.findByJti(oldJti)
                .orElseThrow(() -> new JwtException("Invalid refresh token"));

        if (!oldToken.getUserId().equals(userId)) {
            throw new JwtException("Invalid refresh token");
        }

        if(updated == 1)
            return issueNewRefreshToken(oldToken);

        if (oldToken.isRevoked()) {
            refreshTokenRepository.revokeByFamilyId(oldToken.getFamilyId());
            throw new JwtException("Invalid refresh token");
        }

        if (!oldToken.getExpiresAt().isAfter(Instant.now())) {
            log.warn("Expired refresh token used");
            throw new JwtException("Invalid refresh token");
        }

        throw new JwtException("Invalid refresh token");
    }

    private RefreshToken issueNewRefreshToken(RefreshToken oldToken) {
        UUID newJti = UUID.randomUUID();
        Instant now = Instant.now();
        RefreshToken newToken = createRefreshToken(
                newJti,
                oldToken.getFamilyId(),
                oldToken.getUserId(),
                now.plusMillis(refreshJwtExpiration),
                now
        );

        oldToken.setReplacedByJti(newJti);
        refreshTokenRepository.save(oldToken);
        return refreshTokenRepository.saveAndFlush(newToken);
    }

    private RefreshToken createRefreshToken(UUID jti, UUID familyId, UUID userId, Instant expiresAt, Instant createdAt) {
        return RefreshToken.builder()
                .jti(jti)
                .familyId(familyId)
                .userId(userId)
                .expiresAt(expiresAt)
                .createdAt(createdAt)
                .build();
    }

}
