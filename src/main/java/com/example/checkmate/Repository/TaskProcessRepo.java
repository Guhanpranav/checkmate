package com.example.checkmate.Repository;

import com.example.checkmate.Entity.Task;
import com.example.checkmate.Entity.TaskProgress;
import com.example.checkmate.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskProcessRepo extends JpaRepository<TaskProgress, Integer> {
    List<TaskProgress> findByTaskIn(List<Task> task);


    List<TaskProgress> findByTaskInAndCompleted(List<Task> task, boolean completed);

    List<TaskProgress> findByTaskInAndDatedBetween(List<Task> tasks, LocalDateTime startDate, LocalDateTime endDate);

    TaskProgress findByTaskAndDatedBetween(Task task, LocalDateTime start, LocalDateTime end);

    List<TaskProgress> findByTask(Task task);
}
