package service;

import model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskManager {
    boolean addTask(Task task);

    Optional<Task> removeTask(Integer taskId);

    void updateTaskStatus(Integer taskId, String status);

    List<Task> findTaskByTitle(String title);

    List<Task> getListTasks();
}
