package com.sewjo.sewjo.models;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findBySize(int size);

    List<User> findByNameAndPassword(String name, String Password); // this gives a list of users by name and password
    // if the name and password matches in the list then that means we have that
    // user as a registered member already, if it gives us null then the user needs
    // to sign up first
}
