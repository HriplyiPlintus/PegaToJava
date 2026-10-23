package service;

import dto.CreateTaskDto;
import dto.SelectTaskDto;
import model.Status;
import model.Task;
import repository.TaskRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DbTaskManager implements TaskManager {
    private final TaskRepository repository;

    public DbTaskManager () {
        repository = new TaskRepository();
    }

    @Override
    public boolean addTask(String title, String description) {
        CreateTaskDto taskDto = new CreateTaskDto(title, description, Status.New.toString());
        repository.createTask(taskDto);
        return true;
    }

    @Override
    public Optional<Task> removeTask(Integer taskId) {
        try {
            SelectTaskDto removedTask = repository.removeTaskById(taskId);
            return Optional.of(new Task(
                    Integer.parseInt(removedTask.id()),
                    removedTask.title(),
                    removedTask.description(),
                    removedTask.status()));
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    @Override
    public void updateTaskStatus(Integer taskId, String status) {
        try {
            repository.updateTaskStatus(taskId, status);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public List<Task> findTasksByTitle(String title) {
        List<Task> result = new ArrayList<>();

        try {
            List<SelectTaskDto> tasks = repository.findTasksByTitle(title);
            for (SelectTaskDto t : tasks) {
                result.add(new Task(Integer.parseInt(t.id()), t.title(), t.description(), t.status()));
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        return result;
    }

    @Override
    public List<Task> getListTasks() {
        List<Task> result = new ArrayList<>();

        try {
            List<SelectTaskDto> tasks = repository.getAllTasks();

            for (SelectTaskDto taskDto : tasks) {
                result.add(new Task(Integer.parseInt(taskDto.id()), taskDto.title(), taskDto.description(), taskDto.status()));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        return result;
    }
}
