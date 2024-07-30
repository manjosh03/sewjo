package com.sewjo.sewjo.Tests;

import com.sewjo.sewjo.Controllers.UserController;
import com.sewjo.sewjo.Models.User;
import com.sewjo.sewjo.Models.UserRepo;
import com.sewjo.sewjo.Services.FileStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import jakarta .servlet.http.HttpSession;

import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepo userRepo;

    @MockBean
    private FileStorageService fileStorageService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @InjectMocks
    private UserController userController;

    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1);
        user.setEmail("test@example.com");
        user.setPassword("password");
        when(session.getAttribute("session_user")).thenReturn(user);
    }

    @Test
    public void testUploadProfilePicture_Success() throws Exception {
        when(fileStorageService.uploadFile(any(MultipartFile.class))).thenReturn("http://example.com/image.jpg");

        ClassPathResource resource = new ClassPathResource("UserController/614PmbHZHxL.jpg");
        MockMultipartFile file = new MockMultipartFile("file", "UserController/614PmbHZHxL.jpg", "image/jpeg", Files.readAllBytes(resource.getFile().toPath()));


        MvcResult result = mockMvc.perform(multipart("/myProfile/uploadProfilePicture")
                        .file(file)
                        .sessionAttr("session_user", user))

                .andExpect(status().is3xxRedirection())
                .andReturn();

        verify(userRepo, times(1)).save(any(User.class));
        assertEquals("/myProfile/view", result.getResponse().getRedirectedUrl());
    }

    @Test
    public void testUploadProfilePicture_UserNotLoggedIn() throws Exception {
        when(session.getAttribute("session_user")).thenReturn(null);

        MvcResult result = mockMvc.perform(multipart("/myProfile/uploadProfilePicture")
                        .file("file", "test image content".getBytes())
                        .contentType("image/jpeg"))

                .andExpect(status().is3xxRedirection())
                .andReturn();

        verify(userRepo, never()).save(any(User.class));
        assertEquals("/login", result.getResponse().getRedirectedUrl());
    }

    @Test
    public void testUploadProfilePicture_FileUploadFailure() throws Exception {
        when(fileStorageService.uploadFile(any(MultipartFile.class))).thenThrow(new IOException("File upload failed"));

        MvcResult result = mockMvc.perform(multipart("/myProfile/uploadProfilePicture")
                        .file("file", "test image content".getBytes())
                        .contentType("image/jpeg")
                        .sessionAttr("session_user", user))

                .andExpect(status().is3xxRedirection())
                .andReturn();

        verify(userRepo, times(1)).save(any(User.class));
        assertEquals("/myProfile/view", result.getResponse().getRedirectedUrl());
    }

    @Test
    public void testGetLogin_UserNotLoggedIn(){
        when(session.getAttribute("session_user")).thenReturn(null);

        String result = userController.getLogin(model, session);

        assertEquals("users/login", result);
    }

    @Test
    public void testGetLogin_UserLoggedIn() {
        String result = userController.getLogin(model, session);

        assertEquals("homepage/Homepage", result);
    }

    @Test
    public void testPasswordUpdate() throws Exception {
        MvcResult result = mockMvc.perform(post("/myProfile/updatePassword")
                        .param("password", "new-password")
                        .sessionAttr("session_user", user))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        verify(userRepo, times(1)).save(any(User.class));
        assertEquals("/myProfile/profile", result.getResponse().getRedirectedUrl());
        assertEquals("new-password", user.getPassword());
    }
}