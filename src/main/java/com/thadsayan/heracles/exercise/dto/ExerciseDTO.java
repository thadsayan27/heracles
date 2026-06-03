package com.thadsayan.heracles.exercise.dto;

import com.thadsayan.heracles.exercise.MuscleGroup;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseDTO {
    private UUID id;
    private String name;
    private MuscleGroup primaryMuscleGroup;
}
