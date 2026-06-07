package com.thadsayan.heracles.workout.entity;

import com.thadsayan.heracles.exercise.Exercise;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "workout_exercises")
public class WorkoutExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "workout_id", nullable = false)
    private Workout workout;

    @ManyToOne(optional = false)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    @OneToMany(
            mappedBy = "workoutExercise",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<WorkoutSet> sets = new ArrayList<>();

    private int exerciseNum;
}
