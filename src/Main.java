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
        Boolean tryAgain = false;
        String userInput = "";

        while (!shouldExit) {
            if (!tryAgain) {
                System.out.print("Введите команду: ");
                userInput = scanner.nextLine();
            }

            switch(userInput.trim().toLowerCase()) {
                case "add":
                    System.out.print("Введите название задачи: ");
                    String taskTitle = scanner.nextLine();

                    if (addTask(taskTitle) == -1) {
                        tryAgain = true;
                        continue;
                    }
                    tryAgain = false;

                    break;
                case "delete":
                    System.out.print("Введите номер задачи для удаления: ");
                    String userProvidedNumber = scanner.nextLine();

                    try {
                        Integer taskNumber = Integer.parseInt(userProvidedNumber);
                        deleteTask(taskNumber);
                    } catch (Exception ex) {
                        if (ex instanceof NumberFormatException) {
                            System.out.println("Введите число");
                        } else {
                            System.out.println(ex.getClass());
                            System.out.println("Вот такая вот ошибка " + ex.getMessage());
                        }

                        tryAgain = true;
                        continue;
                    }

                    tryAgain = false;

                    break;
                case "find":
                    System.out.print("Введите номер или название задачи: ");
                    findTask(scanner.nextLine());
                    break;
                case "update":
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
        newTasks[tasks.length] = title;
        tasks = newTasks;

        return tasks.length;
    }

    static void deleteTask(int number) throws InvalidAttributeValueException {
        System.out.println("This is the nubmer " + number);
        number = number - 1;

        if (number < 0 || number > tasks.length) {
            throw new InvalidAttributeValueException("Введите номер от 1 до " + tasks.length);
        }

        String[] newTasks = new String[tasks.length - 1];

        int j = 0;
        for (int i = 0; i < tasks.length; i++) {
            if (i == number) {
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
    static void updateTask(int number, String newTitle) { }
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