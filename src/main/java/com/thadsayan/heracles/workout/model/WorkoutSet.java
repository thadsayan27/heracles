package com.thadsayan.heracles.workout.model;

import com.thadsayan.heracles.workout.enums.SetType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "workout_sets")
public class WorkoutSet {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workout_exercise_id", nullable = false)
    private WorkoutExercise workoutExercise;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SetType type;

    private int setNum;

    private int weight;

    private int repetitions;
}
