package com.example.checkmate.Controller;


import com.example.checkmate.Entity.Task;
import com.example.checkmate.Entity.TaskProgress;
import com.example.checkmate.Entity.User;
import com.example.checkmate.Repository.TaskProcessRepo;
import com.example.checkmate.Repository.TaskRepo;
import com.example.checkmate.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepo userRepository;

    @Autowired
    TaskRepo taskRepository;

    @Autowired
    TaskProcessRepo tpr;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/createAccount")
    public String createAcc(){
        return "createAccount";
    }

    @GetMapping("/addTask")
    public String addTaskHtml(){
        return "addTask";
    }

    @GetMapping("/api/v1/home")
    public String home(@AuthenticationPrincipal User user, Model model){
        String name = user.getName();
        String email = user.getEmail();

        List<LocalDate> last10Days = new ArrayList<>();

        for(int i=0; i<10; i++){
            last10Days.add(LocalDate.now().minusDays(i));
        }

        LocalDateTime startDate = LocalDate.now().minusDays(10).atStartOfDay();
        LocalDateTime endDate = LocalDate.now().atTime(23, 59, 59);

        List<Task> tasks = taskRepository.findByUser(user);
        List<TaskProgress> progressList = tpr.findByTaskInAndDatedBetween(tasks, startDate, endDate);

        Map<Integer, List<LocalDate>> map= new HashMap<>();

        for(Task task : tasks){
            List<LocalDate> info = progressList.stream().filter(pl -> (pl.getTask().getId() == task.getId()) && (pl.isCompleted())).map(pl -> pl.getDated().toLocalDate()).toList();
            map.put(task.getId(), info);
        }

        model.addAttribute("tasks", tasks);
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("last10Days", last10Days);
        model.addAttribute("completedDatesMap", map);

        return "home";
    }

    @GetMapping("/api/v1/deleteTask")
    public String deleteTask(@RequestParam int taskId, Model model){
        Task task = taskRepository.findById(taskId).orElseThrow();
        tpr.deleteAll(tpr.findByTask(task));
        taskRepository.delete(task);
        return "redirect:/api/v1/home";
    }

    @PostMapping("/api/v1/addTask")
    public String addTask(@ModelAttribute Task task, @AuthenticationPrincipal User user){
        task.setUser(user);
        taskRepository.save(task);
        return "redirect:/api/v1/home";
    }

    @PostMapping("/api/v1/createAccount")
    public String createAccount(@ModelAttribute User user, Model model){

        if(userRepository.findByName(user.getName()) != null){
            model.addAttribute("error", "Username already exists! Please choose a different username.");
            return "createAccount"; // ← go back to createAccount page
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "login";
    }
}
