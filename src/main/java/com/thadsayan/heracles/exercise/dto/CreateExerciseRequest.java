package com.thadsayan.heracles.exercise.dto;

import com.thadsayan.heracles.exercise.enums.MuscleGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateExerciseRequest {

    @NotBlank(message = "Exercise must have a name")
    @Size(max = 255, message = "Exercise name must be less than 255 characters")
    private String name;

    @NotNull(message = "Exercise must be associated with a muscle group")
    private MuscleGroup primaryMuscleGroup;
}
