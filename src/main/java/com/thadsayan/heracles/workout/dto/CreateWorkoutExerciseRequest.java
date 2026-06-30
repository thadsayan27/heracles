package com.thadsayan.heracles.workout.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateWorkoutExerciseRequest {
    @NotNull(message = "Workout exercise must be associated with an exercise")
    private UUID exerciseId;
    @NotNull(message = "Workout exercise must have at least one set")
    @Size(min = 1, message = "Workout exercise must have at least one set")
    @Valid
    private List<CreateWorkoutSetRequest> sets;
    @Min(value = 0, message = "Exercise number must be at least 0")
    private int exerciseNum;
}
