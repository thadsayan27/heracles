package com.thadsayan.heracles.workout.dto;

import com.thadsayan.heracles.workout.model.SetType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateWorkoutSetRequest {
    @NotNull(message = "Set set must have a type")
    private SetType type;
    @NotNull(message = "Set must be associated with a Number")
    @Min(value = 0, message = "Set Number must be larger than 0")
    private int setNum;
    @NotNull(message= "Set must be associated with a weight")
    @Min(value = 0, message = "Set weight must be larger than or equal to 0")
    private int weight;
    @NotNull(message = "Set must be associated with a number of repetitions")
    @Min(value = 0, message = "Set repetitions must be larger than or equal to 0")
    private int repetitions;
}
