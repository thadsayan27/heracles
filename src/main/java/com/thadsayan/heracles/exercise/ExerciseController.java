package com.thadsayan.heracles.exercise;

import com.thadsayan.heracles.exercise.dto.ExerciseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/exercises")
public class ExerciseController {

    private ExerciseService exerciseService;

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
    public ResponseEntity<ExerciseDTO> createExercise(@RequestBody ExerciseDTO dto) {
        ExerciseDTO created = exerciseService.createExercise(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }
}
