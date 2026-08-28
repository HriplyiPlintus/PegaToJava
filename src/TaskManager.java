import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskManager {
    private final List<Task> tasks;

    public TaskManager() {
        this(new ArrayList<>());
    }
    
    public TaskManager(List<Task> tasks) {

        this.tasks = new ArrayList<>(tasks);
    }

    public boolean addTask(Task task) {
        Optional<Task> existingTask = this.tasks.stream()
                .filter(t -> t.title.equals(task.title))
                .findFirst();

        if (existingTask.isPresent())
            return false;

        tasks.add(task);
        return true;
    }

    public Optional<Task> removeTask(Integer taskId) {
        Optional<Task> task = this.findTaskById(taskId);
        this.tasks.removeIf(t -> t.id.equals(taskId));

        return task;
    }

    public List<Task> findTaskByTitle(String title){
        if (title.isBlank())
            return List.of();

        return this.tasks.stream()
                .filter(t -> t.title.toLowerCase()
                        .contains(title.trim().toLowerCase()))
                .toList();
    }

    public List<Task> getListTasks() {
        return this.tasks;
    }

    private Optional<Task> findTaskById(Integer id) {
        return this.tasks.stream()
                .filter(t -> t.id.equals(id))
                .findFirst();
    }
}
