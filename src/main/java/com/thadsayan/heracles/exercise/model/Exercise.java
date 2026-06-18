package com.thadsayan.heracles.exercise.model;

import com.thadsayan.heracles.exercise.enums.ExerciseType;
import com.thadsayan.heracles.exercise.enums.MuscleGroup;
import com.thadsayan.heracles.user.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import lombok.*;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "exercises")
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "primary_muscle_group", nullable = false)
    private MuscleGroup primaryMuscleGroup;

    @Enumerated(EnumType.STRING)
    @Column(name = "exercise_type", nullable = false)
    private ExerciseType exerciseType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @AssertTrue(message = "User is required unless exercise type is PRESET")
    public boolean isUserValidForExerciseType() {
        return exerciseType == ExerciseType.PRESET || user != null;
    }
}
