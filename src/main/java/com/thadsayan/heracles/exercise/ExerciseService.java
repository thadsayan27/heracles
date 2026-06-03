package com.thadsayan.heracles.exercise;


import com.thadsayan.heracles.exercise.dto.ExerciseDTO;
import com.thadsayan.heracles.exercise.mapper.ExerciseMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ExerciseService {
    private ExerciseRepository exerciseRepository;
    private ExerciseMapper exerciseMapper;

    public Optional<ExerciseDTO> getExerciseById(UUID id) {
        return exerciseRepository.findById(id)
                .map(exerciseMapper::toDTO);
    }

    public List<ExerciseDTO> getExercisesByName(String name) {
        return exerciseMapper.toDTOList(exerciseRepository.findByName(name));
    }

    public ExerciseDTO createExercise(ExerciseDTO dto) {
        Exercise exercise = exerciseMapper.toEntity(dto);
        Exercise saved = exerciseRepository.save(exercise);
        return exerciseMapper.toDTO(saved);
    }
}
