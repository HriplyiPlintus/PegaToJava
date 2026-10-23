package service;

import model.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryTaskManager implements TaskManager {
    private final List<Task> tasks;
    private static final AtomicInteger counter = new AtomicInteger(0);

    public InMemoryTaskManager() {
        this(new ArrayList<>());
    }
    
    public InMemoryTaskManager(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    @Override
    public boolean addTask(String title, String description) {
        Optional<Task> existingTask = this.tasks.stream()
                .filter(t -> t.title.equals(title))
                .findFirst();

        if (existingTask.isPresent())
            return false;

        tasks.add(new Task(counter.incrementAndGet(), title, description));
        return true;
    }

    @Override
    public Optional<Task> removeTask(Integer taskId) {
        Optional<Task> task = this.findTaskById(taskId);
        this.tasks.removeIf(t -> t.id.equals(taskId));

        return task;
    }

    @Override
    public void updateTaskStatus(Integer taskId, String status) {
        Optional<Task> task = this.findTaskById(taskId);
        if(task.isEmpty())
            throw new IllegalArgumentException("Задача с номером " + taskId + " не найдена");

        Status newStatus = null;
        for(Status s : Status.values()) {
            if(s.name().equalsIgnoreCase(status.trim())){
                newStatus = s;
                break;
            }
        }

        if(newStatus == null) {
            System.out.println("Неизвестный статус: " + status);
        } else {
            task.get().status = newStatus;
        }
    }

    @Override
    public List<Task> findTasksByTitle(String title){
        if (title.isBlank())
            return List.of();

        return this.tasks.stream()
                .filter(t -> t.title.toLowerCase()
                        .contains(title.trim().toLowerCase()))
                .toList();
    }

    @Override
    public List<Task> getListTasks() {
        return this.tasks;
    }

    private Optional<Task> findTaskById(Integer id) {
        return this.tasks.stream()
                .filter(t -> t.id.equals(id))
                .findFirst();
    }
}
