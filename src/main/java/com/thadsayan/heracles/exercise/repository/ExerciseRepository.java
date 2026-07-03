package com.thadsayan.heracles.exercise.repository;

import com.thadsayan.heracles.exercise.enums.ExerciseType;
import com.thadsayan.heracles.exercise.model.Exercise;
import com.thadsayan.heracles.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, UUID> {
    boolean existsByNameIgnoreCaseAndUserId(String name, UUID userId);
    @Query("""
        SELECT e
        FROM Exercise e
        WHERE e.id = :exerciseId
          AND (
              e.exerciseType = PRESET
              OR e.user.id = :userId
          )
    """)
    Optional<Exercise> findByIdAndUserId(
            @Param("exerciseId") UUID exerciseId,
            @Param("userId") UUID userId
    );

    List<Exercise> findAllByUser_Id(UUID userId);

    List<Exercise> findAllByExerciseType(ExerciseType exerciseType);

    UUID user(User user);
}
