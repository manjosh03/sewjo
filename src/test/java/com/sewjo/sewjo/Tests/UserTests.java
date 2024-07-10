package com.sewjo.sewjo.Tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.sewjo.sewjo.Controllers.UserController;
import com.sewjo.sewjo.Models.UserRepo;
import com.sewjo.sewjo.Models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(UserController.class)
public class UserTests {

    @Autowired
    private UserController userController;

    @MockBean
    private UserRepo userRepo;

    private User testUser;

    @BeforeEach
    void setUp() {
        // Initialize your test user, fabrics, and patterns here
        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        // Assume User class has methods to add fabrics and patterns
        // testUser.addFabric(new Fabric(...));
        // testUser.addPattern(new Pattern(...));
    }

    @Test
    void testAddUser() {
        when(userRepo.save(any(User.class))).thenReturn(testUser);
        // Assuming UserController has a method to add a user
        // String viewName = userController.addUser(testUser, mockModel, mockRequest, mockSession);
        // assertEquals("expectedViewName", viewName);
        // Verify userRepo.save was called
        verify(userRepo, times(1)).save(any(User.class));
    }
}
