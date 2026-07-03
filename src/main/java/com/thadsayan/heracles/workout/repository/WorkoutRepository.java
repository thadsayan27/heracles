package com.thadsayan.heracles.workout.repository;

import com.thadsayan.heracles.workout.model.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, UUID> {
    Optional<Workout> findByIdAndUser_Id(UUID id, UUID userId);
}
