package com.sewjo.sewjo.Tests;

import com.sewjo.sewjo.Models.User;
import com.sewjo.sewjo.Models.UserRepo;
import com.sewjo.sewjo.Controllers.UserController;

import com.sewjo.sewjo.Services.FileStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserControllerTests {

    @Mock
    private UserRepo userRepo;

    @Mock
    private FileStorageService fileStorageService;

    @Mock
    private HttpSession session;

    @Mock
    private HttpServletRequest request;

    @Mock
    private Model model;

    @Mock
    private MultipartFile file;

    @InjectMocks
    private UserController userController;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User("John Doe", "password123", "john@example.com", "profilePic.jpg");
    }

    @Test
    void testUploadProfilePicture() throws Exception {
        when(session.getAttribute("session_user")).thenReturn(user);
        when(fileStorageService.uploadFile(file)).thenReturn("newProfilePic.jpg");

        String viewName = userController.uploadProfilePicture(file, session, model);

        verify(userRepo, times(1)).save(user);
        verify(model, times(1)).addAttribute("user", user);
        assertEquals("myProfile/profile", viewName);
        assertEquals("newProfilePic.jpg", user.getProfilePicture());
    }

    @Test
    void testUploadProfilePictureUserNotLoggedIn() {
        when(session.getAttribute("session_user")).thenReturn(null);

        String viewName = userController.uploadProfilePicture(file, session, model);

        assertEquals("redirect:/login", viewName);
    }

    @Test
    void testUploadProfilePictureIOException() throws Exception {
        when(session.getAttribute("session_user")).thenReturn(user);
        when(fileStorageService.uploadFile(file)).thenThrow(new IOException());

        String viewName = userController.uploadProfilePicture(file, session, model);

        verify(model, times(1)).addAttribute("uploadError", "File upload failed");
        assertEquals("myProfile/profile", viewName);
    }

    @Test
    void testGetLoginUserNotLoggedIn() {
        when(session.getAttribute("session_user")).thenReturn(null);

        String viewName = userController.getLogin(model, request, session);

        assertEquals("users/login", viewName);
    }

    @Test
    void testGetLoginUserLoggedIn() {
        when(session.getAttribute("session_user")).thenReturn(user);

        String viewName = userController.getLogin(model, request, session);

        verify(model, times(1)).addAttribute("user", user);
        assertEquals("homepage/Homepage", viewName);
    }

    @Test
    void testLoginInvalidCredentials() {
        Map<String, String> formData = new HashMap<>();
        formData.put("email", "john@example.com");
        formData.put("password", "wrongpassword");

        when(userRepo.findByEmailAndPassword("john@example.com", "wrongpassword")).thenReturn(Collections.emptyList());

        String viewName = userController.login(formData, model, request, session);

        verify(model, times(1)).addAttribute("loginError", "Invalid email or password");
        assertEquals("users/login", viewName);
    }

    @Test
    void testLoginValidCredentials() {
        Map<String, String> formData = new HashMap<>();
        formData.put("email", "john@example.com");
        formData.put("password", "password123");

        when(userRepo.findByEmailAndPassword("john@example.com", "password123")).thenReturn(List.of(user));

        String viewName = userController.login(formData, model, request, session);

        verify(request.getSession(), times(1)).setAttribute("session_user", user);
        verify(request.getSession(), times(1)).setAttribute("userId", user.getId());
        verify(model, times(1)).addAttribute("user", user);
        assertEquals("homepage/Homepage", viewName);
    }

    @Test
    void testAddUser() {
        Map<String, String> newUser = new HashMap<>();
        newUser.put("name", "Jane Doe");
        newUser.put("email", "jane@example.com");
        newUser.put("password", "password123");

        String viewName = userController.addUser(newUser, model);

        verify(userRepo, times(1)).save(any(User.class));
        verify(model, times(1)).addAttribute("message", "User added successfully");
        assertEquals("users/addedUser", viewName);
    }

    @Test
    public void testReloadProfile_UserIsNull() {
        when(session.getAttribute("session_user")).thenReturn(null);
        String view = userController.reloadProfile(session);
        assertEquals("/login", view);
    }

    @Test
    public void testReloadProfile_UserIsNotNull() {
        User user = new User();
        when(session.getAttribute("session_user")).thenReturn(user);
        String view = userController.reloadProfile(session);
        assertEquals("/myProfile/profile", view);
    }

    @Test
    public void testGetProfile_UserIsNull() {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("session_user")).thenReturn(null);
        String view = userController.getProfile(request, model);
        assertEquals("redirect:/login", view);
    }

    @Test
    public void testGetProfile_UserIsNotNull() {
        User user = new User();
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("session_user")).thenReturn(user);
        String view = userController.getProfile(request, model);
        assertEquals("myProfile/profile", view);
        verify(model).addAttribute("user", user);
    }

    @Test
    public void testUpdateBio_UserIsNull() {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("session_user")).thenReturn(null);
        String view = userController.updateBio("new bio", request, model);
        assertEquals("redirect:/login", view);
    }

    @Test
    public void testUpdateBio_UserIsNotNull() {
        User user = new User();
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("session_user")).thenReturn(user);
        String view = userController.updateBio("new bio", request, model);
        assertEquals("redirect:/myProfile/profile", view);
        verify(user).addBio("new bio");
        verify(userRepo).save(user);
        verify(model).addAttribute("user", user);
    }

    @Test
    public void testUpdateName_UserIsNull() {
        when(session.getAttribute("session_user")).thenReturn(null);
        String view = userController.updateName("new name", session, model);
        assertEquals("redirect:/login", view);
    }

    @Test
    public void testUpdateName_UserIsNotNull() {
        User user = new User();
        when(session.getAttribute("session_user")).thenReturn(user);
        String view = userController.updateName("new name", session, model);
        assertEquals("redirect:/myProfile/profile", view);
        verify(user).setName("new name");
        verify(userRepo).save(user);
        verify(model).addAttribute("user", user);
    }

    @Test
    public void testUpdatePassword_UserIsNull() {
        when(session.getAttribute("session_user")).thenReturn(null);
        String view = userController.updatePassword("new password", session, model);
        assertEquals("redirect:/login", view);
    }

    @Test
    public void testUpdatePassword_UserIsNotNull() {
        User user = new User();
        when(session.getAttribute("session_user")).thenReturn(user);
        String view = userController.updatePassword("new password", session, model);
        assertEquals("redirect:/myProfile/profile", view);
        verify(user).setPassword("new password");
        verify(userRepo).save(user);
        verify(model).addAttribute("user", user);
    }
}