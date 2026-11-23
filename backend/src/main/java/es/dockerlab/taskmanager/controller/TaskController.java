package es.dockerlab.taskmanager.controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final JdbcTemplate jdbcTemplate;

    public TaskController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping
    public List<Map<String, Object>> tasks() {
        initialiseDatabase();

        return jdbcTemplate.query(
                "SELECT id, title, completed FROM tasks ORDER BY id",
                (rs, rowNum) -> Map.of(
                        "id", rs.getLong("id"),
                        "title", rs.getString("title"),
                        "completed", rs.getBoolean("completed")
                )
        );
    }

    private void initialiseDatabase() {
        jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS tasks (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "title VARCHAR(255) NOT NULL, " +
                "completed BOOLEAN NOT NULL DEFAULT FALSE" +
                ")"
        );

        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM tasks",
                Integer.class
        );

        if (count != null && count == 0) {
            jdbcTemplate.update(
                    "INSERT INTO tasks (title, completed) VALUES (?, ?)",
                    "Crear las imágenes Docker",
                    true
            );
            jdbcTemplate.update(
                    "INSERT INTO tasks (title, completed) VALUES (?, ?)",
                    "Configurar las redes",
                    false
            );
            jdbcTemplate.update(
                    "INSERT INTO tasks (title, completed) VALUES (?, ?)",
                    "Verificar el despliegue",
                    false
            );
        }
    }
}
