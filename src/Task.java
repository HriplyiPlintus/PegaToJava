import java.util.concurrent.atomic.AtomicInteger;

public class Task {
    private static final AtomicInteger counter = new AtomicInteger(0);
    public final Integer id;
    public final String title;
    private String description;
    private Status status;

    public Task(String title, String description, Status status){
        this.title = title;
        this.description = description;
        this.status = status;
        this.id = counter.incrementAndGet();
    }
}

enum Status {
    ToDo,
    InProgress,
    Done,
    Cancelled
}