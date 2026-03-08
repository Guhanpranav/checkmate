package com.example.checkmate.Repository;

import com.example.checkmate.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    public User findByEmail(String email);

    User findByName(String name);
}
