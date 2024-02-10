package eu.chrost.tasktracker.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TaskInputDto {
    @NotBlank
    @Size(max = 255)
    private String description;
}
