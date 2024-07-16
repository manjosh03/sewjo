package com.sewjo.sewjo.Models;

import java.util.List;

public interface ProjectRepo {
    List<Project> findAllByUser(User user);
}
