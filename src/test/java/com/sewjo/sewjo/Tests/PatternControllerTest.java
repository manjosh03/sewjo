package com.sewjo.sewjo.Tests;

import com.sewjo.sewjo.Controllers.UserController;
import com.sewjo.sewjo.Models.User;
import com.sewjo.sewjo.Models.UserRepo;
import com.sewjo.sewjo.Controllers.PatternController;
import com.sewjo.sewjo.Models.Pattern;
import com.sewjo.sewjo.Models.PatternRepo;
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

import jakarta.servlet.http.HttpSession;

import java.nio.file.Files;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PatternController.class)
public class PatternControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatternRepo patternRepo;

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

    @InjectMocks
    private PatternController patternController;

    private Pattern pattern;

    @BeforeEach
    public void setUpUser() {
        MockitoAnnotations.openMocks(this);
        user = new User("test", "test", "test", "test");
        user.setId(1);
        when(session.getAttribute("session_user")).thenReturn(user);
    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User("test", "test", "test", "test");
        user.setId(1);
        pattern = new Pattern("Test", "Dress", "Test", "https://via.placeholder.com/150", 20 , user);
    }

    @Test
    public void testGetAllPatterns_UserNotLoggedIn() throws Exception {
        when(session.getAttribute("session_user")).thenReturn(null);

        MvcResult result = mockMvc.perform(get("/pattern/view"))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        verify(patternRepo, never()).findAllByUser(user);
        assertEquals("/login", result.getResponse().getRedirectedUrl());
    }

    @Test
    public void testGetAllPatterns() throws Exception {
        List<Pattern> patterns = List.of(pattern);
        when(patternRepo.findAllByUser(user)).thenReturn(patterns);
        when(userRepo.findById(1)).thenReturn(java.util.Optional.of(user));

        MvcResult result = mockMvc.perform(get("/pattern/view")
                        .sessionAttr("session_user", user)
                        .sessionAttr("userId", 1))
                .andExpect(status().isOk())
                .andReturn();

        verify(patternRepo, times(1)).findAllByUser(user);
        assertEquals("pattern/showAll", result.getModelAndView().getViewName());
    }

    @Test
    public void testShowAddPatternPage() throws Exception {
        MvcResult result = mockMvc.perform(get("/pattern/add-page"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("pattern/addPattern", result.getModelAndView().getViewName());
    }

    @Test
    public void testShowEditPatternPage() throws Exception{
        when(patternRepo.findById(1)).thenReturn((pattern));

        MvcResult result = mockMvc.perform(get("/pattern/edit-page")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andReturn();

        verify(patternRepo, times(1)).findById(1);
        assertEquals("pattern/editPattern", result.getModelAndView().getViewName());
    }

    @Test
    public void testUploadPattern_Success() throws Exception {
        when(fileStorageService.uploadFile(any(MultipartFile.class))).thenReturn("https://via.placeholder.com/150");
        when(userRepo.findById(1)).thenReturn(java.util.Optional.of(user));

        ClassPathResource resource = new ClassPathResource("UserController/614PmbHZHxL.jpg");
        MockMultipartFile file = new MockMultipartFile("file", "UserController/614PmbHZHxL.jpg", "image/jpeg", Files.readAllBytes(resource.getFile().toPath()));

        MvcResult result = mockMvc.perform(multipart("/pattern/add")
                        .file(file)
                        .sessionAttr("session_user", user)
                        .sessionAttr("userId", 1)
                        .param("type", "Dress")
                        .param("description", "Test")
                        .param("price", "30"))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        verify(patternRepo, times(1)).save(any(Pattern.class));
        assertEquals("/pattern/view", result.getResponse().getRedirectedUrl());
    }

    @Test
    public void testUploadPattern_UserNotLoggedIn() throws Exception{
        when(session.getAttribute("session_user")).thenReturn(null);

        MvcResult result = mockMvc.perform(multipart("/pattern/add")
                        .file("file", "test image content".getBytes())
                        .contentType("image/jpeg"))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        verify(patternRepo, never()).save(any(Pattern.class));
        assertEquals("/login", result.getResponse().getRedirectedUrl());
    }

    @Test
    public void testUploadPattern_PictureUploadFailure() throws Exception {
        when(fileStorageService.uploadFile(any(MultipartFile.class))).thenReturn("https://via.placeholder.com/300");
        when(userRepo.findById(1)).thenReturn(java.util.Optional.of(user));

        ClassPathResource resource = new ClassPathResource("UserController/614PmbHZHxL.jpg");
        MockMultipartFile file = new MockMultipartFile("file", "UserController/614PmbHZHxL.jpg", "image/jpeg", Files.readAllBytes(resource.getFile().toPath()));

        MvcResult result = mockMvc.perform(multipart("/pattern/add")
                        .file(file)
                        .sessionAttr("session_user", user)
                        .sessionAttr("userId", 1)
                        .param("type", "Dress")
                        .param("description", "Test")
                        .param("price", "30"))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        verify(patternRepo, times(1)).save(any(Pattern.class));
        assertEquals("https://via.placeholder.com/150", pattern.getImage());
        assertEquals("/pattern/view", result.getResponse().getRedirectedUrl());
    }

    @Test
    public void testDeletePattern() throws Exception {
        doNothing().when(patternRepo).deleteById(1);
        when(userRepo.findById(1)).thenReturn(java.util.Optional.of(user));
        when(patternRepo.findById(1)).thenReturn((pattern));

        MvcResult result = mockMvc.perform(post("/pattern/delete")
                        .param("id", "1")
                        .sessionAttr("session_user", user)
                        .sessionAttr("userId", 1))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        verify(patternRepo, times(1)).deleteById(1);
        assertEquals("/pattern/view", result.getResponse().getRedirectedUrl());
    }

    @Test
    public void testDeletePattern_UserNotLoggedIn() throws Exception {
        when(session.getAttribute("session_user")).thenReturn(null);

        MvcResult result = mockMvc.perform(post("/pattern/delete")
                        .param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        verify(patternRepo, never()).deleteById(1);
        assertEquals("/login", result.getResponse().getRedirectedUrl());
    }

    @Test
    public void testDeletePattern_PatternNotFound() throws Exception {
        when(patternRepo.findById(1)).thenReturn(null);
        when(userRepo.findById(1)).thenReturn(java.util.Optional.of(user));

        MvcResult result = mockMvc.perform(post("/pattern/delete")
                        .param("id", "1")
                        .sessionAttr("session_user", user)
                        .sessionAttr("userId", 1))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        verify(patternRepo, never()).deleteById(1);
        assertEquals("/pattern/view", result.getResponse().getRedirectedUrl());
    }

    @Test
    public void testUpdatePattern_Success() throws Exception {
        when(fileStorageService.uploadFile(any(MultipartFile.class))).thenReturn("https://via.placeholder.com/150");
        when(userRepo.findById(1)).thenReturn(java.util.Optional.of(user));
        when(patternRepo.findById(1)).thenReturn(pattern);

        ClassPathResource resource = new ClassPathResource("UserController/614PmbHZHxL.jpg");
        MockMultipartFile file = new MockMultipartFile("file", "UserController/614PmbHZHxL.jpg", "image/jpeg", Files.readAllBytes(resource.getFile().toPath()));

        MvcResult result = mockMvc.perform(multipart("/pattern/update")
                        .file(file)
                        .sessionAttr("session_user", user)
                        .sessionAttr("userId", 1)
                        .param("id", "1")
                        .param("type", "Dress")
                        .param("description", "Test")
                        .param("price", "30"))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        verify(patternRepo, times(1)).save(any(Pattern.class));
        assertEquals("/pattern/view", result.getResponse().getRedirectedUrl());
    }

    @Test
    public void testUpdatePattern_UserNotLoggedIn() throws Exception {
        when(session.getAttribute("session_user")).thenReturn(null);

        MvcResult result = mockMvc.perform(multipart("/pattern/update")
                        .file("file", "test image content".getBytes())
                        .contentType("image/jpeg"))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        verify(patternRepo, never()).save(any(Pattern.class));
        assertEquals("/login", result.getResponse().getRedirectedUrl());
    }

    @Test
    public void testUpdatePattern_FileEmpty() throws Exception {
        when(fileStorageService.uploadFile(any(MultipartFile.class))).thenReturn("https://via.placeholder.com/300");
        when(userRepo.findById(1)).thenReturn(java.util.Optional.of(user));
        when(patternRepo.findById(1)).thenReturn(pattern);

        MockMultipartFile emptyFile = new MockMultipartFile("file", "", "image/jpeg", new byte[0]);

        MvcResult result = mockMvc.perform(multipart("/pattern/update")
                        .file(emptyFile)
                        .sessionAttr("session_user", user)
                        .sessionAttr("userId", 1)
                        .param("id", "1")
                        .param("type", "Dress-new")
                        .param("description", "Test-new")
                        .param("price", "300"))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        verify(patternRepo, times(1)).save(any(Pattern.class));
        assertEquals("/pattern/view", result.getResponse().getRedirectedUrl());
        assertEquals("https://via.placeholder.com/150", pattern.getImage());
        assertEquals("Dress-new", pattern.getType());
        assertEquals("Test-new", pattern.getDescription());
        assertEquals(300, pattern.getPrice());

    }
}