import javax.management.InvalidAttributeValueException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    static String[] tasks = new String[0];

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

        Boolean shouldExit = false;

        while (!shouldExit) {
            System.out.print("Введите команду: ");
            String userInput = scanner.nextLine();

            switch(userInput.trim().toLowerCase()) {
                case "add":
                    System.out.print("Введите название задачи: ");
                    String taskTitle = scanner.nextLine();

                    if (addTask(taskTitle) == -1) {
                        System.out.println("Название не может быть пустым");
                    }

                    break;
                case "delete":
                    System.out.print("Введите номер задачи для удаления: ");
                    String userProvidedNumber = scanner.nextLine();

                    try {
                        int taskNumberToDelete = Integer.parseInt(userProvidedNumber);
                        deleteTask(taskNumberToDelete);
                    } catch (NumberFormatException ex) {
                        System.out.println("Введите число");
                    } catch (InvalidAttributeValueException ex) {
                        System.out.println(ex.getMessage());
                    }

                    break;
                case "find":
                    System.out.print("Введите номер или название задачи: ");
                    findTask(scanner.nextLine());

                    break;
                case "update":
                    System.out.print("Введите номер задачи для изменения: ");
                    String taskNumberToUpdate = scanner.nextLine();

                    try {
                        int taskNumber = Integer.parseInt(taskNumberToUpdate);

                        System.out.print("Введите новое название: ");
                        String newTitle = scanner.nextLine();

                        updateTask(taskNumber, newTitle);
                    } catch (NumberFormatException ex) {
                        System.out.println("Введите число");
                    } catch (InvalidAttributeValueException ex) {
                        System.out.println(ex.getMessage());
                    }

                    break;
                case "list":
                    printTasks();

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

    static Integer addTask(String title) {
        //can't create task
        if (title.isBlank()) {
            return -1;
        }

        String[] newTasks = Arrays.copyOf(tasks, tasks.length + 1);
        newTasks[tasks.length] = title.trim();
        tasks = newTasks;

        return tasks.length;
    }

    static void deleteTask(int number) throws InvalidAttributeValueException {
        if (tasks.length == 0) {
            throw new InvalidAttributeValueException("Список пуст, нечего удалять");
        }

        int index = number - 1;

        if (index < 0 || index >= tasks.length) {
            throw new InvalidAttributeValueException("Введите номер от 1 до " + tasks.length);
        }

        String[] newTasks = new String[tasks.length - 1];

        int j = 0;
        for (int i = 0; i < tasks.length; i++) {
            if (i == index) {
                continue;
            }

            newTasks[j] = tasks[i];
            j++;
        }

        tasks = newTasks;
    }

    static void findTask(String query) {
        String trimmedQuery = query.trim().toLowerCase();

        if (trimmedQuery.isBlank()) {
            System.out.println("Пустой запрос");
            return;
        }

        boolean found = false;

        for (int i = 0; i < tasks.length; i++) {
            boolean matchesNumber = String.valueOf(i + 1).equals(trimmedQuery);
            boolean matchesTitle = tasks[i].toLowerCase().contains(trimmedQuery);

            if (matchesNumber || matchesTitle) {
                if (!found) {
                    System.out.println("Найдено:");
                    found = true;
                }
                System.out.println((i + 1) + ": " + tasks[i]);
            }
        }

        if (!found) {
            System.out.println("Ничего не найдено");
        }
    }

    static void updateTask(int number, String newTitle) throws InvalidAttributeValueException {
        if (tasks.length == 0) {
            throw new InvalidAttributeValueException("Список пуст, нечего изменять");
        }

        int index = number - 1;

        if (index < 0 || index >= tasks.length) {
            throw new InvalidAttributeValueException("Введите номер от 1 до " + tasks.length);
        }

        String trimmedTitle = newTitle.trim();

        if (trimmedTitle.isBlank()) {
            throw new InvalidAttributeValueException("Название не может быть пустым");
        }

        String oldTitle = tasks[index];
        tasks[index] = trimmedTitle;

        System.out.println("Задача " + number + " изменена: " + oldTitle + " -> " + trimmedTitle);
    }

    static void printTasks() {
        if (tasks.length == 0) {
            System.out.println("Список пуст");
            return;
        }

        System.out.println("Задачи:");
        for (int i = 0; i < tasks.length; i++) {
            System.out.println(String.valueOf(i + 1) + ": " + tasks[i]);
        }
    }
}
