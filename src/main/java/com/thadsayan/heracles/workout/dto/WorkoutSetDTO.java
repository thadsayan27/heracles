package com.thadsayan.heracles.workout.dto;

import com.thadsayan.heracles.workout.enums.SetType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutSetDTO {
    private UUID id;
    private SetType type;
    private int setNum;
    private int weight;
    private int repetitions;
}
