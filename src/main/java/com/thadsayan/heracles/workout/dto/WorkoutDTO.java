package com.thadsayan.heracles.workout.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutDTO {
    private UUID id;
    private String name;
    private int workoutNum;
    private List<WorkoutExerciseDTO> exercises;
}
