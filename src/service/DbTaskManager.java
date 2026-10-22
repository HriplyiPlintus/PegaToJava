package service;

import dto.CreateTaskDto;
import dto.SelectTaskDto;
import model.Task;
import repository.TaskRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DbTaskManager implements TaskManager {
    private static TaskRepository repository;

    public DbTaskManager () {
        repository = new TaskRepository();
    }

    @Override
    public boolean addTask(Task task) {
        CreateTaskDto taskDto = new CreateTaskDto(task.title, task.description, task.status.toString());
        repository.createTask(taskDto);
        return true;
    }

    @Override
    public Optional<Task> removeTask(Integer taskId) {
        try {
            repository.removeTaskById(taskId);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public void updateTaskStatus(Integer taskId, String status) {
        try {
            repository.updateTaskStatus(taskId, status);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<Task> findTaskByTitle(String title) {
        return List.of();
    }

    @Override
    public List<Task> getListTasks() {
        try {
            List<SelectTaskDto> tasks = repository.getAllTasks();
            List<Task> result = new ArrayList<>();

            for (SelectTaskDto taskDto : tasks) {
                result.add(new Task(taskDto.title(), taskDto.description(), taskDto.status()));
            }

            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
