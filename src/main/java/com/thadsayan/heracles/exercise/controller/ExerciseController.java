package com.thadsayan.heracles.exercise.controller;

import com.thadsayan.heracles.exercise.service.ExerciseService;
import com.thadsayan.heracles.exercise.dto.CreateExerciseRequest;
import com.thadsayan.heracles.exercise.dto.ExerciseDTO;
import com.thadsayan.heracles.security.user.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Exercises", description = "Create and fetch exercises")
@SecurityRequirement(name = "bearerAuth")
public class ExerciseController {

    private final ExerciseService exerciseService;

    @Operation(
            summary = "Get exercise by ID",
            description = "Returns a single exercise"
    )
    @GetMapping("/{id}")
    public ResponseEntity<ExerciseDTO> getExerciseById(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal user) {

        return ResponseEntity.ok(exerciseService.getExerciseById(id,user.getId()));
    }

    @Operation(
            summary = "Create exercise",
            description = "Create a new Exercise"
    )
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

    @Operation(
            summary = "Get public exercises",
            description = "Returns a List of all default Exercises"
    )
    @GetMapping("/public")
    public ResponseEntity<List<ExerciseDTO>> getAllPublicExercises() {
        List<ExerciseDTO> exercises = exerciseService.getAllPublicExercises();
        return ResponseEntity.ok(exercises);
    }
}
