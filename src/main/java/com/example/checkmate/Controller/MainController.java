package com.example.checkmate.Controller;

import com.example.checkmate.Entity.User;
import com.example.checkmate.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class MainController {

    @Autowired
    UserRepo userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/test")
    public String getMessage(){
        return "Hello, Welcome ...!";
    }

    @PostMapping("/createAccount")
    public ResponseEntity<String> createAccount(@RequestBody User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("Account created successfully for " + user.getName());
    }
}
