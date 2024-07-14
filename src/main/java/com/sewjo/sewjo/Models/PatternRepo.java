package com.sewjo.sewjo.Models;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PatternRepo extends JpaRepository<Pattern, Integer>{
    List<Pattern> findAll();
    List<Pattern> findByName(String name);
    List<Pattern> findByProjectId(int projectId);
    Pattern findById(int id);
    void deleteById(int id);
}
