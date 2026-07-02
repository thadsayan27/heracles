package com.thadsayan.heracles.exercise;

import com.thadsayan.heracles.exercise.dto.CreateExerciseRequest;
import com.thadsayan.heracles.exercise.dto.ExerciseDTO;
import com.thadsayan.heracles.exercise.enums.ExerciseType;
import com.thadsayan.heracles.exercise.enums.MuscleGroup;
import com.thadsayan.heracles.exercise.model.Exercise;
import com.thadsayan.heracles.testsupport.ContainersConfig;
import com.thadsayan.heracles.user.UserRepository;
import com.thadsayan.heracles.user.enums.Role;
import com.thadsayan.heracles.user.model.User;
import org.apache.coyote.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
@Import(ContainersConfig.class)
class ExerciseServiceTest {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    @Autowired
    private ExerciseService exerciseService;

    @BeforeEach
    void setUp() {
        exerciseRepository.deleteAll();
        userRepository.deleteAll();

        user = User.builder()
                .email("test@test.com")
                .username("user")
                .role(Role.USER)
                .passwordHash("hash")
                .build();

        userRepository.save(user);
    }

    @Test
    void createExercise_whenCalled_shouldPersistExerciseForUser() {
        CreateExerciseRequest createExerciseRequest = CreateExerciseRequest.builder()
                .name("Push Up")
                .primaryMuscleGroup(MuscleGroup.CHEST)
                .build();

        ExerciseDTO exerciseDTO = exerciseService.createExercise(createExerciseRequest,user);

        assert(exerciseDTO.getId() != null);
        assert(exerciseDTO.getName().equals("Push Up"));
        assert(exerciseDTO.getPrimaryMuscleGroup().equals(MuscleGroup.CHEST));

        Optional<Exercise> exercise = exerciseRepository.findByIdAndUserId(exerciseDTO.getId(), user.getId());

        assert(exercise.isPresent());
        assert(exercise.get().getName().equals("Push Up"));
        assert(exercise.get().getPrimaryMuscleGroup().equals(MuscleGroup.CHEST));
        assert(exercise.get().getUser().getId().equals(user.getId()));
        assert(exercise.get().getExerciseType().equals(ExerciseType.CUSTOM));

    }

    @Test
    void createExercise_whenCalled_withExistingNameForUser_shouldFail() {
        CreateExerciseRequest createExerciseRequest = CreateExerciseRequest.builder()
                .name("Push Up")
                .primaryMuscleGroup(MuscleGroup.CHEST)
                .build();

        exerciseService.createExercise(createExerciseRequest,user);

        CreateExerciseRequest sameName = CreateExerciseRequest.builder()
                .name("Push Up")
                .primaryMuscleGroup(MuscleGroup.CHEST)
                .build();

        assertThrows(ResponseStatusException.class, () -> {
            exerciseService.createExercise(sameName,user);
        });
    }
}
