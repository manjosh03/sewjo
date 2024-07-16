package com.sewjo.sewjo.Controllers;

import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.sewjo.sewjo.Models.Fabric;
import com.sewjo.sewjo.Models.FabricRepo;
import com.sewjo.sewjo.Models.Pattern;
import com.sewjo.sewjo.Models.Project;
import com.sewjo.sewjo.Models.User;
import com.sewjo.sewjo.Models.UserRepo;

@Controller
public class PageController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private FabricRepo fabricRepo;

    @GetMapping("/")
    public String index(Model model) {
        return "redirect:/login";
    }

    @GetMapping("/homepage/view")
    public String getAllFabrics(HttpServletRequest request, Model model) {
        System.out.println("view homepage");
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        List<Fabric> fabrics = fabricRepo.findAllByUser(user);
        // List<Pattern> patterns = patternRepo.findAllByUser(user);
        // List<Project> projects = projectRepo.findAllByUser(user);
        model.addAttribute("fabrics", fabrics);
        // model.addAttribute("patterns", patterns);
        // model.addAttribute("projects", projects);
        return "homepage/Homepage"; // Ensure this matches the Thymeleaf template name
    }

}
