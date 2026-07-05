package com.thadsayan.heracles.workout.repository;

import com.thadsayan.heracles.workout.model.WorkoutExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExercise, UUID>  {

    @Query("""
    SELECT we
    FROM WorkoutExercise we
    JOIN we.workout w
    WHERE we.exercise.id = :exerciseId
      AND w.user.id = :userId
    ORDER BY w.workoutNum, we.exerciseNum
    """)
    List<WorkoutExercise> findAllByExerciseIdAndUserId(
            @Param("exerciseId") UUID exerciseId,
            @Param("userId") UUID userId
    );

    @Query("""
    SELECT we.exercise, COUNT(we.exercise.id)
    FROM WorkoutExercise we
    WHERE we.workout.user.id = :userId
    GROUP BY we.exercise
    ORDER BY COUNT(we.exercise.id) DESC
    """)
    List<WorkoutExercise> findTopExercisesByUser_Id(UUID userId);

    List<WorkoutExercise> findWorkoutExercisesByWorkout_IdAndWorkout_PerformedAtAfter(UUID workoutId, Instant performedAtAfter);
}
