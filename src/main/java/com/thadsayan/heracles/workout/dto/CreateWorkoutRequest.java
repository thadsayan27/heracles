package com.thadsayan.heracles.workout.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateWorkoutRequest {
    @NotBlank(message = "Workout must have a name")
    @Size(min = 1, max = 255, message = "Workout name must be between 1 and 255 characters")
    private String name;
    @NotNull(message = "Workout must have a workout number")
    @Min(value = 0, message = "Workout number must be at least 1")
    private int workoutNum;
    @NotNull(message = "Workout must have exercises")
    @Size(min = 1, message = "Workout must have at least one exercise")
    @Valid
    private List<CreateWorkoutExerciseRequest> exercises;
}
