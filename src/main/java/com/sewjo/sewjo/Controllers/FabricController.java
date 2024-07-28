package com.sewjo.sewjo.Controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.sewjo.sewjo.Interfaces.FabricInterface;
import com.sewjo.sewjo.Models.*;
import com.sewjo.sewjo.Services.FileStorageService;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class FabricController {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private FabricRepo fabricRepo;

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/fabric/view")
    public String getAllFabrics(HttpServletRequest request, HttpServletResponse response, Model model) {
        System.out.println("Getting all fabrics");
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            response.setStatus(401);
            return "redirect:/login";
        }
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        List<Fabric> fabrics = fabricRepo.findAllByUser(user);
        model.addAttribute("fabrics", fabrics);
        List<String> fabricTypes = FabricInterface.getFabricTypes();
        model.addAttribute("fabricTypes", fabricTypes);
        response.setStatus(200);
        return "fabric/showAll"; // Ensure this matches the Thymeleaf template name
    }

    @GetMapping("/fabric/add-page")
    public String showAddFabricPage(Model model, HttpServletResponse response) {
        List<String> fabricTypes = FabricInterface.getFabricTypes();
        model.addAttribute("fabricTypes", fabricTypes);
        response.setStatus(200);
        return "fabric/addFabric";
    }

    @GetMapping("/fabric/edit-page")
    public String showEditPatternPage(@RequestParam("id") int id, Model model, HttpServletResponse response) {
        List<String> fabricTypes = FabricInterface.getFabricTypes();
        model.addAttribute("fabricTypes", fabricTypes); // Corrected typo
        Fabric fabric = fabricRepo.findById(id);
        model.addAttribute("fabric", fabric);
        response.setStatus(200);
        return "fabric/editFabric";
    }

    @PostMapping("/fabric/add")
    public String addFabric(@RequestParam Map<String, String> newFabric, @RequestParam("file") MultipartFile file,
            HttpServletResponse response, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId != null) {
            String name = newFabric.get("name");
            String type = newFabric.get("type");
            int price = Integer.parseInt(newFabric.get("price"));
            int width = Integer.parseInt(newFabric.get("width"));
            int height = Integer.parseInt(newFabric.get("height"));
            String image;
            String colour = newFabric.get("colour");
            User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

            try {
                String fileUrl = fileStorageService.uploadFile(file);
                image = fileUrl;

            } catch (IOException e) {
                image = "https://via.placeholder.com/150";
            }

            Fabric fabric = new Fabric(name, colour, width, height, price, type, user, image);
            fabricRepo.save(fabric);
        } else {
            response.setStatus(401);
            return "redirect:/login";
        }
        response.setStatus(201);
        return "redirect:/fabric/view";
    }

    @PostMapping("/fabric/delete")
    public String deleteFabric(@RequestParam("id") int id, HttpServletResponse response, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        Fabric fabric = fabricRepo.findById(id);
        if (fabric == null) {
            response.setStatus(404);
            return "redirect:/fabric/view";
        }
        if (fabric.getUser().getId() != userId) {
            response.setStatus(401);
            return "redirect:/fabric/view";
        }
        System.out.println("DELETE fabric " + id);
        fabricRepo.deleteById(id);
        response.setStatus(200);
        return "redirect:/fabric/view";
    }

    @GetMapping("/fabric/{id}")
    public String getFabricDetail(@PathVariable("id") int id, Model model, HttpServletResponse response,
            HttpServletRequest request) {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            response.setStatus(401);
            return "redirect:/login";
        }
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Fabric fabric = fabricRepo.findByIdAndUser(id, user);
        model.addAttribute("fabric", fabric);
        response.setStatus(200);
        return "fabric/details";

    }

    @PostMapping("/fabric/update")
    public String updateFabric(@RequestParam Map<String, String> updatedFabric,
            @RequestParam("file") MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            response.setStatus(401);
            return "redirect:/login";
        }
        int id = Integer.parseInt(updatedFabric.get("id"));
        Fabric fabric = fabricRepo.findById(id);
        if (fabric.getUser().getId() != userId) {
            response.setStatus(401);
            return "redirect:/fabric/view";
        }
        fabric.setName(updatedFabric.get("name"));
        fabric.setColour(updatedFabric.get("colour"));
        fabric.setWidth(Integer.parseInt(updatedFabric.get("width")));
        fabric.setHeight(Integer.parseInt(updatedFabric.get("height")));
        fabric.setPrice(Integer.parseInt(updatedFabric.get("price")));
        fabric.setType(updatedFabric.get("type"));

        if (file != null && !file.isEmpty()) {
            try {
                String fileUrl = fileStorageService.uploadFile(file);
                fabric.setImage(fileUrl);
            } catch (IOException e) {
                response.setStatus(500);
                return "error";
            }
        }

        fabricRepo.save(fabric);
        response.setStatus(200);
        return "redirect:/fabric/view";
    }
}
