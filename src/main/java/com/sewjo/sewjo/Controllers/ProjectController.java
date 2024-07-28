package com.sewjo.sewjo.Controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.sewjo.sewjo.Models.*;
import com.sewjo.sewjo.Services.FileStorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    @Autowired
    private FileStorageService fileStorageService;

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

    // fix

    @GetMapping("/project/add-page")
    public String showAddProjectPage(Model model, HttpServletResponse response, HttpServletRequest request) {
        List<String> projectTypes = ProjectInterface.getProjectTypes();
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        List<Fabric> fabrics = fabricRepo.findAllByUser(user);
        List<Pattern> patterns = patternRepo.findAllByUser(user);

        model.addAttribute("projectTypes", projectTypes);
        model.addAttribute("fabrics", fabrics);
        model.addAttribute("patterns", patterns);

        response.setStatus(200);
        return "project/addProject";
    }

    @GetMapping("/project/edit-page")
    public String showEditProjectPage(@RequestParam("id") int id, Model model, HttpServletResponse response) {
        List<String> projectTypes = ProjectInterface.getProjectTypes();
        model.addAttribute("projectTypes", projectTypes);
        Project project = projectRepo.findById(id);
        model.addAttribute("project", project);
        response.setStatus(200);
        return "project/editProject";
    }

    @PostMapping("/project/add")
    public String addProject(@RequestParam Map<String, String> newProject, @RequestParam("file") MultipartFile file,
            HttpServletResponse response,
            HttpServletRequest request) {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId != null) {
            String name = newProject.get("name");
            String description = newProject.get("description");
            String image;
            User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            int fabricId = Integer.parseInt(newProject.get("fabricId"));
            int patternId = Integer.parseInt(newProject.get("patternId"));
            String type = newProject.get("type");
            boolean shared = newProject.containsKey("shared");
            int progress = Integer.parseInt(newProject.get("progress"));

            try {
                String fileUrl = fileStorageService.uploadFile(file);
                image = fileUrl;

            } catch (IOException e) {
                image = "https://via.placeholder.com/150";
            }

            Project project = new Project(name, description, image, user, type, fabricId, patternId, shared, progress);
            projectRepo.save(project);
        } else {
            response.setStatus(401);
            return "redirect:/login";
        }
        response.setStatus(201);
        return "redirect:/project/view";
    }

    @PostMapping("/project/delete")
    public String deleteproject(@RequestParam("id") int id, HttpServletResponse response, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        Project project = projectRepo.findById(id);
        if (project == null) {
            response.setStatus(404);
            return "redirect:/project/view";
        }
        if (project.getUser().getId() != userId) {
            response.setStatus(401);
            return "redirect:/project/view";
        }
        System.out.println("DELETE project " + id);
        projectRepo.deleteById(id);
        response.setStatus(200);
        return "redirect:/project/view";
    }

    // end

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
        // List<Integer> FabricIds = project.getFabricIds();
        List<Fabric> fabrics = new ArrayList<>();
        // for (int FabricId : FabricIds) {
        // Fabric fabric = fabricRepo.findById(FabricId);
        // fabrics.add(fabric);
        // }
        model.addAttribute("projectFabrics", fabrics);
        // List<Integer> PatternIds = project.getPatternIds();
        List<Pattern> patterns = new ArrayList<>();
        // for (int PatternId : PatternIds) {
        // Pattern pattern = patternRepo.findById(PatternId);
        // patterns.add(pattern);
        // }
        model.addAttribute("projectPatterns", patterns);
        return "fabric/details";
    }

}
