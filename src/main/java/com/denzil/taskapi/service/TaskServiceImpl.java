package com.denzil.taskapi.service;

import com.denzil.taskapi.entity.Task;
import com.denzil.taskapi.entity.Task.TaskPriority;
import com.denzil.taskapi.entity.Task.TaskStatus;
import com.denzil.taskapi.exception.TaskNotFoundException;
import com.denzil.taskapi.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Task> getAllTasks() {
        log.debug("Fetching all tasks");
        return taskRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Task getTaskById(Long id) {
        log.debug("Fetching task with id: {}", id);
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    @Override
    public Task createTask(Task task) {
        log.debug("Creating task: {}", task.getTitle());
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(Long id, Task taskDetails) {
        log.debug("Updating task with id: {}", id);
        Task existing = getTaskById(id);
        existing.setTitle(taskDetails.getTitle());
        existing.setDescription(taskDetails.getDescription());
        existing.setStatus(taskDetails.getStatus());
        existing.setPriority(taskDetails.getPriority());
        existing.setDueDate(taskDetails.getDueDate());
        return taskRepository.save(existing);
    }

    @Override
    public Task updateTaskStatus(Long id, TaskStatus status) {
        log.debug("Updating status of task {} to {}", id, status);
        Task existing = getTaskById(id);
        existing.setStatus(status);
        return taskRepository.save(existing);
    }

    @Override
    public void deleteTask(Long id) {
        log.debug("Deleting task with id: {}", id);
        Task existing = getTaskById(id);
        taskRepository.delete(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> getTasksByStatus(TaskStatus status) {
        log.debug("Fetching tasks by status: {}", status);
        return taskRepository.findByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> getTasksByPriority(TaskPriority priority) {
        log.debug("Fetching tasks by priority: {}", priority);
        return taskRepository.findByPriority(priority);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> searchTasksByTitle(String title) {
        log.debug("Searching tasks by title: {}", title);
        return taskRepository.findByTitleContainingIgnoreCase(title);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> getActiveTasks() {
        log.debug("Fetching active tasks");
        return taskRepository.findActiveTasks();
    }
}