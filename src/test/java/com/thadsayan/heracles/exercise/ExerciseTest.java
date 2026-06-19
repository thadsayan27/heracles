package com.thadsayan.heracles.exercise;

import com.thadsayan.heracles.exercise.enums.ExerciseType;
import com.thadsayan.heracles.exercise.model.Exercise;
import com.thadsayan.heracles.user.enums.Role;
import com.thadsayan.heracles.user.enums.Sex;
import com.thadsayan.heracles.user.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExerciseTest {

    @Test
    void isUserValid_whenPresetAndUserIsNull_shouldReturnTrue() {
        Exercise e = Exercise.builder()
                .exerciseType(ExerciseType.PRESET)
                .user(null)
                .build();

        assertTrue(e.isUserValidForExerciseType());
    }

    @Test
    void isUserValid_whenCustomAndUserPresent_shouldReturnTrue() {
        User u = User.builder()
                .email("user@test.com")
                .username("user")
                .passwordHash("hash")
                .sex(Sex.male)
                .role(Role.USER)
                .build();

        Exercise e = Exercise.builder()
                .exerciseType(ExerciseType.CUSTOM)
                .user(u)
                .build();

        assertTrue(e.isUserValidForExerciseType());
    }

    @Test
    void isUserValid_whenCustomAndUserIsNull_shouldReturnFalse() {
        Exercise e = Exercise.builder()
                .exerciseType(ExerciseType.CUSTOM)
                .user(null)
                .build();

        assertFalse(e.isUserValidForExerciseType());
    }
}

