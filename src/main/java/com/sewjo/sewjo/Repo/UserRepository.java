package com.sewjo.sewjo.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.sewjo.sewjo.Entity.User;

@EnableJpaRepositories
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmailAndPassword(String email, String Password); // this gives a list of users by name and
                                                                          // password
    // if the name and password matches then that means we have that
    // user as a registered member already, if it gives us null then the user needs
    // to sign up first

    User findByEmail(String email);
}
