package com.thadsayan.heracles.workout;

import com.thadsayan.heracles.workout.model.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, UUID> {

    @Query("""
        SELECT w
        FROM Workout w
        WHERE w.id = :workoutId
        AND w.user.id = :userId
    """)
    Optional<Workout> findByIdAndUserId (
            @Param("workoutId") UUID workoutId,
            @Param("userId") UUID userId
    );
}
