package com.sewjo.sewjo.Controllers;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.sewjo.sewjo.Models.Fabric;
import com.sewjo.sewjo.Models.Pattern;
import com.sewjo.sewjo.Models.Project;

@Controller
public class PageController {

    @GetMapping("/")
    public String index(Model model) {
        return "redirect:/Test.html";
    }


}
