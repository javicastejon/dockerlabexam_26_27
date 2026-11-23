package es.dockerlab.taskmanager.controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class StatusController {

    private final JdbcTemplate jdbcTemplate;

    public StatusController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/status")
    public Map<String, String> status() {
        Map<String, String> response = new LinkedHashMap<>();
        response.put("application", "running");

        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            response.put("database", "connected");
        } catch (Exception ex) {
            response.put("database", "disconnected");
        }

        return response;
    }
}
