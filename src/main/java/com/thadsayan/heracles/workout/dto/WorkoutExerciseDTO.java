package com.thadsayan.heracles.workout.dto;

import com.thadsayan.heracles.exercise.model.Exercise;
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
    private Exercise exercise;
    private List<WorkoutSetDTO> sets;
    private int exerciseNum;
}
