package com.thadsayan.heracles.workout;

import com.thadsayan.heracles.workout.dto.CreateWorkoutRequest;
import com.thadsayan.heracles.workout.dto.WorkoutDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/workouts")
@RequiredArgsConstructor
public class WorkoutController {
    private final WorkoutService workoutService;

    @PostMapping
    public ResponseEntity<WorkoutDTO> createWorkout(
            @Valid @RequestBody CreateWorkoutRequest createWorkoutRequest) {

        WorkoutDTO createdWorkout = workoutService.createWorkout(createWorkoutRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdWorkout)
                .toUri();

        return ResponseEntity.created(location)
                .body(createdWorkout);
    }
}
