package eu.chrost.tasktracker.task;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskApi {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @GetMapping
    public ResponseEntity<List<TaskOutputDto>> getAllTasks() {
        return ResponseEntity.ok(taskRepository.findAll().stream()
                .map(taskMapper::toDto)
                .toList());
    }

    @PostMapping
    public ResponseEntity<Void> createTask(@RequestBody @Valid TaskInputDto taskDto) {
        taskRepository.save(taskMapper.fromDto(taskDto));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("{id}/close")
    @Transactional
    public ResponseEntity<Void> closeTask(@PathVariable long id) {
        var task = taskRepository.findById(id);
        task.ifPresent(Task::close);
        return ResponseEntity.noContent().build();
    }
}
