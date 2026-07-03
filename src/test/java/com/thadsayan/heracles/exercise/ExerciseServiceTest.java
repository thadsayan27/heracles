package com.thadsayan.heracles.exercise;

import com.thadsayan.heracles.exercise.dto.CreateExerciseRequest;
import com.thadsayan.heracles.exercise.dto.ExerciseDTO;
import com.thadsayan.heracles.exercise.enums.ExerciseType;
import com.thadsayan.heracles.exercise.enums.MuscleGroup;
import com.thadsayan.heracles.exercise.model.Exercise;
import com.thadsayan.heracles.exercise.repository.ExerciseRepository;
import com.thadsayan.heracles.exercise.service.ExerciseService;
import com.thadsayan.heracles.shared.exception.ConflictException;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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

        ExerciseDTO exerciseDTO = exerciseService.createExercise(createExerciseRequest, user.getId());

        assertNotNull(exerciseDTO.getId());
        assertEquals("Push Up", exerciseDTO.getName());
        assertEquals(MuscleGroup.CHEST, exerciseDTO.getPrimaryMuscleGroup());

        Optional<Exercise> exercise = exerciseRepository.findByIdAndUserId(exerciseDTO.getId(), user.getId());

        assertTrue(exercise.isPresent());
        assertEquals("Push Up", exercise.get().getName());
        assertEquals(MuscleGroup.CHEST, exercise.get().getPrimaryMuscleGroup());
        assertEquals(exercise.get().getUser().getId(), user.getId());
        assertEquals(ExerciseType.CUSTOM, exercise.get().getExerciseType());

    }

    @Test
    void createExercise_whenCalled_withExistingNameForUser_shouldFail() {
        CreateExerciseRequest createExerciseRequest = CreateExerciseRequest.builder()
                .name("Push Up")
                .primaryMuscleGroup(MuscleGroup.CHEST)
                .build();

        exerciseService.createExercise(createExerciseRequest, user.getId());

        CreateExerciseRequest sameName = CreateExerciseRequest.builder()
                .name("Push Up")
                .primaryMuscleGroup(MuscleGroup.CHEST)
                .build();

        assertThrows(ConflictException.class, () ->
                exerciseService.createExercise(sameName, user.getId()));
    }

    @Test
    void getAllCustomExercises_whenCalled_shouldReturnAllCustomExercisesForUser() {
        CreateExerciseRequest createExerciseRequest1 = CreateExerciseRequest.builder()
                .name("Push Up")
                .primaryMuscleGroup(MuscleGroup.CHEST)
                .build();

        CreateExerciseRequest createExerciseRequest2 = CreateExerciseRequest.builder()
                .name("Pull Up")
                .primaryMuscleGroup(MuscleGroup.UPPER_BACK)
                .build();

        exerciseService.createExercise(createExerciseRequest1, user.getId());
        exerciseService.createExercise(createExerciseRequest2, user.getId());

        List<ExerciseDTO> exercises = exerciseService.getAllCustomExercises(user.getId());

        assertEquals(2, exercises.size());
    }

    @Test
    void getAllPublicExercises_whenCalled_shouldReturnAllPublicExercises() {
        Exercise presetExercise = Exercise.builder()
                .name("preset")
                .exerciseType(ExerciseType.PRESET)
                .primaryMuscleGroup(MuscleGroup.UPPER_BACK)
                .build();


        Exercise presetExercise1 = Exercise.builder()
                .name("preset1")
                .exerciseType(ExerciseType.PRESET)
                .primaryMuscleGroup(MuscleGroup.UPPER_BACK)
                .build();

        exerciseRepository.saveAll(List.of(
                presetExercise, presetExercise1)
        );

        List<ExerciseDTO> exercises = exerciseService.getAllPublicExercises();

        assertFalse(exercises.isEmpty());
    }
}
