package com.sewjo.sewjo.Models;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PatternRepo extends JpaRepository<Pattern, Integer>{
    List<Pattern> findAll();
    List<Pattern> findAllByUser(User user);
    Pattern findByIdAndUser(int id, User user);
    List<Pattern> findByProjectId(int projectId);
    Pattern findById(int id);
    void deleteById(int id);
    void deleteByIdAndUser(int id, User user);
}
