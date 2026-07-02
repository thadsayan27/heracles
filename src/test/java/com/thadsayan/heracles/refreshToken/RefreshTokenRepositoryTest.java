package com.thadsayan.heracles.refreshToken;

import com.thadsayan.heracles.refreshToken.model.RefreshToken;
import com.thadsayan.heracles.testsupport.ContainersConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

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
        UUID userId = java.util.UUID.randomUUID();
        RefreshToken token1 = refreshTokenService.createNewRefreshToken(userId);
        RefreshToken token2 = refreshTokenService.createNewRefreshToken(userId);

        refreshTokenRepository.revokeByFamilyId(token1.getFamilyId());

        RefreshToken refreshedToken1 = refreshTokenRepository.findByJti(token1.getJti()).orElseThrow();
        RefreshToken refreshedToken2 = refreshTokenRepository.findByJti(token2.getJti()).orElseThrow();

        assert(refreshedToken1.isRevoked());
        assert(refreshedToken2.isRevoked());
    }

    
}
