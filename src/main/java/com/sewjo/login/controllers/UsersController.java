package com.sewjo.login.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sewjo.login.models.User;
import com.sewjo.login.models.UserReopsitory;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class UsersController {
    @Autowired
    private UserReopsitory userRepo;

    @GetMapping("/login")
    public String getLogin(Model model, HttpServletRequest request, HttpSession session) {
        User user = (User) session.getAttribute("session_user");
        if (user == null) {
            return "users/login";
        } else {
            model.addAttribute("user", user);
            return "fabric/addFabric"; // Redirect to the React app
        }
    }

    @PostMapping("/login")
    public String login(@RequestParam Map<String, String> formData, Model model, HttpServletRequest request,
            HttpSession session) {
        String email = formData.get("email");
        String password = formData.get("password");
        List<User> userList = userRepo.findByEmailAndPassword(email, password);
        if (userList.isEmpty()) {
            model.addAttribute("loginError", "Invalid email or password");
            return "users/login";
        } else {
            User user = userList.get(0);
            request.getSession().setAttribute("session_user", user);
            model.addAttribute("user", user);
            return "fabric/addFabric";  // Redirect to the protected page
        }
    }

    @PostMapping("/users/add")
    public String addUser(@RequestParam Map<String, String> newUser, Model model) {
        System.out.println("Add User");
        String newName = newUser.get("name");
        String newEmail = newUser.get("email");
        String newPassword = newUser.get("password");
        userRepo.save(new User(newName, newPassword, newEmail));
        model.addAttribute("message", "User added successfully");
        return "users/addedUser";
    }
}
