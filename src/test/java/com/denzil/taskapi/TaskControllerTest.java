package com.denzil.taskapi;

import com.denzil.taskapi.controller.TaskController;
import com.denzil.taskapi.entity.Task;
import com.denzil.taskapi.entity.Task.TaskPriority;
import com.denzil.taskapi.entity.Task.TaskStatus;
import com.denzil.taskapi.exception.GlobalExceptionHandler;
import com.denzil.taskapi.exception.TaskNotFoundException;
import com.denzil.taskapi.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
@DisplayName("Task Controller Tests — TDD")
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    private Task todoTask;
    private Task inProgressTask;

    @BeforeEach
    void setUp() {
        todoTask = Task.builder()
                .id(1L)
                .title("Write unit tests")
                .description("Write TDD tests for task service")
                .status(TaskStatus.TODO)
                .priority(TaskPriority.HIGH)
                .build();

        inProgressTask = Task.builder()
                .id(2L)
                .title("Implement REST API")
                .description("Build the task management API")
                .status(TaskStatus.IN_PROGRESS)
                .priority(TaskPriority.CRITICAL)
                .build();
    }

    @Test
    @DisplayName("GET /api/v1/tasks - should return all tasks")
    void shouldReturnAllTasks() throws Exception {
        when(taskService.getAllTasks())
                .thenReturn(Arrays.asList(
                        todoTask, inProgressTask));

        mockMvc.perform(get("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title")
                        .value("Write unit tests"))
                .andExpect(jsonPath("$[0].status")
                        .value("TODO"))
                .andExpect(jsonPath("$[1].title")
                        .value("Implement REST API"));

        verify(taskService, times(1)).getAllTasks();
    }

    @Test
    @DisplayName("GET /api/v1/tasks/{id} - should return task by id")
    void shouldReturnTaskById() throws Exception {
        when(taskService.getTaskById(1L))
                .thenReturn(todoTask);

        mockMvc.perform(get("/api/v1/tasks/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title")
                        .value("Write unit tests"))
                .andExpect(jsonPath("$.status").value("TODO"))
                .andExpect(jsonPath("$.priority").value("HIGH"));

        verify(taskService, times(1)).getTaskById(1L);
    }

    @Test
    @DisplayName("GET /api/v1/tasks/{id} - should return 404 when not found")
    void shouldReturn404WhenTaskNotFound() throws Exception {
        when(taskService.getTaskById(99L))
                .thenThrow(new TaskNotFoundException(99L));

        mockMvc.perform(get("/api/v1/tasks/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Task not found with id: 99"));

        verify(taskService, times(1)).getTaskById(99L);
    }

    @Test
    @DisplayName("POST /api/v1/tasks - should create task")
    void shouldCreateTask() throws Exception {
        when(taskService.createTask(any(Task.class)))
                .thenReturn(todoTask);

        mockMvc.perform(post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper
                        .writeValueAsString(todoTask)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title")
                        .value("Write unit tests"))
                .andExpect(jsonPath("$.status")
                        .value("TODO"));

        verify(taskService, times(1))
                .createTask(any(Task.class));
    }

    @Test
    @DisplayName("PUT /api/v1/tasks/{id} - should update task")
    void shouldUpdateTask() throws Exception {
        when(taskService.updateTask(eq(1L), any(Task.class)))
                .thenReturn(inProgressTask);

        mockMvc.perform(put("/api/v1/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper
                        .writeValueAsString(inProgressTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status")
                        .value("IN_PROGRESS"));

        verify(taskService, times(1))
                .updateTask(eq(1L), any(Task.class));
    }

    @Test
    @DisplayName("PATCH /api/v1/tasks/{id}/status - should update status")
    void shouldUpdateTaskStatus() throws Exception {
        Task updatedTask = Task.builder()
                .id(1L)
                .title("Write unit tests")
                .status(TaskStatus.IN_PROGRESS)
                .priority(TaskPriority.HIGH)
                .build();

        when(taskService.updateTaskStatus(
                1L, TaskStatus.IN_PROGRESS))
                .thenReturn(updatedTask);

        mockMvc.perform(patch("/api/v1/tasks/1/status")
                .param("status", "IN_PROGRESS")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status")
                        .value("IN_PROGRESS"));

        verify(taskService, times(1))
                .updateTaskStatus(1L, TaskStatus.IN_PROGRESS);
    }

    @Test
    @DisplayName("DELETE /api/v1/tasks/{id} - should delete task")
    void shouldDeleteTask() throws Exception {
        doNothing().when(taskService).deleteTask(1L);

        mockMvc.perform(delete("/api/v1/tasks/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(taskService, times(1)).deleteTask(1L);
    }

    @Test
    @DisplayName("GET /api/v1/tasks/active - should return active tasks")
    void shouldReturnActiveTasks() throws Exception {
        when(taskService.getActiveTasks())
                .thenReturn(Arrays.asList(
                        todoTask, inProgressTask));

        mockMvc.perform(get("/api/v1/tasks/active")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(taskService, times(1)).getActiveTasks();
    }

    @Test
    @DisplayName("GET /api/v1/tasks/search - should search by title")
    void shouldSearchTasksByTitle() throws Exception {
        when(taskService.searchTasksByTitle("unit"))
                .thenReturn(List.of(todoTask));

        mockMvc.perform(get("/api/v1/tasks/search")
                .param("title", "unit")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title")
                        .value("Write unit tests"));

        verify(taskService, times(1))
                .searchTasksByTitle("unit");
    }
}