package com.thadsayan.heracles.workout.service;

import com.thadsayan.heracles.exercise.service.ExerciseService;
import com.thadsayan.heracles.workout.dto.WorkoutExerciseDTO;
import com.thadsayan.heracles.workout.mapper.WorkoutMapper;
import com.thadsayan.heracles.workout.repository.WorkoutExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkoutExerciseService {

    private final WorkoutExerciseRepository workoutExerciseRepository;
    private final ExerciseService exerciseService;
    private final WorkoutMapper workoutMapper;

    public List<WorkoutExerciseDTO> getWorkoutExercisesByExerciseId(UUID exerciseId, UUID userId) {
        if(!exerciseService.canAccess(exerciseId, userId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return workoutMapper.toWorkoutExerciseDTOList(workoutExerciseRepository.findAllByExerciseIdAndUserId(exerciseId,userId));
    }
}
