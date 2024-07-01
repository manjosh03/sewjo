package com.sewjo.sewjo.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sewjo.sewjo.models.Fabric;

@Controller
public class FabricController {
    @GetMapping("/fabric/view")
    public String getAllFabrics(Model model) {
        System.out.println("Getting all fabrics");

        // Todo: get all fabrics from database
        // making a list for a test purpose for now
        List<Fabric> fabrics = new ArrayList<>();
        fabrics.add(new Fabric("fabric1", "Blue", 20, 30, 10, "type1"));
        fabrics.add(new Fabric("fabric2", "Red", 500, 200, 50, "type2"));
        fabrics.add(new Fabric("fabric3", "Yellow", 150, 310, 40, "type3"));

        model.addAttribute("fb", fabrics);
        return "fabric/showAll";
    }

}
