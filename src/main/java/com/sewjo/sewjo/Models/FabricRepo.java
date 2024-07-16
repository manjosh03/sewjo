package com.sewjo.sewjo.Models;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FabricRepo extends JpaRepository<Fabric, Integer> {
    Fabric findByIdAndUser(int id, User user);
    List<Fabric> findAll();
    List<Fabric> findAllByUser(User user);
    List<Fabric> findByName(String name);
    List<Fabric> findByProjectId(int projectId);
    Fabric findById(int id);
    void deleteById(int id);
}