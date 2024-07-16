package com.sewjo.sewjo.Models;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProjectRepo extends JpaRepository<Project,Integer> {
    List<Project> findAllByUser(User user);
    Project findById(int id);
}
