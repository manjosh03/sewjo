package com.sewjo.login.models;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserReopsitory extends JpaRepository<User, Integer> {
    List<User> findByEmailAndPassword(String email, String password);

}
