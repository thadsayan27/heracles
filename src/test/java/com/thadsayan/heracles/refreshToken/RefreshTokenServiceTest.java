package com.thadsayan.heracles.refreshToken;

import com.thadsayan.heracles.refreshToken.model.RefreshToken;
import com.thadsayan.heracles.testsupport.ContainersConfig;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
@Import(ContainersConfig.class)
class RefreshTokenServiceTest {

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    void setUp() {
        refreshTokenRepository.deleteAll();
    }

    @Test
    void createNewRefreshToken_whenCalled_shouldPersistTokenForUser() {
        UUID userId = UUID.randomUUID();
        Instant before = Instant.now();

        RefreshToken token = refreshTokenService.createNewRefreshToken(userId);

        Instant after = Instant.now();

        assertNotNull(token.getJti());
        assertNotNull(token.getFamilyId());
        assertEquals(userId, token.getUserId());
        assertFalse(token.isRevoked());
        assertNotNull(token.getCreatedAt());
        assertNotNull(token.getExpiresAt());
        assertFalse(token.getExpiresAt().isBefore(token.getCreatedAt()));
        assertFalse(token.getCreatedAt().isBefore(before));
        assertFalse(token.getCreatedAt().isAfter(after));

        RefreshToken persisted = refreshTokenRepository.findById(token.getJti()).orElseThrow();
        assertEquals(token.getJti(), persisted.getJti());
        assertEquals(token.getFamilyId(), persisted.getFamilyId());
        assertEquals(token.getUserId(), persisted.getUserId());
        assertFalse(persisted.isRevoked());
    }

    @Test
    void refresh_whenCalled_shouldRevokeOldTokenAndIssueNewOne() {
        UUID userId = UUID.randomUUID();
        RefreshToken oldToken = refreshTokenService.createNewRefreshToken(userId);

        RefreshToken newToken = refreshTokenService.refresh(oldToken.getJti(), userId);

        assertNotNull(newToken);
        assertNotEquals(oldToken.getJti(), newToken.getJti());
        assertEquals(oldToken.getFamilyId(), newToken.getFamilyId());
        assertEquals(oldToken.getUserId(), newToken.getUserId());
        assertFalse(newToken.isRevoked());

        RefreshToken persistedOld = refreshTokenRepository.findById(oldToken.getJti()).orElseThrow();
        assertTrue(persistedOld.isRevoked());

        RefreshToken persistedNew = refreshTokenRepository.findById(newToken.getJti()).orElseThrow();
        assertFalse(persistedNew.isRevoked());
    }

    @Test
    void refresh_whenCalled_withRevokedToken_shouldThrow(){
        UUID userId = UUID.randomUUID();
        RefreshToken oldToken = refreshTokenService.createNewRefreshToken(userId);

        refreshTokenService.refresh(oldToken.getJti(), userId);

        assertThrows(JwtException.class, () ->
                refreshTokenService.refresh(oldToken.getJti(), userId));
    }

    @Test
    void refresh_whenCalled_withDifferentUser_shouldThrow(){
        UUID userId = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();
        RefreshToken oldToken = refreshTokenService.createNewRefreshToken(userId);

        assertThrows(JwtException.class, () ->
                refreshTokenService.refresh(oldToken.getJti(), userId2));
    }

    @Test
    void refresh_whenCalled_withExpiredToken_shouldThrow(){
        UUID userId = UUID.randomUUID();
        RefreshToken oldToken = refreshTokenService.createNewRefreshToken(userId);

        oldToken.setExpiresAt(Instant.now().minusSeconds(1));
        refreshTokenRepository.save(oldToken);

        assertThrows(JwtException.class, () ->
                refreshTokenService.refresh(oldToken.getJti(), userId));
    }
}
