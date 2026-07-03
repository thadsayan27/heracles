package com.thadsayan.heracles.exercise;

import com.thadsayan.heracles.exercise.enums.ExerciseType;
import com.thadsayan.heracles.exercise.enums.MuscleGroup;
import com.thadsayan.heracles.exercise.model.Exercise;
import com.thadsayan.heracles.exercise.repository.ExerciseRepository;
import com.thadsayan.heracles.testsupport.ContainersConfig;
import com.thadsayan.heracles.user.repository.UserRepository;
import com.thadsayan.heracles.user.enums.Role;
import com.thadsayan.heracles.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;
import java.util.List;
import java.util.UUID;


@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
@Import(ContainersConfig.class)
class ExerciseRepositoryTest {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private User user2;
    private Exercise customExercise;

    @BeforeEach
    void setUp(){
        exerciseRepository.deleteAll();
        userRepository.deleteAll();

        user = User.builder()
                .email("test@test.com")
                .username("user")
                .role(Role.USER)
                .passwordHash("hash")
                .build();

        user2 = User.builder()
                .email("test2@test.com")
                .username("user2")
                .role(Role.USER)
                .passwordHash("hash2")
                .build();
        userRepository.saveAll(List.of(
                user, user2)
        );

        customExercise = Exercise.builder()
                .name("custom")
                .exerciseType(ExerciseType.CUSTOM)
                .primaryMuscleGroup(MuscleGroup.UPPER_BACK)
                .user(user)
                .build();
        exerciseRepository.save(customExercise);
    }

    @Test
    void findByIdAndUserId_whenExerciseIsPreset_shouldReturnExercise() {
        Exercise presetExercise = Exercise.builder()
                .name("preset")
                .exerciseType(ExerciseType.PRESET)
                .primaryMuscleGroup(MuscleGroup.UPPER_BACK)
                .build();

        Exercise savedExercise = exerciseRepository.save(presetExercise);
        UUID savedExerciseUUID = savedExercise.getId();

        Optional<Exercise> result = exerciseRepository.findByIdAndUserId(savedExerciseUUID, user.getId());

        assert(result.isPresent());
        assert(result.get().getId().equals(savedExerciseUUID));

        result = exerciseRepository.findByIdAndUserId(savedExerciseUUID, user2.getId());

        assert(result.isPresent());
        assert(result.get().getId().equals(savedExerciseUUID));

    }

    @Test
    void findByidAndUserId_whenExerciseIsCustomAndUserMatches_shouldReturnExercise() {
        Optional<Exercise> result = exerciseRepository.findByIdAndUserId(customExercise.getId(), user.getId());

        assert(result.isPresent());
        assert(result.get().getId().equals(customExercise.getId()));
    }

    @Test
    void findByidAndUserId_whenExerciseIsCustomAndUserDoesNotMatch_shouldReturnEmpty() {
        Optional<Exercise> result = exerciseRepository.findByIdAndUserId(customExercise.getId(), user2.getId());

        assert(result.isEmpty());
    }
}
