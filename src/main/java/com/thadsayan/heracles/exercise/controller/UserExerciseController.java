package com.thadsayan.heracles.exercise.controller;

import com.thadsayan.heracles.exercise.dto.ExerciseDTO;
import com.thadsayan.heracles.exercise.service.ExerciseService;
import com.thadsayan.heracles.security.user.UserPrincipal;
import com.thadsayan.heracles.workout.dto.WorkoutExerciseDTO;
import com.thadsayan.heracles.workout.model.WorkoutExercise;
import com.thadsayan.heracles.workout.repository.WorkoutExerciseRepository;
import com.thadsayan.heracles.workout.service.WorkoutExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/me/exercises")
@RequiredArgsConstructor
public class UserExerciseController {

    private final ExerciseService exerciseService;
    private final WorkoutExerciseService workoutExerciseService;

    @GetMapping("/custom")
    public ResponseEntity<List<ExerciseDTO>> getAllCustomExercises(
            @AuthenticationPrincipal UserPrincipal user) {
        List<ExerciseDTO> exercises = exerciseService.getAllCustomExercises(user.getId());
        return ResponseEntity.ok(exercises);
    }

    @GetMapping("/{id}/workout_exercises")
    public ResponseEntity<List<WorkoutExerciseDTO>> getWorkoutExercises(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal user) {

        return ResponseEntity.ok(workoutExerciseService.getWorkoutExercisesByExerciseId(id));
    }
}
