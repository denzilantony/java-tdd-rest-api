package com.denzil.taskapi;

import com.denzil.taskapi.entity.Task;
import com.denzil.taskapi.entity.Task.TaskPriority;
import com.denzil.taskapi.entity.Task.TaskStatus;
import com.denzil.taskapi.exception.TaskNotFoundException;
import com.denzil.taskapi.repository.TaskRepository;
import com.denzil.taskapi.service.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Task Service Tests — TDD")
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task todoTask;
    private Task inProgressTask;
    private Task doneTask;

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

        doneTask = Task.builder()
                .id(3L)
                .title("Setup project")
                .description("Initialize Spring Boot project")
                .status(TaskStatus.DONE)
                .priority(TaskPriority.MEDIUM)
                .build();
    }

    @Test
    @DisplayName("Should return all tasks successfully")
    void shouldReturnAllTasks() {
        when(taskRepository.findAll())
                .thenReturn(Arrays.asList(
                        todoTask, inProgressTask, doneTask));

        List<Task> tasks = taskService.getAllTasks();

        assertThat(tasks).isNotEmpty();
        assertThat(tasks).hasSize(3);
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return task by id successfully")
    void shouldReturnTaskById() {
        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(todoTask));

        Task found = taskService.getTaskById(1L);

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(1L);
        assertThat(found.getTitle()).isEqualTo("Write unit tests");
        assertThat(found.getStatus()).isEqualTo(TaskStatus.TODO);
        assertThat(found.getPriority()).isEqualTo(TaskPriority.HIGH);
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw TaskNotFoundException when task not found")
    void shouldThrowExceptionWhenTaskNotFound() {
        when(taskRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.getTaskById(99L))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining("Task not found with id: 99");
        verify(taskRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Should create task successfully")
    void shouldCreateTaskSuccessfully() {
        when(taskRepository.save(any(Task.class)))
                .thenReturn(todoTask);

        Task created = taskService.createTask(todoTask);

        assertThat(created).isNotNull();
        assertThat(created.getTitle()).isEqualTo("Write unit tests");
        assertThat(created.getStatus()).isEqualTo(TaskStatus.TODO);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    @DisplayName("Should update task successfully")
    void shouldUpdateTaskSuccessfully() {
        Task updatedDetails = Task.builder()
                .title("Write unit and integration tests")
                .description("Updated description")
                .status(TaskStatus.IN_PROGRESS)
                .priority(TaskPriority.CRITICAL)
                .build();

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(todoTask));
        when(taskRepository.save(any(Task.class)))
                .thenReturn(updatedDetails);

        Task updated = taskService.updateTask(1L, updatedDetails);

        assertThat(updated.getTitle())
                .isEqualTo("Write unit and integration tests");
        assertThat(updated.getStatus())
                .isEqualTo(TaskStatus.IN_PROGRESS);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    @DisplayName("Should update task status successfully")
    void shouldUpdateTaskStatus() {
        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(todoTask));
        when(taskRepository.save(any(Task.class)))
                .thenReturn(todoTask);

        Task updated = taskService.updateTaskStatus(
                1L, TaskStatus.IN_PROGRESS);

        assertThat(updated).isNotNull();
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    @DisplayName("Should delete task successfully")
    void shouldDeleteTaskSuccessfully() {
        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(todoTask));
        doNothing().when(taskRepository).delete(any(Task.class));

        taskService.deleteTask(1L);

        verify(taskRepository, times(1))
                .delete(any(Task.class));
    }

    @Test
    @DisplayName("Should return tasks by status")
    void shouldReturnTasksByStatus() {
        when(taskRepository.findByStatus(TaskStatus.TODO))
                .thenReturn(Arrays.asList(todoTask));

        List<Task> tasks = taskService
                .getTasksByStatus(TaskStatus.TODO);

        assertThat(tasks).hasSize(1);
        assertThat(tasks.get(0).getStatus())
                .isEqualTo(TaskStatus.TODO);
        verify(taskRepository, times(1))
                .findByStatus(TaskStatus.TODO);
    }

    @Test
    @DisplayName("Should return tasks by priority")
    void shouldReturnTasksByPriority() {
        when(taskRepository.findByPriority(TaskPriority.HIGH))
                .thenReturn(Arrays.asList(todoTask));

        List<Task> tasks = taskService
                .getTasksByPriority(TaskPriority.HIGH);

        assertThat(tasks).hasSize(1);
        assertThat(tasks.get(0).getPriority())
                .isEqualTo(TaskPriority.HIGH);
        verify(taskRepository, times(1))
                .findByPriority(TaskPriority.HIGH);
    }

    @Test
    @DisplayName("Should search tasks by title")
    void shouldSearchTasksByTitle() {
        when(taskRepository.findByTitleContainingIgnoreCase("unit"))
                .thenReturn(Arrays.asList(todoTask));

        List<Task> tasks = taskService.searchTasksByTitle("unit");

        assertThat(tasks).hasSize(1);
        assertThat(tasks.get(0).getTitle())
                .contains("unit");
        verify(taskRepository, times(1))
                .findByTitleContainingIgnoreCase("unit");
    }

    @Test
    @DisplayName("Should return active tasks only")
    void shouldReturnActiveTasks() {
        when(taskRepository.findActiveTasks())
                .thenReturn(Arrays.asList(todoTask, inProgressTask));

        List<Task> activeTasks = taskService.getActiveTasks();

        assertThat(activeTasks).hasSize(2);
        assertThat(activeTasks)
                .noneMatch(t -> t.getStatus() == TaskStatus.DONE);
        assertThat(activeTasks)
                .noneMatch(t -> t.getStatus()
                        == TaskStatus.CANCELLED);
        verify(taskRepository, times(1)).findActiveTasks();
    }
}