package com.sewjo.sewjo.Controllers;

import java.util.List;
import java.util.Map;

import com.sewjo.sewjo.Models.UserRepo;
import com.sewjo.sewjo.Models.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import com.sewjo.sewjo.Models.Fabric;
import com.sewjo.sewjo.Models.FabricRepo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class FabricController {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private FabricRepo fabricRepo;

    @GetMapping("/fabric/view")
    public String getAllFabrics(HttpServletRequest request, Model model) {
        System.out.println("Getting all fabrics");
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        List<Fabric> fabrics = fabricRepo.findAllByUser(user);
        model.addAttribute("fabrics", fabrics);
        return "fabric/showAll"; // Ensure this matches the Thymeleaf template name
    }

    @GetMapping("/fabric/add-page")
    public String showAddFabricPage() {
        return "fabric/addFabric";
    }

    @PostMapping("/fabric/add")
    public String addFabric(@RequestParam Map<String, String> newfabric, HttpServletResponse response, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId != null) {
            String name = newfabric.get("name");
            String type = newfabric.get("type");
            int price = Integer.parseInt(newfabric.get("price"));
            int width = Integer.parseInt(newfabric.get("width"));
            int height = Integer.parseInt(newfabric.get("height"));
            String colour = newfabric.get("colour");
            User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            Fabric fabric = new Fabric(name, colour, width, height, price, type, user);
            fabricRepo.save(fabric);
        }
        else{
            response.setStatus(401);
            return "redirect:/login";
        }
        response.setStatus(201);
        return "redirect:/fabric/view";
    }

    @PostMapping("/fabric/delete")
    public String deleteFabric(@RequestParam("id") int id, HttpServletResponse response) {
        System.out.println("DELETE fabric "+ id);
        fabricRepo.deleteById(id);
        response.setStatus(200);
        return "redirect:/fabric/view";
    }

    @GetMapping("/fabric/{id}")
    public String getFabricDetail(@PathVariable("id") int id, Model model) {
        Fabric fabric = fabricRepo.findById(id);
        model.addAttribute("fabric", fabric);
        return "fabric/details";
    }
//
//    @PutMapping("/fabric/update/{id}")
//    public void updateAttribute(@PathVariable("id") int id, @RequestBody Map<String, String> updates,
//                                HttpServletResponse response) {
//        // Fabric fabric = fabricRepo.findById(id);
//
//        // updates.forEach((key, val) -> {
//        // switch (key) {
//        // case "name":
//        // fabric.setName(val);
//        // break;
//
//        // case "width":
//        // fabric.setWidth(Integer.parseInt(val));
//        // break;
//
//        // case "height":
//        // fabric.setHeight(Integer.parseInt(val));
//        // break;
//
//        // case "color":
//        // fabric.setColor(val);
//        // break;
//        // }
//
//        // });
//
//        // fabricRepo.save(fabric);
//        response.setStatus(200);
//
//    }
}
