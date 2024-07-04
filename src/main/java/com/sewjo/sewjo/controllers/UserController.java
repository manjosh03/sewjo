package com.sewjo.sewjo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sewjo.sewjo.Dto.UserDTO;
import com.sewjo.sewjo.Repo.UserRepository;
import com.sewjo.sewjo.Service.UserService;

@RestController
@CrossOrigin
@RequestMapping("api/v1/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    // @GetMapping("/") // we need to direct it to login page
    // public RedirectView process() {
    // return new RedirectView("login");
    // }

    @PostMapping(path = "/save") // This is when user signs up in the form
    public String saveUser(@RequestBody UserDTO userDTO) {

        String id = userService.addUser(userDTO);
        return id;
    }

}
