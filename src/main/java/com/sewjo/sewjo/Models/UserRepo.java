package com.sewjo.sewjo.Models;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Integer> {
    List<User> findByEmailAndPassword(String email, String password);
}