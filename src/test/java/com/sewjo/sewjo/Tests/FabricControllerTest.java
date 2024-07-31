package com.sewjo.sewjo.Tests;

import com.sewjo.sewjo.Controllers.UserController;
import com.sewjo.sewjo.Models.User;
import com.sewjo.sewjo.Models.UserRepo;
import com.sewjo.sewjo.Controllers.FabricController;
import com.sewjo.sewjo.Models.Fabric;
import com.sewjo.sewjo.Models.FabricRepo;
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

@WebMvcTest(FabricController.class)
public class FabricControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FabricRepo fabricRepo;

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
    private FabricController fabricController;

    private Fabric fabric;

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
        fabric = new Fabric("Test", "Blue", 10, 20, 30, "Cotton", user ,"https://via.placeholder.com/150");
    }


    @Test
    public void testGetAllFabrics_UserNotLoggedIn() throws Exception {
        when(session.getAttribute("session_user")).thenReturn(null);

        MvcResult result = mockMvc.perform(get("/fabric/view"))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        verify(fabricRepo, never()).findAllByUser(user);
        assertEquals("/login", result.getResponse().getRedirectedUrl());
    }

    @Test
    public void testGetAllFabrics() throws Exception {
        List<Fabric> fabrics = List.of(fabric);
        when(fabricRepo.findAllByUser(user)).thenReturn(fabrics);
        when(userRepo.findById(1)).thenReturn(java.util.Optional.of(user));

        MvcResult result = mockMvc.perform(get("/fabric/view")
                        .sessionAttr("session_user", user)
                        .sessionAttr("userId", 1))
                .andExpect(status().isOk())
                .andReturn();

        verify(fabricRepo, times(1)).findAllByUser(user);
        assertEquals("fabric/showAll", result.getModelAndView().getViewName());
    }

    @Test
    public void testShowAddFabricPage() throws Exception {
        MvcResult result = mockMvc.perform(get("/fabric/add-page"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("fabric/addFabric", result.getModelAndView().getViewName());
    }

    @Test
    public void testShowEditFabricPage() throws Exception {
        when(fabricRepo.findById(1)).thenReturn((fabric));

        MvcResult result = mockMvc.perform(get("/fabric/edit-page")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andReturn();

        verify(fabricRepo, times(1)).findById(1);
        assertEquals("fabric/editFabric", result.getModelAndView().getViewName());
    }

    @Test
    public void testUploadFabric_Success() throws Exception {
        when(fileStorageService.uploadFile(any(MultipartFile.class))).thenReturn("https://via.placeholder.com/150");
        when(userRepo.findById(1)).thenReturn(java.util.Optional.of(user));

        ClassPathResource resource = new ClassPathResource("UserController/614PmbHZHxL.jpg");
        MockMultipartFile file = new MockMultipartFile("file", "UserController/614PmbHZHxL.jpg", "image/jpeg", Files.readAllBytes(resource.getFile().toPath()));

        MvcResult result = mockMvc.perform(multipart("/fabric/add")
                        .file(file)
                        .sessionAttr("session_user", user)
                        .sessionAttr("userId", 1)
                        .param("name", "Test")
                        .param("colour", "Blue")
                        .param("height", "10")
                        .param("width", "20")
                        .param("price", "30")
                        .param("type", "Cotton"))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        verify(fabricRepo, times(1)).save(any(Fabric.class));
        assertEquals("/fabric/view", result.getResponse().getRedirectedUrl());
    }

    @Test
    public void testUploadFabric_UserNotLoggedIn() throws Exception {
        when(session.getAttribute("session_user")).thenReturn(null);

        MvcResult result = mockMvc.perform(multipart("/fabric/add")
                        .file("file", "test image content".getBytes())
                        .contentType("image/jpeg"))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        verify(fabricRepo, never()).save(any(Fabric.class));
        assertEquals("/login", result.getResponse().getRedirectedUrl());
    }

    @Test
    public void testUploadFabric_PictureUploadFailure() throws Exception {
        when(fileStorageService.uploadFile(any(MultipartFile.class))).thenThrow(new IOException());
        when(userRepo.findById(1)).thenReturn(java.util.Optional.of(user));

        ClassPathResource resource = new ClassPathResource("UserController/614PmbHZHxL.jpg");
        MockMultipartFile file = new MockMultipartFile("file", "UserController/614PmbHZHxL.jpg", "image/jpeg", Files.readAllBytes(resource.getFile().toPath()));

        MvcResult result = mockMvc.perform(multipart("/fabric/add")
                        .file(file)
                        .sessionAttr("session_user", user)
                        .sessionAttr("userId", 1)
                        .param("name", "Test")
                        .param("colour", "Blue")
                        .param("height", "10")
                        .param("width", "20")
                        .param("price", "30")
                        .param("type", "Cotton"))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        verify(fabricRepo, times(1)).save(any(Fabric.class));
        assertEquals("https://via.placeholder.com/150", fabric.getImage());
        assertEquals("/fabric/view", result.getResponse().getRedirectedUrl());
    }

    @Test
    public void testDeleteFabric() throws Exception {
        doNothing().when(fabricRepo).deleteById(1);
        when(userRepo.findById(1)).thenReturn(java.util.Optional.of(user));
        when(fabricRepo.findById(1)).thenReturn((fabric));

        MvcResult result = mockMvc.perform(post("/fabric/delete")
                .param("id", "1")
                .sessionAttr("session_user", user)
                .sessionAttr("userId", 1))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        verify(fabricRepo, times(1)).deleteById(1);
        assertEquals("/fabric/view", result.getResponse().getRedirectedUrl());
    }

    @Test
    public void testDeleteFabric_UserNotLoggedIn() throws Exception {
        when(session.getAttribute("session_user")).thenReturn(null);

        MvcResult result = mockMvc.perform(post("/fabric/delete")
                        .param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        verify(fabricRepo, never()).deleteById(1);
        assertEquals("/login", result.getResponse().getRedirectedUrl());
    }

    @Test
    public void testDeleteFabric_FabricNotFound() throws Exception {
        when(userRepo.findById(1)).thenReturn(java.util.Optional.of(user));
        when(fabricRepo.findById(1)).thenReturn(null);

        MvcResult result = mockMvc.perform(post("/fabric/delete")
                        .param("id", "1")
                        .sessionAttr("session_user", user)
                        .sessionAttr("userId", 1))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        verify(fabricRepo, never()).deleteById(1);
        assertEquals("/fabric/view", result.getResponse().getRedirectedUrl());
    }

    @Test
    public void testUpdateFabric_Success() throws Exception {
        when(fileStorageService.uploadFile(any(MultipartFile.class))).thenReturn("https://via.placeholder.com/150");
        when(userRepo.findById(1)).thenReturn(java.util.Optional.of(user));
        when(fabricRepo.findById(1)).thenReturn((fabric));

        ClassPathResource resource = new ClassPathResource("UserController/614PmbHZHxL.jpg");
        MockMultipartFile file = new MockMultipartFile("file", "UserController/614PmbHZHxL.jpg", "image/jpeg", Files.readAllBytes(resource.getFile().toPath()));

        MvcResult result = mockMvc.perform(multipart("/fabric/update")
                        .file(file)
                        .sessionAttr("session_user", user)
                        .sessionAttr("userId", 1)
                        .param("id", "1")
                        .param("name", "Test-New")
                        .param("colour", "Blue-New")
                        .param("height", "100")
                        .param("width", "200")
                        .param("price", "300")
                        .param("type", "Cotton-New"))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        verify(fabricRepo, times(1)).save(any(Fabric.class));
        assertEquals("/fabric/view", result.getResponse().getRedirectedUrl());
        assertEquals("Test-New", fabric.getName());
        assertEquals("Blue-New", fabric.getColour());
        assertEquals(100, fabric.getHeight());
        assertEquals(200, fabric.getWidth());
        assertEquals(300, fabric.getPrice());
        assertEquals("Cotton-New", fabric.getType());
    }

    @Test
    public void testUpdateFabric_UserNotLoggedIn() throws Exception {
        when(session.getAttribute("session_user")).thenReturn(null);

        MvcResult result = mockMvc.perform(multipart("/fabric/update")
                        .file("file", "test image content".getBytes())
                        .contentType("image/jpeg"))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        verify(fabricRepo, never()).save(any(Fabric.class));
        assertEquals("/login", result.getResponse().getRedirectedUrl());
    }

    @Test
    public void testUpdateFabric_FileEmpty() throws Exception {
        when(fileStorageService.uploadFile(any(MultipartFile.class))).thenReturn("https://via.placeholder.com/300");
        when(userRepo.findById(1)).thenReturn(java.util.Optional.of(user));
        when(fabricRepo.findById(1)).thenReturn((fabric));

        MockMultipartFile emptyFile = new MockMultipartFile("file", "", "image/jpeg", new byte[0]);

        MvcResult result = mockMvc.perform(multipart("/fabric/update")
                        .file(emptyFile)
                        .sessionAttr("session_user", user)
                        .sessionAttr("userId", 1)
                        .param("id", "1")
                        .param("name", "Test-New")
                        .param("colour", "Blue-New")
                        .param("height", "100")
                        .param("width", "200")
                        .param("price", "300")
                        .param("type", "Cotton-New"))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        verify(fabricRepo, times(1)).save(any(Fabric.class));
        assertEquals("/fabric/view", result.getResponse().getRedirectedUrl());
        assertEquals("Test-New", fabric.getName());
        assertEquals("Blue-New", fabric.getColour());
        assertEquals(100, fabric.getHeight());
        assertEquals(200, fabric.getWidth());
        assertEquals(300, fabric.getPrice());
        assertEquals("Cotton-New", fabric.getType());
        assertEquals("https://via.placeholder.com/150", fabric.getImage());
    }
}