package com.thadsayan.heracles.exercise.controller;

import com.thadsayan.heracles.exercise.dto.ExerciseDTO;
import com.thadsayan.heracles.exercise.service.ExerciseService;
import com.thadsayan.heracles.security.user.UserPrincipal;
import com.thadsayan.heracles.workout.dto.WorkoutExerciseDTO;
import com.thadsayan.heracles.workout.service.WorkoutExerciseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/me/exercises")
@RequiredArgsConstructor
@Tag(name = "User Exercises", description = "Fetch user specific exercises and performed exercises")
@SecurityRequirement(name = "bearerAuth")
public class UserExerciseController {

    private final ExerciseService exerciseService;
    private final WorkoutExerciseService workoutExerciseService;

    @Operation(
            summary = "Get all custom exercises",
            description = "Fetch all exercises created by the User"
    )
    @GetMapping("/custom")
    public ResponseEntity<List<ExerciseDTO>> getAllCustomExercises(
            @AuthenticationPrincipal UserPrincipal user) {
        List<ExerciseDTO> exercises = exerciseService.getAllCustomExercises(user.getId());
        return ResponseEntity.ok(exercises);
    }

    @Operation(
            summary = "Get performed exercise",
            description = "Fetch all the performed exercises for a given exercise"
    )
    @GetMapping("/{id}/workout_exercises")
    public ResponseEntity<List<WorkoutExerciseDTO>> getWorkoutExercises(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal user) {

        return ResponseEntity.ok(
                workoutExerciseService.getWorkoutExercisesByExerciseId(id,user.getId())
        );
    }

    @Operation(
            summary = "Get exercise for a time period",
            description = "Fetch all Exercises that have been performed after the given date"
    )
    @GetMapping("/workout_exercises/performed")
    public ResponseEntity<List<WorkoutExerciseDTO>> getWorkoutExercisesPerformedAfter(
            @RequestParam Instant after,
            @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(
                workoutExerciseService.findExercisesPerformedAfter(user.getId(),after)
        );
    }

    @Operation(
            summary = "Get top exercises",
            description = "Fetch Exercises that have been performed the most by the user"
    )
    @GetMapping("/top")
    public ResponseEntity<List<WorkoutExerciseDTO>> getTopExercises(
            @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(
                workoutExerciseService.findTopExercises(user.getId())
        );
    }
}
