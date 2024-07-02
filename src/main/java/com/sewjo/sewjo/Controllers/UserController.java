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

    @GetMapping("/loginPage")
    public String getLogin(Model model, HttpServletRequest request, HttpSession session){
        User user = (User) session.getAttribute("session_user");
        if (user == null){
            return "/loginPage";
        }
        else {
            model.addAttribute("user",user);
            return "Test.html";
        }
    }

    @PostMapping("/loginPage")
    public String login(@RequestParam Map<String,String> formData, Model model, HttpServletRequest request, HttpSession session){
        String email = formData.get("email");
        String password = formData.get("password");
        List<User> userlist = userRepo.findByEmailAndPassword(email, password);
        if (userlist.isEmpty()){
            return "/loginPage";
        }
        else {
            User user = userlist.get(0);
            request.getSession().setAttribute("session_user", user);
            model.addAttribute("user", user);
            return "Test.html";
        }
    }
}
