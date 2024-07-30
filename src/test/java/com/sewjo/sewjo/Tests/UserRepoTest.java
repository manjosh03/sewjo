package com.sewjo.sewjo.Tests;

import com.sewjo.sewjo.Models.UserRepo;
import com.sewjo.sewjo.Models.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserRepoTest {

    @Autowired
    private UserRepo userRepo;

    @BeforeEach
    @Rollback(false)
    public void setUp() {
        User user1 = new User("John Doe", "password123", "john.doe@example.com", "profilePic.jpg");
        User user2 = new User("Jane Doe", "password456", "jane.doe@example.com", "profilePic2.jpg");
        userRepo.save(user1);
        userRepo.save(user2);
    }

    @Test
    public void testFindByEmailAndPassword() {
        List<User> users = userRepo.findByEmailAndPassword("john.doe@example.com", "password123");
        assertEquals(1, users.size());
        assertEquals("John Doe", users.get(0).getName());

        users = userRepo.findByEmailAndPassword("jane.doe@example.com", "password456");
        assertEquals(1, users.size());
        assertEquals("Jane Doe", users.get(0).getName());

        users = userRepo.findByEmailAndPassword("nonexistent@example.com", "password");
        assertTrue(users.isEmpty());
    }
}