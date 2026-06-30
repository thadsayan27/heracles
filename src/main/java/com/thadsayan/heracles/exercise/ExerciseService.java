package com.thadsayan.heracles.exercise;


import com.thadsayan.heracles.exercise.dto.CreateExerciseRequest;
import com.thadsayan.heracles.exercise.dto.ExerciseDTO;
import com.thadsayan.heracles.exercise.enums.ExerciseType;
import com.thadsayan.heracles.exercise.mapper.ExerciseMapper;
import com.thadsayan.heracles.exercise.model.Exercise;
import com.thadsayan.heracles.user.UserRepository;
import com.thadsayan.heracles.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final ExerciseMapper exerciseMapper;
    private final UserRepository userRepository;

    public Optional<ExerciseDTO> getExerciseById(UUID id, User user) {
        return exerciseRepository.findByIdAndUserId(id, user.getId())
                .map(exerciseMapper::toDTO);
    }

    public ExerciseDTO createExercise(CreateExerciseRequest createExerciseRequest, User user){
        Exercise exercise = exerciseMapper.toEntity(createExerciseRequest);
        if(exerciseRepository.existsByNameIgnoreCaseAndUserId(exercise.getName(), user.getId()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Exercise with the same name already exists");

        exercise.setUser(user);
        exercise.setExerciseType(ExerciseType.CUSTOM);

        return exerciseMapper.toDTO(exerciseRepository.save(exercise));
    }
}
