package model;

import javax.print.DocFlavor;
import java.util.concurrent.atomic.AtomicInteger;

public class Task {
    private static final AtomicInteger counter = new AtomicInteger(0);
    public final Integer id;
    public final String title;
    public final String description;
    public Status status;

    public Task(String title, String description){
        if (title.isBlank())
            throw new IllegalArgumentException("Навание задачи не может быть пустым");

        this.title = title;
        this.description = description;
        this.status = Status.New;
        this.id = counter.incrementAndGet();
    }

    public Task(String title, String description, String status) {
        this(title, description);
        this.status = Status.valueOf(status);
    }
}