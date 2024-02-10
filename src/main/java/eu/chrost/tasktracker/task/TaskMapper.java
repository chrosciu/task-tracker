package eu.chrost.tasktracker.task;

import org.springframework.stereotype.Component;

@Component
public class TaskMapper {
    public TaskDto toDto(Task task) {
        return new TaskDto(task.getId(), task.getDescription(), task.isClosed());
    }

    public Task fromDto(TaskDto taskDto) {
        return new Task(taskDto.getDescription(), taskDto.isClosed());
    }
}
