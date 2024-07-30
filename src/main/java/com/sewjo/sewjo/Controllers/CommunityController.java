package com.sewjo.sewjo.Controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import com.sewjo.sewjo.Models.Project;
import com.sewjo.sewjo.Models.ProjectRepo;
import com.sewjo.sewjo.Models.UserRepo;

import java.util.List;

@Controller
public class CommunityController {

    @Autowired
    private ProjectRepo projectRepo;

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/community/view")
    public String getAllCommunityProjects(HttpServletRequest request, HttpServletResponse response, Model model) {
        System.out.println("Getting all community projects");
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            response.setStatus(401);
            return "redirect:/login";
        }

        List<Project> projects = projectRepo.findAllByShared(true);
        model.addAttribute("projects", projects);

        return "community/showAll";
    }
}
