package com.example.checkmate.Repository;

import com.example.checkmate.Entity.Task;
import com.example.checkmate.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface TaskRepo extends JpaRepository<Task, Integer> {
    List<Task> findByUser(User user);
}
