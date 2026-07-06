package com.thadsayan.heracles.workout.controller;

import com.thadsayan.heracles.security.user.UserPrincipal;
import com.thadsayan.heracles.workout.service.WorkoutService;
import com.thadsayan.heracles.workout.dto.CreateWorkoutRequest;
import com.thadsayan.heracles.workout.dto.WorkoutDTO;
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
import java.util.UUID;

@RestController
@RequestMapping("/api/workouts")
@RequiredArgsConstructor
@Tag(name = "Workouts", description = "Create and fetch workouts")
@SecurityRequirement(name = "bearerAuth")
public class WorkoutController {
    private final WorkoutService workoutService;


    @Operation(
            summary = "Get workout by ID",
            description = "Returns a single workout owned by the user"
    )
    @GetMapping("/{id}")
    public ResponseEntity<WorkoutDTO> getWorkoutById(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal user) {

        return ResponseEntity.ok(workoutService.getWorkoutById(id,user.getId()));

    }

    @Operation(
            summary = "Create workout",
            description = "Create a workout with exercises and sets"
    )
    @PostMapping
    public ResponseEntity<WorkoutDTO> createWorkout(
            @Valid @RequestBody CreateWorkoutRequest createWorkoutRequest,
            @AuthenticationPrincipal UserPrincipal user) {

        WorkoutDTO createdWorkout = workoutService.createWorkout(createWorkoutRequest, user.getId());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdWorkout.getId())
                .toUri();

        return ResponseEntity.created(location)
                .body(createdWorkout);
    }
}
