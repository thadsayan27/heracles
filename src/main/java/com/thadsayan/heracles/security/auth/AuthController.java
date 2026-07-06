package com.thadsayan.heracles.security.auth;

import com.thadsayan.heracles.security.dto.LoginRequest;
import com.thadsayan.heracles.security.dto.AuthResponse;
import com.thadsayan.heracles.security.dto.RefreshRequest;
import com.thadsayan.heracles.security.dto.SignUpRequest;
import com.thadsayan.heracles.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@SecurityRequirements
@Tag(name = "Authentication", description = " User registration, login and token refresh")
public class AuthController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    @Operation(
            summary = "Login",
            description = "Authenticates a user and returns an access token and refresh token."
    )
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResponse authResponse = authenticationService.generateTokens(
                loginRequest
        );
        return ResponseEntity.ok(authResponse);
    }

    @Operation(
            summary = "Registration",
            description = "Registers a new user."
    )
    @PostMapping("/signup")
    public ResponseEntity<HttpStatus> signup(@Valid @RequestBody SignUpRequest signUpRequest) {
        return userService.registerUser(signUpRequest);
    }

    @Operation(
            summary = "Refresh Token",
            description = "Refreshes the access token using a valid refresh token."
    )
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshRequest refreshRequest) {
        AuthResponse authResponse = authenticationService.refreshTokens(
                refreshRequest.getRefreshToken()
        );
        return ResponseEntity.ok(authResponse);
    }
}
