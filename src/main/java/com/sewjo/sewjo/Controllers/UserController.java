package com.sewjo.sewjo.Controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.sewjo.sewjo.Services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import com.sewjo.sewjo.Models.User;
import com.sewjo.sewjo.Models.UserRepo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/myProfile/uploadProfilePicture")
    public String uploadProfilePicture(@RequestParam("file") MultipartFile file, HttpSession session, Model model) {
        User user = (User) session.getAttribute("session_user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            String fileUrl = fileStorageService.uploadFile(file);
            user.addProfilePicture(fileUrl);
            userRepo.save(user);
            model.addAttribute("user", user);
            return "myProfile/profile";
        } catch (IOException e) {
            model.addAttribute("uploadError", "File upload failed");
            return "myProfile/profile";
        }
    }

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
        String newProfilePicture = "https://via.placeholder.com/150";
        userRepo.save(new User(newName, newPassword, newEmail, newProfilePicture));
        model.addAttribute("message", "User added successfully");
        return "users/addedUser";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, Model model) {
        model.addAttribute("user", null);
        request.getSession().invalidate();
        return "redirect:/sewjohome.html";
    }

    @GetMapping("/myProfile/reload")
    public String reloadProfile(HttpSession session) {
        User user = (User) session.getAttribute("session_user");
        if (user == null) {
            return "/login";
        }
        return "/myProfile/profile";
    }

    @GetMapping("myProfile/profile")
    public String getProfile(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("session_user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "myProfile/profile";
    }

    @PostMapping("/myProfile/updateBio")
    public String updateBio(@RequestParam("bio") String bio, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("session_user");
        if (user == null) {
            return "redirect:/login";
        }
        user.addBio(bio);
        userRepo.save(user); // Update the user in the database
        model.addAttribute("user", user);
        return "redirect:/myProfile/profile";
    }

    @PostMapping("/myProfile/updateName")
    public String updateName(@RequestParam("name") String name, HttpSession session, Model model) {
        User user = (User) session.getAttribute("session_user");
        if (user == null) {
            return "redirect:/login";
        }
        user.setName(name);
        userRepo.save(user);
        model.addAttribute("user", user);
        return "redirect:/myProfile/profile";
    }

    @PostMapping("/myProfile/updatePassword")
    public String updatePassword(@RequestParam("password") String password, HttpSession session, Model model) {
        User user = (User) session.getAttribute("session_user");
        if (user == null) {
            return "redirect:/login";
        }
        user.setPassword(password);
        userRepo.save(user);
        model.addAttribute("user", user);
        return "redirect:/myProfile/profile";
    }
}
