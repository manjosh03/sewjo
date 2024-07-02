package com.sewjo.sewjo.Controllers;

import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import com.sewjo.sewjo.Models.Fabric;
import com.sewjo.sewjo.Models.FabricRepo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class FabricController {

    @Autowired
    private FabricRepo fabricRepo;

    @PostMapping("/fabric/addView")
    public String addFabricAddView(@RequestParam Map<String, String> fabric, HttpServletResponse response) {
        Fabric newFabric = new Fabric();
        newFabric.setName(fabric.get("name"));
        newFabric.setColour(fabric.get("colour"));
        newFabric.setWidth(Integer.parseInt(fabric.get("width")));
        newFabric.setHeight(Integer.parseInt(fabric.get("height")));
        newFabric.setPrice(Integer.parseInt(fabric.get("price")));
        newFabric.setType(fabric.get("type"));
        fabricRepo.save(newFabric);
        response.setStatus(HttpServletResponse.SC_CREATED);
        return "redirect:/Test.html";
    }

    @PostMapping("/fabric/add")
    public void addFabricAdd(@RequestParam Map<String, String> fabric, HttpServletResponse response) {
        Fabric newFabric = new Fabric();
        newFabric.setName(fabric.get("name"));
        newFabric.setColour(fabric.get("colour"));
        newFabric.setWidth(Integer.parseInt(fabric.get("width")));
        newFabric.setHeight(Integer.parseInt(fabric.get("height")));
        newFabric.setPrice(Integer.parseInt(fabric.get("price")));
        newFabric.setType(fabric.get("type"));
        fabricRepo.save(newFabric);
        response.setStatus(HttpServletResponse.SC_CREATED);
    }

    @GetMapping("/fabric/view")
    public String getAllFabrics(Model model) {
        List<Fabric> fabricList = fabricRepo.findAll();
        model.addAttribute("fabrics", fabricList);
        return "Fabrics/AllFabrics";
    }
}
