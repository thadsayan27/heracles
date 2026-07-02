package com.thadsayan.heracles.refreshToken;

import com.thadsayan.heracles.refreshToken.model.RefreshToken;
import com.thadsayan.heracles.testsupport.ContainersConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
@Import(ContainersConfig.class)
class RefreshTokenRepositoryTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Test
    void revokeByFamilyId_whenCalled_shouldRevokeAllTokensWithFamilyId() {
        UUID familyId = UUID.randomUUID();
        UUID userId = java.util.UUID.randomUUID();

        RefreshToken token1 = RefreshToken.builder()
                .jti(UUID.randomUUID())
                .familyId(familyId)
                .userId(userId)
                .expiresAt(Instant.now().plusSeconds(3600))
                .createdAt(Instant.now())
                .build();

        RefreshToken token2 = RefreshToken.builder()
                .jti(UUID.randomUUID())
                .familyId(familyId)
                .userId(userId)
                .expiresAt(Instant.now().plusSeconds(3600))
                .createdAt(Instant.now())
                .build();

        refreshTokenRepository.saveAllAndFlush(
                java.util.List.of(token1, token2)
        );

        refreshTokenRepository.revokeByFamilyId(token1.getFamilyId());

        RefreshToken refreshedToken1 = refreshTokenRepository.findByJti(token1.getJti()).orElseThrow();
        RefreshToken refreshedToken2 = refreshTokenRepository.findByJti(token2.getJti()).orElseThrow();

        assertTrue(refreshedToken1.isRevoked());
        assertTrue(refreshedToken2.isRevoked());
    }


}
