package com.sewjo.sewjo.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sewjo.sewjo.models.Fabric;
import com.sewjo.sewjo.models.FabricRepo;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class FabricController {

    @Autowired
    private FabricRepo fabricRepo;
    // making a list for a test purpose for now
    List<Fabric> fabrics = new ArrayList<>();

    @GetMapping("/fabric/view")
    public String getAllFabrics(Model model) {
        System.out.println("Getting all fabrics");

        // Todo: get all fabrics from database
        fabrics.add(new Fabric("fabric1", "Blue", 20, 30, 10, "type1"));
        fabrics.add(new Fabric("fabric2", "Red", 500, 200, 50, "type2"));
        fabrics.add(new Fabric("fabric3", "Yellow", 150, 310, 40, "type3"));
        // List<Rectangle> fabrics = fabricRepo.findAll();

        model.addAttribute("fb", fabrics);
        return "fabric/showAll";
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
        fabrics.add(new Fabric(newName, newColor, newWidth, newHeight, newPrice, newType));

        // fabricRepo.save(new Fabric(newName, newColor, newWidth, newHeight, newPrice,
        // newType));

        response.setStatus(201);

        return "redirect:/fabric/view"; // maybe show detail page
    }

}
