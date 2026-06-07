package com.thadsayan.heracles.workout;

import com.thadsayan.heracles.workout.dto.CreateWorkoutRequest;
import com.thadsayan.heracles.workout.dto.WorkoutDTO;
import com.thadsayan.heracles.workout.entity.Workout;
import com.thadsayan.heracles.workout.mapper.WorkoutMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkoutService {
    private final WorkoutRepository workoutRepository;
    private final WorkoutMapper workoutMapper;

    public WorkoutDTO createWorkout(CreateWorkoutRequest createWorkoutRequest) {
        Workout workout = workoutMapper.toEntity(createWorkoutRequest);

        return new WorkoutDTO();
    }
}
