package com.denzil.taskapi.controller;

import com.denzil.taskapi.entity.Task;
import com.denzil.taskapi.entity.Task.TaskPriority;
import com.denzil.taskapi.entity.Task.TaskStatus;
import com.denzil.taskapi.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Slf4j
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        log.info("GET /api/v1/tasks");
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(
            @PathVariable Long id) {
        log.info("GET /api/v1/tasks/{}", id);
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @PostMapping
    public ResponseEntity<Task> createTask(
            @Valid @RequestBody Task task) {
        log.info("POST /api/v1/tasks - {}", task.getTitle());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(taskService.createTask(task));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody Task task) {
        log.info("PUT /api/v1/tasks/{}", id);
        return ResponseEntity.ok(taskService.updateTask(id, task));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Task> updateTaskStatus(
            @PathVariable Long id,
            @RequestParam TaskStatus status) {
        log.info("PATCH /api/v1/tasks/{}/status - {}", id, status);
        return ResponseEntity.ok(
                taskService.updateTaskStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long id) {
        log.info("DELETE /api/v1/tasks/{}", id);
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Task>> getTasksByStatus(
            @PathVariable TaskStatus status) {
        log.info("GET /api/v1/tasks/status/{}", status);
        return ResponseEntity.ok(
                taskService.getTasksByStatus(status));
    }

    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<Task>> getTasksByPriority(
            @PathVariable TaskPriority priority) {
        log.info("GET /api/v1/tasks/priority/{}", priority);
        return ResponseEntity.ok(
                taskService.getTasksByPriority(priority));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Task>> searchTasks(
            @RequestParam String title) {
        log.info("GET /api/v1/tasks/search?title={}", title);
        return ResponseEntity.ok(
                taskService.searchTasksByTitle(title));
    }

    @GetMapping("/active")
    public ResponseEntity<List<Task>> getActiveTasks() {
        log.info("GET /api/v1/tasks/active");
        return ResponseEntity.ok(taskService.getActiveTasks());
    }
}