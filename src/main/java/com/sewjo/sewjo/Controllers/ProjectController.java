package com.sewjo.sewjo.Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.sewjo.sewjo.Models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class ProjectController {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProjectRepo projectRepo;

    @Autowired
    private FabricRepo fabricRepo;

    @Autowired
    private PatternRepo patternRepo;

    @GetMapping("/project/view")
    public String getAllProjects(HttpServletRequest request, Model model) {
        System.out.println("Getting all projects");
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        List<Project> projects = projectRepo.findAllByUser(user);
        model.addAttribute("projects", projects);
        return "project/showAll"; // Ensure this matches the Thymeleaf template name
    }

    @GetMapping("/project/{id}")
    public String getProjectDetail(@PathVariable("id") int id, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        Project project = projectRepo.findById(id);
        if (project == null) {
            return "redirect:/project/view";
        }
        if (project.getUser().getId() != userId) {
            return "redirect:/project/view";
        }
        List<Integer> FabricIds = project.getFabricIds();
        List<Fabric> fabrics = new ArrayList<>();
        for (int FabricId : FabricIds) {
            Fabric fabric = fabricRepo.findById(FabricId);
            fabrics.add(fabric);
        }
        model.addAttribute("projectFabrics", fabrics);
        List<Integer> PatternIds = project.getPatternIds();
        List<Pattern> patterns = new ArrayList<>();
        for (int PatternId : PatternIds) {
            Pattern pattern = patternRepo.findById(PatternId);
            patterns.add(pattern);
        }
        model.addAttribute("projectPatterns", patterns);
        return "fabric/details";
    }

}
