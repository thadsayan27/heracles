package com.thadsayan.heracles.security.auth;

import com.thadsayan.heracles.security.dto.LoginRequest;
import com.thadsayan.heracles.security.dto.AuthResponse;
import com.thadsayan.heracles.security.dto.RefreshRequest;
import com.thadsayan.heracles.security.dto.SignUpRequest;
import com.thadsayan.heracles.user.service.UserService;
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
public class AuthController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResponse authResponse = authenticationService.generateTokens(
                loginRequest
        );
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<HttpStatus> signup(@Valid @RequestBody SignUpRequest signUpRequest) {
        return userService.registerUser(signUpRequest);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshRequest refreshRequest) {
        AuthResponse authResponse = authenticationService.refreshTokens(
                refreshRequest.getRefreshToken()
        );
        return ResponseEntity.ok(authResponse);
    }
}
