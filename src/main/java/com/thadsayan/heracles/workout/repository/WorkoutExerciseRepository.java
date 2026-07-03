package com.thadsayan.heracles.workout.repository;

import com.thadsayan.heracles.workout.model.WorkoutExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExercise, UUID>  {
    List<WorkoutExercise> findAllByExercise_Id(UUID exerciseId);
}
