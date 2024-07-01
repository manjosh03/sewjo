package com.sewjo.sewjo.models;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

//for database
public interface FabricRepo extends JpaRepository<Fabric, Integer> {
    List<Fabric> findAll();

    List<Fabric> findByName(String name);

    Fabric findById(int id);

    void deleteById(int id);
}