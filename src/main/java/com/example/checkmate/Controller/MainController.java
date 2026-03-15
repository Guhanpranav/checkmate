package com.example.checkmate.Controller;

import com.example.checkmate.DTO.TaskProgressRequest;
import com.example.checkmate.Entity.Task;
import com.example.checkmate.Entity.TaskProgress;
import com.example.checkmate.Entity.User;
import com.example.checkmate.Repository.TaskProcessRepo;
import com.example.checkmate.Repository.TaskRepo;
import com.example.checkmate.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hibernate.internal.util.collections.ArrayHelper.forEach;

@RestController
@RequestMapping("/api/v1")
public class MainController {

    @Autowired
    UserRepo userRepository;

    @Autowired
    TaskRepo taskRepository;

    @Autowired
    TaskProcessRepo tpr;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/test")
    public String getMessage(){
        return "Hello, Welcome ...!";
    }



    @GetMapping("/createAccount")
    public String createAcc(){
        return "createAccount";
    }



    @PostMapping("/updateProgress")
    public void updateTaskProgress(@RequestBody TaskProgressRequest tpreq) {
        int taskId = tpreq.getTaskId();
        LocalDateTime start = tpreq.getDate().atStartOfDay();
        LocalDateTime end = tpreq.getDate().atTime(23, 59,  59);

        boolean status = tpreq.isCompleted();

        Task task = taskRepository.findById(taskId).orElseThrow();

        TaskProgress progress = tpr.findByTaskAndDatedBetween(task, start, end);

        if(status){
            if(progress == null){
                TaskProgress tempTaskProgress = new TaskProgress();
                tempTaskProgress.setTask(task);
                tempTaskProgress.setDated(start);
                tempTaskProgress.setCompleted(true);
                tpr.save(tempTaskProgress);
            }
        }else{
            if(progress != null){
                tpr.delete(progress);
            }
        }

    }



}
