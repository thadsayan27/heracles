package com.thadsayan.heracles.exercise;

import com.thadsayan.heracles.exercise.dto.CreateExerciseRequest;
import com.thadsayan.heracles.exercise.dto.ExerciseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/exercises")
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseService exerciseService;

    @GetMapping("/{id}")
    public ResponseEntity<ExerciseDTO> getExerciseById(@PathVariable UUID id) {
        return exerciseService.getExerciseById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ExerciseDTO>> findByName(@RequestParam(required = false) String name) {
        if (name == null || name.isBlank())
            return ResponseEntity.ok(List.of());

        return ResponseEntity.ok(exerciseService.getExercisesByName(name));
    }

    @PostMapping
    public ResponseEntity<ExerciseDTO> createExercise(
            @Valid @RequestBody CreateExerciseRequest createExerciseRequest) {

        ExerciseDTO createdExercise = exerciseService.createExercise(createExerciseRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdExercise.getId())
                .toUri();

        return ResponseEntity.created(location)
                .body(createdExercise);
    }
}
