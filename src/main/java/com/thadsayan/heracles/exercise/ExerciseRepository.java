package com.thadsayan.heracles.exercise;

import com.thadsayan.heracles.exercise.enums.MuscleGroup;
import com.thadsayan.heracles.exercise.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, UUID> {
    List<Exercise> findByName(String name);
    List<Exercise> findByPrimaryMuscleGroup(MuscleGroup primaryMuscleGroup);
    boolean existsByNameIgnoreCase(String name);

    @Query("""
        SELECT e
        FROM Exercise e
        WHERE e.id = :exerciseId
          AND (
              e.exerciseType = PRESET
              OR e.user.id = :userId
          )
    """)
    Optional<Exercise> findByIdAndUserId (
            @Param("exerciseId") UUID exerciseId,
            @Param("userId") UUID userId
    );
}
