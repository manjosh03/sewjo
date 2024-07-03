package com.sewjo.sewjo.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.bind.annotation.PostMapping;

import com.sewjo.sewjo.models.User;
import com.sewjo.sewjo.models.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/") // we need to direct it to login page
    public RedirectView process() {
        return new RedirectView("login");
    }

    @PostMapping("/users/add") // This is when user signs up in the form
    public String addUser(@RequestParam Map<String, String> newuser, HttpServletResponse response) {
        System.out.println("ADD user");
        String newName = newuser.get("name");
        String newEmail = newuser.get("email");
        String newPwd = newuser.get("password");
        String newPhoneNumber = newuser.get("phoneNumber");
        userRepository.save(new User(newName, newEmail, newPwd, newPhoneNumber));
        response.setStatus(201);
        return "users/addedUser";
    }

    @GetMapping("/login") // This is getmapping when we enter in the url to go to login page
    public String getLogin(Model model, HttpServletRequest request, HttpSession session) {
        User user = (User) session.getAttribute("session_user");
        if (user == null) {
            return "users/login"; // this is the case when user has not logged in, show the page for log in
        } else {
            model.addAttribute("user", user);
            return "users/protected"; // this is accessible for users who are logged in, need to change the location
                                      // as we don't have the folder users under templates and no login.html we also
                                      // don't have the protected.html file which will show the features that the
                                      // registered users have we can redirect the user to the fabric page
        } // Above will either take me to the page of login/signUp or will take me to the
          // page where user can do smthn when logged in
    }

    @PostMapping("/login")
    public String login(@RequestParam Map<String, String> formData, Model model, HttpServletRequest request,
            HttpSession session) {
        // processing login, when user hits the sign in button
        String name = formData.get("name");
        String pwd = formData.get("password");
        List<User> userlist = userRepository.findByNameAndPassword(name, pwd);
        if (userlist.isEmpty()) {
            return "users/login"; // re direct back to login if they enter the wrong password or username
        } else {
            // success
            User user = userlist.get(0); // getting which user it is who logged in
            request.getSession().setAttribute("session_user", user);
            model.addAttribute("user", user); // user logged in
            return "users/protected";
        }
    }

    @GetMapping("/logout")
    public String destroySession(HttpServletRequest request) {
        request.getSession().invalidate();
        return "/users/login"; // Go back to the initial login page
    }
}
