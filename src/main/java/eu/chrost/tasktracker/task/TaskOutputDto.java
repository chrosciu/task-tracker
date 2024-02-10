package eu.chrost.tasktracker.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskOutputDto {
    private long id;
    private String description;
    private boolean closed;
}
