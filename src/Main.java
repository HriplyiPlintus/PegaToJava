import model.Task;
import service.TaskManager;
import java.util.*;

public class Main {
    static TaskManager taskManager = new TaskManager();

    public static void main(String[] args) {
        System.out.println("""
                Менеджер задач
                
                Доступные команды:
                add    - добавить задачу
                delete - удалить по номеру
                find   - найти по идентификатору или названию
                update - изменить задачу
                list   - показать все
                exit   - выход
                """);

        Scanner scanner = new Scanner(System.in);

        boolean shouldExit = false;

        while (!shouldExit) {
            System.out.print("Введите команду: ");
            String userInput = scanner.nextLine();

            switch(userInput.trim().toLowerCase()) {
                case "add":
                    System.out.print("Введите название задачи: ");
                    String taskTitle = scanner.nextLine().trim();

                    System.out.print("Введите описание для задачи " + taskTitle + ": ");
                    String taskDescription = scanner.nextLine().trim();

                    try {
                        Task newTask = new Task(taskTitle, taskDescription);

                        taskManager.addTask(newTask);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "delete":
                    System.out.print("Введите номер задачи для удаления: ");
                    String userProvidedNumber = scanner.nextLine();


                    Integer taskNumberToDelete;
                    try {
                        taskNumberToDelete = Integer.parseInt(userProvidedNumber);
                    } catch (NumberFormatException ex) {
                        System.out.println("Введите число");
                        break;
                    }

                    Optional<Task> removedTask = taskManager.removeTask(taskNumberToDelete);

                    if (removedTask.isPresent()) {
                        System.out.println("Задача \"" + removedTask.get().title + "\" удалена");
                    } else {
                        System.out.println("Задача не найдена");
                    }
                    break;
                case "find":
                    System.out.print("Введите название задачи: ");
                    String titleQuery = scanner.nextLine();

                    List<Task> tasks = taskManager.findTaskByTitle(titleQuery);

                    if(tasks.isEmpty()){
                        System.out.println("Задач нет");
                        break;
                    }

                    for (Task task : tasks) {
                        printTask(task);
                    }

                    break;
                case "update":
                    System.out.print("Введите номер задачи для изменения: ");
                    String taskNumberToUpdate = scanner.nextLine();

                    Integer taskNumber = null;

                    try {
                        taskNumber = Integer.parseInt(taskNumberToUpdate);

                        System.out.print("Введите новый статус (New, ToDo, InProgress, Done, Cancelled): ");
                        String newStatus = scanner.nextLine();

                        taskManager.updateTaskStatus(taskNumber, newStatus);
                    } catch (NumberFormatException ex) {
                        System.out.println("Введите число");
                    } catch (IllegalArgumentException iex) {
                        System.out.println("Введен несуществующий id: " + taskNumber);
                    }

                    break;
                case "list":
                    List<Task> allTasks = taskManager.getListTasks();
                    for(Task t : allTasks) {
                        printTask(t);
                    }
                    break;
                case "exit":
                    shouldExit = true;

                    break;
                default:
                    System.out.println("Неопознанная команда");

                    break;
            }
        }
    }

    static void printTask(Task t) {
        System.out.println("Задача: " + t.title
                + "; Описание: " + t.description
                + "; Статус: " + t.status);
    }
}
