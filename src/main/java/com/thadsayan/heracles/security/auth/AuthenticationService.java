package com.thadsayan.heracles.security.auth;

import com.thadsayan.heracles.refreshToken.model.RefreshToken;
import com.thadsayan.heracles.refreshToken.RefreshTokenService;
import com.thadsayan.heracles.security.user.UserPrincipal;
import com.thadsayan.heracles.security.dto.AuthResponse;
import com.thadsayan.heracles.security.dto.LoginRequest;
import com.thadsayan.heracles.security.user.UserPrincipalService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final RefreshTokenService refreshTokenService;

    @Value("${heracles.jwt.secret}")
    private String secretKey;

    @Value("${heracles.jwt.refresh-secret}")
    private String refreshSecretKey;

    @Value("${heracles.jwt.expiration-ms}")
    private Long accessJwtExpiration;

    @Value("${heracles.jwt.refresh-expiration-ms}")
    private Long refreshJwtExpiration;

    private SecretKey accessSigningKey;
    private SecretKey refreshSigningKey;

    @PostConstruct
    void init() {
        this.accessSigningKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.refreshSigningKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshSecretKey));
    }

    public UserDetails authenticate(String username, String password) {
        Authentication authentication = authenticationManager.authenticate (
                new UsernamePasswordAuthenticationToken(username, password)
        );

        return (UserDetails) authentication.getPrincipal();
    }

    public AuthResponse generateTokens(LoginRequest loginRequest){
        UserPrincipal user = (UserPrincipal) authenticate(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        );

        String accessToken = generateAccessToken(user.getId());
        String refreshToken = generateRefreshToken(
                refreshTokenService.createNewRefreshToken(user.getId())
        );

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(accessJwtExpiration)
                .refreshExpiresIn(refreshJwtExpiration)
                .build();
    }

    public AuthResponse refreshTokens(String token) {
        Claims claims = extractClaims(token, refreshSigningKey);

        if(!"refresh".equals(claims.get("type", String.class)))
            throw new JwtException("Invalid token type");

        if (claims.getId() == null)
            throw new JwtException("Invalid refresh token");

        UserPrincipal user = (UserPrincipal) ((UserPrincipalService) userDetailsService).loadUserById(
                UUID.fromString(claims.getSubject())
        );

        RefreshToken refreshToken = refreshTokenService.refresh(
                UUID.fromString(claims.getId()),
                user.getId()
        );

        String newAccessToken = generateAccessToken(user.getId());
        String newRefreshToken = generateRefreshToken(refreshToken);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .expiresIn(accessJwtExpiration)
                .refreshExpiresIn(refreshJwtExpiration)
                .build();
    }

    public UserDetails validateAccessToken(String token) {
        Claims claims = extractClaims(token, accessSigningKey);

        if (!"access".equals(claims.get("type", String.class)))
            throw new JwtException("Invalid token type");

        return ((UserPrincipalService) userDetailsService).loadUserById(
                UUID.fromString(claims.getSubject())
        );
    }

    private String generateAccessToken(UUID userId) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("type", "access")
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(accessJwtExpiration)))
                .signWith(accessSigningKey)
                .compact();
    }

    private String generateRefreshToken(RefreshToken token){
        return Jwts.builder()
                .subject(String.valueOf(token.getUserId()))
                .id(String.valueOf(token.getJti()))
                .claim("type", "refresh")
                .claim("familyId", String.valueOf(token.getFamilyId()))
                .issuedAt(Date.from(token.getCreatedAt()))
                .expiration(Date.from(token.getExpiresAt()))
                .signWith(refreshSigningKey)
                .compact();
    }

    private Claims extractClaims(String token, SecretKey key) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception ex) {
            throw new JwtException("Invalid token", ex);
        }
    }
}
