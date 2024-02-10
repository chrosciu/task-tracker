package eu.chrost.tasktracker.task;

import org.springframework.stereotype.Component;

@Component
public class TaskMapper {
    public TaskOutputDto toDto(Task task) {
        return new TaskOutputDto(task.getId(), task.getDescription(), task.isClosed());
    }

    public Task fromDto(TaskInputDto taskDto) {
        return new Task(taskDto.getDescription());
    }
}
