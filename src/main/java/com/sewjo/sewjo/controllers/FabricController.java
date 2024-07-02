package com.sewjo.sewjo.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    // Mock list of fabrics for testing
    List<Fabric> fabrics = new ArrayList<>();

    @GetMapping("/view")
    public List<Fabric> getAllFabrics() {
        System.out.println("Getting all fabrics");

        // Todo: get all fabrics from database
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

    @DeleteMapping("/delete/{id}")
    public void deleteFabric(@PathVariable int id) {
        
    }

}
