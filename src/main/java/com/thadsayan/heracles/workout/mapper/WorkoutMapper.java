package com.thadsayan.heracles.workout.mapper;


import com.thadsayan.heracles.workout.model.Workout;
import com.thadsayan.heracles.workout.model.WorkoutExercise;
import com.thadsayan.heracles.workout.model.WorkoutSet;
import com.thadsayan.heracles.workout.dto.*;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import java.util.List;

@Mapper(componentModel = "spring")
public interface WorkoutMapper {

    WorkoutDTO toDTO(Workout workout);
    WorkoutSetDTO toDTO(WorkoutSet workoutSet);
    WorkoutExerciseDTO toDTO(WorkoutExercise workoutExercise);

    Workout toEntity(WorkoutDTO workoutDTO);
    WorkoutSet toEntity(WorkoutSetDTO workoutSetDTO);
    WorkoutExercise toEntity(WorkoutExerciseDTO workoutExerciseDTO);

    Workout toEntity(CreateWorkoutRequest createWorkoutRequest);
    WorkoutSet toEntity(CreateWorkoutSetRequest workoutSetRequest);
    WorkoutExercise toEntity(CreateWorkoutExerciseRequest workoutExerciseRequest);

    List<WorkoutDTO> toWorkoutDTOList(List<Workout> workouts);
    List<WorkoutSetDTO> toWorkoutSetDTOList(List<WorkoutSet> workoutSets);
    List<WorkoutExerciseDTO> toWorkoutExerciseDTOList(List<WorkoutExercise> workoutExercises);

    List<Workout> toWorkoutEntityList(List<CreateWorkoutRequest> createWorkoutRequests);
    List<WorkoutSet> toWorkoutSetEntityList(List<CreateWorkoutSetRequest> createWorkoutSetRequests);
    List<WorkoutExercise> toWorkoutExerciseEntityList(List<CreateWorkoutExerciseRequest> createWorkoutExerciseRequests);

    @AfterMapping
    default void linkParents(@MappingTarget Workout workout) {
        if (workout.getExercises() == null)
            return;

        for (WorkoutExercise workoutExercise : workout.getExercises()) {
            workoutExercise.setWorkout(workout);

            if (workoutExercise.getSets() == null)
                continue;

            for (WorkoutSet workoutSet : workoutExercise.getSets())
                workoutSet.setWorkoutExercise(workoutExercise);
        }
    }
}
