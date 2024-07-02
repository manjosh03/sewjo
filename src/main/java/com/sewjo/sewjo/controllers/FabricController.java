package com.sewjo.sewjo.controllers;

import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;

import com.sewjo.sewjo.models.Fabric;
import com.sewjo.sewjo.models.FabricRepo;



import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/fabric")
@CrossOrigin
public class FabricController {

    @Autowired
    private FabricRepo fabricRepo;
    
    // making a list for a test purpose for now
    List<Fabric> fabrics = new ArrayList<>();

    @GetMapping("/view")
    public List<Fabric> getAllFabrics() {
        System.out.println("Getting all fabrics");

        // Todo: get all fabrics from database
        fabrics.add(new Fabric("fabric1", "Blue", 20, 30, 10, "type1"));
        fabrics.add(new Fabric("fabric2", "Red", 500, 200, 50, "type2"));
        fabrics.add(new Fabric("fabric3", "Yellow", 150, 310, 40, "type3"));
        // List<Rectangle> fabrics = fabricRepo.findAll();

        
        return fabrics;
    }

    @PostMapping("/add")
    public Fabric addFabric(@RequestBody Fabric newFabric) {
        System.out.println("Adding fabric");

        // For test
        fabrics.add(newFabric);

        // Save to the repository (uncomment when using the database)
        // fabricRepo.save(newFabric);

        return newFabric;
    }

}
