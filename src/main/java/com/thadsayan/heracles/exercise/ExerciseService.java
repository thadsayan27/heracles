package com.thadsayan.heracles.exercise;


import com.thadsayan.heracles.exercise.dto.CreateExerciseRequest;
import com.thadsayan.heracles.exercise.dto.ExerciseDTO;
import com.thadsayan.heracles.exercise.mapper.ExerciseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final ExerciseMapper exerciseMapper;

    public Optional<ExerciseDTO> getExerciseById(UUID id) {
        return exerciseRepository.findById(id)
                .map(exerciseMapper::toDTO);
    }

    public List<ExerciseDTO> getExercisesByName(String name) {
        return exerciseMapper.toDTOList(exerciseRepository.findByName(name));
    }

    public ExerciseDTO createExercise(CreateExerciseRequest createExerciseRequest){
        Exercise exercise = exerciseMapper.toEntity(createExerciseRequest);
        if(exerciseRepository.existsByNameIgnoreCase(exercise.getName()))
            throw new IllegalArgumentException("Exercise with the same name already exists");
        return exerciseMapper.toDTO(exerciseRepository.save(exercise));
    }
}
