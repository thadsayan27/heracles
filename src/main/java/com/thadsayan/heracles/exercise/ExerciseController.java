package com.thadsayan.heracles.exercise;

import com.thadsayan.heracles.exercise.dto.CreateExerciseRequest;
import com.thadsayan.heracles.exercise.dto.ExerciseDTO;
import com.thadsayan.heracles.security.user.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/exercises")
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseService exerciseService;

    @GetMapping("/{id}")
    public ResponseEntity<ExerciseDTO> getExerciseById(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal user) {

        return exerciseService.getExerciseById(id,user.getId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ExerciseDTO> createExercise(
            @Valid @RequestBody CreateExerciseRequest createExerciseRequest,
            @AuthenticationPrincipal UserPrincipal user) {

        ExerciseDTO createdExercise = exerciseService.createExercise(createExerciseRequest, user.getId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdExercise.getId())
                .toUri();

        return ResponseEntity.created(location)
                .body(createdExercise);
    }

    @GetMapping("/custom")
    public ResponseEntity<List<ExerciseDTO>> getAllCustomExercises(
            @AuthenticationPrincipal UserPrincipal user) {
        List<ExerciseDTO> exercises = exerciseService.getAllCustomExercises(user.getUser());
        return ResponseEntity.ok(exercises);
    }

    @GetMapping("/public")
    public ResponseEntity<List<ExerciseDTO>> getAllPublicExercises() {
        List<ExerciseDTO> exercises = exerciseService.getAllPublicExercises();
        return ResponseEntity.ok(exercises);
    }
}
