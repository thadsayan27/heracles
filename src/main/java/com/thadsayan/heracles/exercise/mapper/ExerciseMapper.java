package com.thadsayan.heracles.exercise.mapper;


import com.thadsayan.heracles.exercise.Exercise;
import com.thadsayan.heracles.exercise.dto.ExerciseDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExerciseMapper {

    ExerciseDTO toDTO(Exercise exercise);
    List<ExerciseDTO> toDTOList(List<Exercise> exercises);

    Exercise toEntity(ExerciseDTO dto);
}
