package com.thadsayan.heracles.exercise.service;


import com.thadsayan.heracles.exercise.repository.ExerciseRepository;
import com.thadsayan.heracles.exercise.dto.CreateExerciseRequest;
import com.thadsayan.heracles.exercise.dto.ExerciseDTO;
import com.thadsayan.heracles.exercise.enums.ExerciseType;
import com.thadsayan.heracles.exercise.mapper.ExerciseMapper;
import com.thadsayan.heracles.exercise.model.Exercise;
import com.thadsayan.heracles.shared.exception.ConflictException;
import com.thadsayan.heracles.shared.exception.NotFoundException;
import com.thadsayan.heracles.user.repository.UserRepository;
import com.thadsayan.heracles.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final ExerciseMapper exerciseMapper;
    private final UserRepository userRepository;

    public ExerciseDTO getExerciseById(UUID id, UUID userId) {
        return exerciseRepository.findByIdAndUserId(id, userId)
                .map(exerciseMapper::toDTO)
                .orElseThrow(() -> new NotFoundException("Workout not found"));
    }

    public List<ExerciseDTO> getAllCustomExercises(UUID userId) {
        return exerciseMapper.toDTOList(exerciseRepository.findAllByUser_Id(userId));
    }

    public List<ExerciseDTO> getAllPublicExercises() {
        return exerciseMapper.toDTOList(exerciseRepository.findAllByExerciseType(ExerciseType.PRESET));
    }

    public ExerciseDTO createExercise(CreateExerciseRequest createExerciseRequest, UUID userId){
        Exercise exercise = exerciseMapper.toEntity(createExerciseRequest);
        if(exerciseRepository.existsByNameIgnoreCaseAndUserId(exercise.getName(), userId))
            throw new ConflictException("Exercise already exists");

        User user = userRepository.getReferenceById(userId);
        exercise.setUser(user);
        exercise.setExerciseType(ExerciseType.CUSTOM);

        return exerciseMapper.toDTO(exerciseRepository.save(exercise));
    }

    public boolean canAccess(UUID id, UUID userId) {
        return exerciseRepository.canAccess(id, userId);
    }
}
