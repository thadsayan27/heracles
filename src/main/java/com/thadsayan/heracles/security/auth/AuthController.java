package com.thadsayan.heracles.security.auth;

import com.thadsayan.heracles.security.dto.*;
import com.thadsayan.heracles.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@SecurityRequirements
@Tag(name = "Authentication", description = "User registration, login and token refresh")
public class AuthController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    @Operation(
            summary = "Login",
            description = "Authenticates a user and returns an access token and refresh token."
    )
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletResponse response) {
        AccessTokens accessTokens = authenticationService.generateTokens(
                loginRequest
        );

        ResponseCookie cookie = ResponseCookie.from("refreshToken", accessTokens.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .path("/api/auth/refresh")
                .maxAge(Duration.ofMillis(accessTokens.getExpiresIn()))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok(new AuthResponse(accessTokens.getAccessToken()));
    }

    @Operation(
            summary = "Registration",
            description = "Registers a new user."
    )
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(
            @Valid @RequestBody SignUpRequest signUpRequest,
            HttpServletResponse response) {
        userService.registerUser(signUpRequest);

        return login(new LoginRequest(signUpRequest.getUsername(), signUpRequest.getPassword()), response);
    }

    @Operation(
            summary = "Refresh Token",
            description = "Refreshes the access token using a valid refresh token."
    )
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(
            @CookieValue("refreshToken") String refreshToken,
            HttpServletResponse response) {
        AccessTokens accessTokens = authenticationService.refreshTokens(
                refreshToken
        );

        ResponseCookie cookie = ResponseCookie.from("refreshToken", accessTokens.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .path("/api/auth/refresh")
                .maxAge(Duration.ofMillis(accessTokens.getExpiresIn()))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok(new AuthResponse(accessTokens.getAccessToken()));
    }
}
