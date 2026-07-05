package com.thadsayan.heracles.workout.controller;

import com.thadsayan.heracles.security.user.UserPrincipal;
import com.thadsayan.heracles.shared.exception.BadRequestException;
import com.thadsayan.heracles.workout.dto.WorkoutDTO;
import com.thadsayan.heracles.workout.service.WorkoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/me/workouts")
@RequiredArgsConstructor
public class UserWorkoutController {

    private final WorkoutService workoutService;

    @GetMapping("/all")
    public ResponseEntity<List<WorkoutDTO>> getWorkoutExercisesByUserId(
            @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(
                workoutService.getAllWorkoutsByUserId(user.getId())
        );
    }

    @GetMapping("/performed")
    public ResponseEntity<List<WorkoutDTO>> getWorkoutsBetween(
            @RequestParam Instant from,
            @RequestParam Instant to,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        if (!from.isBefore(to))
            throw new BadRequestException(
                    "Invalid time range: 'from' must be before 'to'"
            );

        return ResponseEntity.ok(
                workoutService.getWorkoutsByPerformedAtBetween(
                        user.getId(),
                        from,
                        to
                )
        );
    }
}
