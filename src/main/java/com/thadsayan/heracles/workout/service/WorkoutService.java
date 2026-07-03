package com.thadsayan.heracles.workout.service;

import com.thadsayan.heracles.exercise.repository.ExerciseRepository;
import com.thadsayan.heracles.user.repository.UserRepository;
import com.thadsayan.heracles.user.model.User;
import com.thadsayan.heracles.workout.dto.CreateWorkoutRequest;
import com.thadsayan.heracles.workout.dto.WorkoutDTO;
import com.thadsayan.heracles.workout.model.Workout;
import com.thadsayan.heracles.workout.mapper.WorkoutMapper;
import com.thadsayan.heracles.workout.model.WorkoutExercise;
import com.thadsayan.heracles.workout.repository.WorkoutRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkoutService {
    private final WorkoutRepository workoutRepository;
    private final WorkoutMapper workoutMapper;
    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;


    public Optional<WorkoutDTO> getWorkoutById(UUID id, UUID userId) {
        return workoutRepository.findByIdAndUser_Id(id, userId)
                .map(workoutMapper::toDTO);
    }
    public WorkoutDTO createWorkout(CreateWorkoutRequest createWorkoutRequest, UUID userId) {
        Workout workout = workoutMapper.toEntity(createWorkoutRequest);

        User user = userRepository.getReferenceById(userId);
        workout.setUser(user);

        for(int i = 0; i < workout.getExercises().size(); i++) {
            WorkoutExercise workoutExercise = workout.getExercises().get(i);
            workoutExercise.setExercise(exerciseRepository.findByIdAndUserId(
                    createWorkoutRequest.getExercises().get(i).getExerciseId(),
                    user.getId()
                    ).orElseThrow(() -> new ResourceAccessException("Exercise not found"))
            );
        }

        return workoutMapper.toDTO(workoutRepository.save(workout));
    }
}
