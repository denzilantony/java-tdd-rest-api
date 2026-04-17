package com.denzil.taskapi.service;

import com.denzil.taskapi.entity.Task;
import com.denzil.taskapi.entity.Task.TaskPriority;
import com.denzil.taskapi.entity.Task.TaskStatus;
import java.util.List;

public interface TaskService {

    List<Task> getAllTasks();

    Task getTaskById(Long id);

    Task createTask(Task task);

    Task updateTask(Long id, Task task);

    Task updateTaskStatus(Long id, TaskStatus status);

    void deleteTask(Long id);

    List<Task> getTasksByStatus(TaskStatus status);

    List<Task> getTasksByPriority(TaskPriority priority);

    List<Task> searchTasksByTitle(String title);

    List<Task> getActiveTasks();
}