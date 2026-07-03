package com.thadsayan.heracles.workout.service;

import com.thadsayan.heracles.workout.dto.WorkoutExerciseDTO;
import com.thadsayan.heracles.workout.mapper.WorkoutMapper;
import com.thadsayan.heracles.workout.repository.WorkoutExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkoutExerciseService {

    private final WorkoutExerciseRepository workoutExerciseRepository;
    private final WorkoutMapper workoutMapper;

    public List<WorkoutExerciseDTO> getWorkoutExercisesByExerciseId(UUID exerciseId) {
        return workoutMapper.toWorkoutExerciseDTOList(workoutExerciseRepository.findAllByExercise_Id(exerciseId));
    }
}
