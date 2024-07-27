package com.sewjo.sewjo.Controllers;

import java.util.List;
import java.util.Map;

import com.sewjo.sewjo.Interfaces.PatternInterface;
import com.sewjo.sewjo.Models.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class PatternController {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PatternRepo patternRepo;

    @GetMapping("/pattern/view")
    public String getAllPatterns(HttpServletRequest request, HttpServletResponse response, Model model) {
        System.out.println("Getting all patterns");
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            response.setStatus(401);
            return "redirect:/login";
        }
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        List<Pattern> patterns = patternRepo.findAllByUser(user);
        model.addAttribute("patterns", patterns);
        List<String> patternTypes = PatternInterface.getPatternTypes();
        model.addAttribute("patternTypes", patternTypes);
        response.setStatus(200);
        return "pattern/showAll"; // Ensure this matches the Thymeleaf template name
    }

    @GetMapping("/pattern/add-page")
    public String showAddPatternPage(Model model, HttpServletResponse response) {
        List<String> patternTypes = PatternInterface.getPatternTypes();
        model.addAttribute("patternTypes", patternTypes);
        response.setStatus(200);
        return "pattern/addPattern";
    }

    @GetMapping("/pattern/edit-page")
    public String showEditPatternPage(@RequestParam("id") int id, Model model, HttpServletResponse response) {
        List<String> patternTypes = PatternInterface.getPatternTypes();
        model.addAttribute("patternTypes", patternTypes);
        Pattern pattern = patternRepo.findById(id);
        model.addAttribute("pattern", pattern);
        response.setStatus(200);
        return "pattern/editPattern";
    }

    @PostMapping("/pattern/add")
    public String addPattern(@RequestParam Map<String, String> newPattern, HttpServletResponse response, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId != null) {
            String name = newPattern.get("name");
            String type = newPattern.get("type");
            String description = newPattern.get("description");
            String image = newPattern.get("image");
            int price = Integer.parseInt(newPattern.get("price"));
            User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            if (image == null) {
                image = "https://via.placeholder.com/150";
            }
            Pattern pattern = new Pattern(name, type, description, image, price, user);
            patternRepo.save(pattern);
        }
        else{
            response.setStatus(401);
            return "redirect:/login";
        }
        response.setStatus(201);
        return "redirect:/pattern/view";
    }

    @PostMapping("/pattern/delete")
    public String deletePattern(@RequestParam("id") int id, HttpServletResponse response, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        Pattern pattern = patternRepo.findById(id);
        if (pattern == null) {
            response.setStatus(404);
            return "redirect:/pattern/view";
        }
        if (pattern.getUser().getId() != userId) {
            response.setStatus(401);
            return "redirect:/pattern/view";
        }
        System.out.println("DELETE pattern "+ id);
        patternRepo.deleteById(id);
        response.setStatus(200);
        return "redirect:/pattern/view";
    }

    @GetMapping("/pattern/{id}")
    public String getPatternDetail(@PathVariable("id") int id, Model model, HttpServletResponse response, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            response.setStatus(401);
            return "redirect:/login";
        }
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Pattern pattern = patternRepo.findByIdAndUser(id, user);
        model.addAttribute("pattern", pattern);
        response.setStatus(200);
        return "pattern/details";
    }

    @PostMapping("/pattern/update")
    public String updatePattern(@RequestParam Map<String, String> updatedPattern, HttpServletResponse response, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            response.setStatus(401);
            return "redirect:/login";
        }
        int id = Integer.parseInt(updatedPattern.get("id"));
        Pattern pattern = patternRepo.findById(id);
        if (pattern.getUser().getId() != userId) {
            response.setStatus(401);
            return "redirect:/login";
        }
        pattern.setName(updatedPattern.get("name"));
        pattern.setType(updatedPattern.get("type"));
        pattern.setDescription(updatedPattern.get("description"));
        pattern.setPrice(Integer.parseInt(updatedPattern.get("price")));
        String image = updatedPattern.get("image");
        if (image == null) {
            image = "https://via.placeholder.com/150";
        }
        pattern.setImage(image);
        patternRepo.save(pattern);
        response.setStatus(200);
        return "redirect:/pattern/view";
    }
}
