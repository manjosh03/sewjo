package com.sewjo.sewjo.Controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import com.sewjo.sewjo.Models.User;
import com.sewjo.sewjo.Models.UserRepo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/login")
    public String getLogin(Model model, HttpServletRequest request, HttpSession session) {
        User user = (User) session.getAttribute("session_user");
        if (user == null) {
            return "users/login";
        } else {
            model.addAttribute("user", user);
            return "homepage/Homepage"; // Redirect to the React app
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
            request.getSession().setAttribute("userId", user.getId());
            model.addAttribute("user", user);
            return "homepage/Homepage"; // Redirect to the protected page
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

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, Model model) {
        model.addAttribute("user", null);
        request.getSession().invalidate();
        return "redirect:/sewjohome.html";
    }
}
