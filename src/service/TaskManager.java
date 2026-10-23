package service;

import model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskManager {
    boolean addTask(String title, String description);

    Optional<Task> removeTask(Integer taskId);

    void updateTaskStatus(Integer taskId, String status);

    List<Task> findTasksByTitle(String title);

    List<Task> getListTasks();
}
