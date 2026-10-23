package repository;

import dto.CreateTaskDto;
import dto.SelectTaskDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskRepository {
    private static final String CREATE_TABLE_SQL = """
            CREATE TABLE IF NOT EXISTS tasks (
                id SERIAL PRIMARY KEY,
                title VARCHAR(255) NOT NULL,
                description VARCHAR(255),
                status VARCHAR(255)
            )""";

    private static final String CREATE_TASK_SQL = """
            INSERT INTO tasks (title, description, status)
            VALUES (?, ?, ?)
            """;

    private static final String GET_TASKS_ALL_SQL = """
            SELECT *
            FROM tasks
            """;
    private static final String GET_TASKS_BY_TITLE_SQL = """
            SELECT *
            FROM tasks
            WHERE lower(title) like ?
            """;

    private static final String UPDATE_TASK_STATUS_SQL = """
            UPDATE tasks
            SET status = ?
            WHERE id = ?
            """;

    private static final String REMOVE_TASK_BY_ID_SQL = """
            DELETE FROM tasks
            WHERE id = ?
            """;

    private static final String GET_TASK_BY_ID_SQL = """
            SELECT *
            FROM tasks
            WHERE id = ?
            """;

    public TaskRepository() {
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        try (Connection connection = getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(CREATE_TABLE_SQL);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при создании таблицы", e);
        }
    }

    public void createTask(CreateTaskDto task) {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(CREATE_TASK_SQL);
            statement.setString(1, task.title());
            statement.setString(2, task.description());
            statement.setString(3, task.status());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при добавлении задачи", e);
        }
    }

    public List<SelectTaskDto> getAllTasks() throws SQLException {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(GET_TASKS_ALL_SQL);
            ResultSet rs = statement.executeQuery();
            List<SelectTaskDto> tasks = new ArrayList<>();
            while (rs.next()) {
                SelectTaskDto task = new SelectTaskDto(rs.getString("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("status"));
                tasks.add(task);
            }
            return tasks;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка получения списка задач", e);
        }
    }

    public void updateTaskStatus(Integer id, String status) throws SQLException {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(UPDATE_TASK_STATUS_SQL);
            statement.setString(1, status);
            statement.setInt(2, id);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка обновления статуса задачи", e);
        }
    }

    public SelectTaskDto removeTaskById(Integer taskId) throws SQLException {
        try (Connection connection = getConnection()) {
            SelectTaskDto taskToRemove = getTaskById(taskId);
            PreparedStatement statement = connection.prepareStatement(REMOVE_TASK_BY_ID_SQL);
            statement.setInt(1, taskId);

            statement.executeUpdate();
            return taskToRemove;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка удаления задачи с id = " + taskId, e);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    public List<SelectTaskDto> findTasksByTitle(String title) {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(GET_TASKS_BY_TITLE_SQL);
            statement.setString(1, "%" + title.toLowerCase() + "%");

            List<SelectTaskDto> result = new ArrayList<>();
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                SelectTaskDto task = new SelectTaskDto(
                        rs.getString("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("status"));

                result.add(task);
            }

            return result;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка получения списка задач по названию", e);
        }
    }

    private SelectTaskDto getTaskById(Integer id) {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(GET_TASK_BY_ID_SQL);
            statement.setString(1, id.toString());

            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                return new SelectTaskDto(
                        rs.getString("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("status"));
            }

            throw new RuntimeException("Задач с id = " + id + " не найдено");
        } catch (SQLException e) {
            throw new RuntimeException("Не удалось получить задачу с id = " + id, e);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    private Connection getConnection() throws SQLException {
        String password = "pega_to_java";
        //TODO: вынести в .env
        String username = "pega_to_java";
        String url = "jdbc:postgresql://localhost:5432/pega_to_java";
        return DriverManager.getConnection(url, username, password);
    }
}

