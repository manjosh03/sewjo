package com.sewjo.sewjo.Service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.sewjo.sewjo.Dto.UserDTO;
import com.sewjo.sewjo.Entity.User;
import com.sewjo.sewjo.Repo.UserRepository;
import com.sewjo.sewjo.Service.UserService;

public class UserIMPL implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String addUser(UserDTO userDTO) {

        // We have created an entity object user
        User user = new User(
                userDTO.getUserid(),
                userDTO.getUsername(),
                userDTO.getEmail(),
                this.passwordEncoder.encode(userDTO.getPassword()));

        userRepository.save(user);

        return user.getUsername();
    }

}
