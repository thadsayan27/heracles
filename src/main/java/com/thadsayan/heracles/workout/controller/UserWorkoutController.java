package com.thadsayan.heracles.workout.controller;

import com.thadsayan.heracles.security.user.UserPrincipal;
import com.thadsayan.heracles.shared.exception.BadRequestException;
import com.thadsayan.heracles.workout.dto.WorkoutDTO;
import com.thadsayan.heracles.workout.service.WorkoutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/me/workouts")
@RequiredArgsConstructor
@Tag(name = "User Workouts", description = "Fetch user specific workouts and performed workouts")
@SecurityRequirement(name = "bearerAuth")
public class UserWorkoutController {

    private final WorkoutService workoutService;

    @Operation(
            summary = "Get all performed workouts",
            description = "Returns all the workouts that have been performed by the user"
    )
    @GetMapping("/all")
    public ResponseEntity<List<WorkoutDTO>> getWorkoutByUserId(
            @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.ok(
                workoutService.getAllWorkoutsByUserId(user.getId())
        );
    }

    @Operation(
            summary = "Get workouts between two dates",
            description = "Returns all the workouts that have been performed by the user between two dates"
    )
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
