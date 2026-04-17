package com.denzil.taskapi.repository;

import com.denzil.taskapi.entity.Task;
import com.denzil.taskapi.entity.Task.TaskPriority;
import com.denzil.taskapi.entity.Task.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByStatus(TaskStatus status);

    List<Task> findByPriority(TaskPriority priority);

    List<Task> findByTitleContainingIgnoreCase(String title);

    List<Task> findByStatusAndPriority(
            TaskStatus status, TaskPriority priority);

    @Query("SELECT t FROM Task t WHERE t.status != 'DONE' " +
           "AND t.status != 'CANCELLED' " +
           "ORDER BY t.priority DESC, t.createdAt ASC")
    List<Task> findActiveTasks();

    long countByStatus(TaskStatus status);

    boolean existsByTitleAndStatus(String title, TaskStatus status);
}