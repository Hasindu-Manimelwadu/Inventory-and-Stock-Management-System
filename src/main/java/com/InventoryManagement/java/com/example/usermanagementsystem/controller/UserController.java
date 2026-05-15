package com.example.usermanagementsystem.controller;

import com.example.usermanagementsystem.model.AdminUser;
import com.example.usermanagementsystem.model.StaffUser;
import com.example.usermanagementsystem.model.User;
import com.example.usermanagementsystem.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/users";
    }

    @GetMapping("/users")
    public String showUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "userList";
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "registerUser";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String email,
                               @RequestParam String password,
                               @RequestParam String role) {

        String userId = "USR-" + System.currentTimeMillis();

        User user;

        if (role.equalsIgnoreCase("ADMIN")) {
            user = new AdminUser(userId, username, email, password);
        } else {
            user = new StaffUser(userId, username, email, password);
        }

        userService.addUser(user);
        return "redirect:/users";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        Model model) {

        boolean success = userService.login(username, password);

        if (success) {
            return "redirect:/users";
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }

    @GetMapping("/editUser/{id}")
    public String editUser(@PathVariable String id, Model model) {
        model.addAttribute("user", userService.findUserById(id));
        return "updateUser";
    }

    @PostMapping("/updateUser")
    public String updateUser(@RequestParam String userId,
                             @RequestParam String username,
                             @RequestParam String email,
                             @RequestParam String password,
                             @RequestParam String role) {

        User user;

        if (role.equalsIgnoreCase("ADMIN")) {
            user = new AdminUser(userId, username, email, password);
        } else {
            user = new StaffUser(userId, username, email, password);
        }

        userService.updateUser(user);
        return "redirect:/users";
    }

    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }
}