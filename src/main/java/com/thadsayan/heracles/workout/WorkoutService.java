package com.thadsayan.heracles.workout;

import com.thadsayan.heracles.exercise.ExerciseRepository;
import com.thadsayan.heracles.security.user.UserPrincipal;
import com.thadsayan.heracles.user.model.User;
import com.thadsayan.heracles.workout.dto.CreateWorkoutRequest;
import com.thadsayan.heracles.workout.dto.WorkoutDTO;
import com.thadsayan.heracles.workout.model.Workout;
import com.thadsayan.heracles.workout.mapper.WorkoutMapper;
import com.thadsayan.heracles.workout.model.WorkoutExercise;
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


    public Optional<WorkoutDTO> getWorkoutById(UUID id, User user) {
        return workoutRepository.findByIdAndUserId(id, user.getId())
                .map(workoutMapper::toDTO);
    }
    public WorkoutDTO createWorkout(CreateWorkoutRequest createWorkoutRequest, UserPrincipal user) {
        Workout workout = workoutMapper.toEntity(createWorkoutRequest);
        workout.setUser(user.getUser());

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
