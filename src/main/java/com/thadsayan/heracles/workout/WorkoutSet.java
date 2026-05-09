package com.thadsayan.heracles.workout;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "workout_sets")
public class WorkoutSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workout_set_id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "workout_exercise_id", nullable = false)
    private WorkoutExercise workoutExercise;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SetType type;

    private int set;

    private int weight;

    private int repetitions;
}
