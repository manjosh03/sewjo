package com.sewjo.login.controllers;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.sewjo.login.models.Fabric;
import com.sewjo.login.models.FabricRepo;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class FabricController {

    @Autowired
    private FabricRepo fabricRepo;
    // making a list for a test purpose for now
    //List<Fabric> fabrics = new ArrayList<>();

    @GetMapping("/fabric/view")
    public String getAllFabrics(Model model) {
        System.out.println("Getting all fabrics");

        // Todo: get all fabrics from database
        List<Fabric> fabrics = fabricRepo.findAll();

        model.addAttribute("fb", fabrics);
        return "fabric/showAll"; // Ensure this matches the Thymeleaf template name
    }

    @GetMapping("/fabric/add-page")
    public String showAddFabricPage() {
        return "fabric/addFabric";
    }

    @PostMapping("/fabric/add")
    public String addFabric(@RequestParam Map<String, String> newfabric, HttpServletResponse response) {
        System.out.println("ADD fabric");
        String newName = newfabric.get("name");
        String newColor = newfabric.get("color");
        int newWidth = Integer.parseInt(newfabric.get("width"));
        int newHeight = Integer.parseInt(newfabric.get("height"));
        int newPrice = Integer.parseInt(newfabric.get("price"));
        String newType = newfabric.get("type");

        // for test
        //fabrics.add(new Fabric(newName, newColor, newWidth, newHeight, newPrice, newType));

        fabricRepo.save(new Fabric(newName, newColor, newWidth, newHeight, newPrice,
        newType));

        response.setStatus(201);

        return "redirect:/fabric/view"; // maybe show detail page
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

    @PutMapping("/fabric/update/{id}")
    public void updateAttribute(@PathVariable("id") int id, @RequestBody Map<String, String> updates,
            HttpServletResponse response) {
        Fabric fabric = fabricRepo.findById(id);

        updates.forEach((key, val) -> {
        switch (key) {
        case "name":
        fabric.setName(val);
        break;

        case "width":
        fabric.setWidth(Integer.parseInt(val));
        break;

        case "height":
        fabric.setHeight(Integer.parseInt(val));
        break;

        case "color":
        fabric.setColor(val);
        break;
        }

        });

        fabricRepo.save(fabric);
        response.setStatus(200);

    }

}