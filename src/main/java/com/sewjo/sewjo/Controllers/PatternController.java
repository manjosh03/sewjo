package com.sewjo.sewjo.Controllers;

import java.util.List;
import java.util.Map;

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
    public String getAllPatterns(HttpServletRequest request, Model model) {
        System.out.println("Getting all patterns");
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        List<Pattern> patterns = patternRepo.findAllByUser(user);
        model.addAttribute("patterns", patterns);
        return "pattern/showAll"; // Ensure this matches the Thymeleaf template name
    }

    @GetMapping("/pattern/add-page")
    public String showAddFabricPage() {
        return "pattern/addPattern";
    }

    @PostMapping("/pattern/add")
    public String addFabric(@RequestParam Map<String, String> newpattern, HttpServletResponse response, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId != null) {
            String name = newpattern.get("name");
            String type = newpattern.get("type");
            String description = newpattern.get("description");
            String image = newpattern.get("image");
            int price = Integer.parseInt(newpattern.get("price"));
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
    public String deleteFabric(@RequestParam("id") int id, HttpServletResponse response) {
        System.out.println("DELETE fabric "+ id);
        patternRepo.deleteById(id);
        response.setStatus(200);
        return "redirect:/pattern/view";
    }

    @GetMapping("/pattern/{id}")
    public String getFabricDetail(@PathVariable("id") int id, Model model) {
        Pattern pattern = patternRepo.findById(id);
        model.addAttribute("pattern", pattern);
        return "pattern/details";
    }
}
