package com.thadsayan.heracles.exercise.dto;

import com.thadsayan.heracles.exercise.MuscleGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseDTO {
    private UUID id;
    private String name;
    private MuscleGroup primaryMuscleGroup;
}
