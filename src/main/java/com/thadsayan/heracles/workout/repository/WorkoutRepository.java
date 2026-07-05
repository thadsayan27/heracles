package com.thadsayan.heracles.workout.repository;

import com.thadsayan.heracles.workout.model.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, UUID> {
    Optional<Workout> findByIdAndUser_Id(UUID id, UUID userId);
    List<Workout> findAllByUser_Id(UUID userId);
    List<Workout> findWorkoutsByUser_IdAndPerformedAtBetween(UUID userId, Instant performedAtAfter, Instant performedAtBefore);
}
