package com.thadsayan.heracles.user.service;

import com.thadsayan.heracles.security.dto.SignUpRequest;
import com.thadsayan.heracles.user.enums.Role;
import com.thadsayan.heracles.user.model.User;
import com.thadsayan.heracles.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<HttpStatus> registerUser(SignUpRequest signUpRequest) {
        boolean usernameTaken = userRepository.existsByUsername(signUpRequest.getUsername());
        boolean emailTaken = userRepository.existsByEmail(signUpRequest.getEmail());

        if(usernameTaken && emailTaken)
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username and Email taken");
        if (emailTaken)
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already in use");
        if(usernameTaken)
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username is already in use");

        User user = User.builder()
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .passwordHash(passwordEncoder.encode(signUpRequest.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
