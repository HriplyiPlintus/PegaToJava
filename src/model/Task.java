package model;

public class Task {
    public final Integer id;
    public final String title;
    public final String description;
    public Status status;

    public Task(Integer id, String title, String description){
        if (title.isBlank())
            throw new IllegalArgumentException("Навание задачи не может быть пустым");

        this.title = title;
        this.description = description;
        this.status = Status.New;
        this.id = id;
    }

    public Task(Integer id, String title, String description, String status) {
        this(id, title, description);
        this.status = Status.valueOf(status);
    }
}