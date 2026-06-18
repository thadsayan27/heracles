package com.thadsayan.heracles.workout.dto;

import com.thadsayan.heracles.exercise.dto.ExerciseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutExerciseDTO {
    private UUID id;
    private ExerciseDTO exercise;
    private List<WorkoutSetDTO> sets;
    private int exerciseNum;
}
