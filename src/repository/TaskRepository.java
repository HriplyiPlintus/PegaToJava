package repository;

import dto.CreateTaskDto;
import dto.SelectTaskDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    private static final String UPDATE_TASK_STATUS_SQL = """
            UPDATE tasks
            SET status = ?
            WHERE id = ?
            """;

    private static final String REMOVE_TASK_BY_ID_SQL = """
            DELETE FROM tasks
            WHERE id = ?
            """;

    private String url = "jdbc:postgresql://localhost:5432/pega_to_java";
    private String username = "pega_to_java";   //TODO: вынести в .env
    private String password = "pega_to_java";

    public TaskRepository() {
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        try(Connection connection = getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(CREATE_TABLE_SQL);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при создании таблицы", e);
        }
    }

    public void createTask(CreateTaskDto task) {
        try(Connection connection = getConnection()) {
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
        try(Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement(GET_TASKS_ALL_SQL);
            ResultSet rs = statement.executeQuery();
            List<SelectTaskDto> tasks = new ArrayList<SelectTaskDto>();
            while (rs.next()) {
                SelectTaskDto task = new SelectTaskDto(rs.getString("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("status"));
                tasks.add(task);
            }
            return tasks;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка получения списка задач");
        }
    }

    public void updateTaskStatus(Integer id, String status) throws SQLException {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(UPDATE_TASK_STATUS_SQL);
            statement.setString(1, status);
            statement.setInt(2, id);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка обновления статуса задачи");
        }
    }

    public void removeTaskById(Integer taskId) throws SQLException {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(REMOVE_TASK_BY_ID_SQL);
            statement.setInt(1, taskId);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка удаления задачи с id = " + taskId);
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}

