package com.sewjo.sewjo.Tests;

import com.sewjo.sewjo.Controllers.ProjectController;
import com.sewjo.sewjo.Models.Project;
import com.sewjo.sewjo.Models.ProjectRepo;
import com.sewjo.sewjo.Controllers.UserController;
import com.sewjo.sewjo.Models.User;
import com.sewjo.sewjo.Models.UserRepo;
import com.sewjo.sewjo.Controllers.PatternController;
import com.sewjo.sewjo.Models.Pattern;
import com.sewjo.sewjo.Models.PatternRepo;
import com.sewjo.sewjo.Models.Fabric;
import com.sewjo.sewjo.Models.FabricRepo;
import com.sewjo.sewjo.Controllers.FabricController;
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
import org.thymeleaf.spring6.expression.Mvc;

import java.nio.file.Files;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProjectController.class)
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectRepo projectRepo;

    @MockBean
    private UserRepo userRepo;

    @MockBean
    private PatternRepo patternRepo;

    @MockBean
    private FabricRepo fabricRepo;

    @MockBean
    private FileStorageService fileStorageService;

    @InjectMocks
    private ProjectController projectController;

    @InjectMocks
    private UserController userController;

    @InjectMocks
    private PatternController patternController;

    @Mock
    private Model model;

    @Mock
    private HttpSession session;

    private User user;
    private Project project;
    private Pattern pattern;
    private Fabric fabric;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        user = new User("test", "test", "test", "test");
        user.setId(1);
        when(session.getAttribute("session_user")).thenReturn(user);
        pattern = new Pattern("Test", "Dress", "Test", "https://via.placeholder.com/150", 20 , user);
        pattern.setId(1);
        fabric = new Fabric("Test", "Blue", 10, 20, 30, "Cotton", user ,"https://via.placeholder.com/150");
        fabric.setId(1);
        project = new Project("Test", "Test", "Test", user, "Test", 1, 1, false, 20);
        project.setId(1);
    }

    @Test
    public void testGetProjects() throws Exception {
        when(session.getAttribute("session_user")).thenReturn(user);
        when(session.getAttribute("userId")).thenReturn(1);
        when(userRepo.findById(1)).thenReturn(java.util.Optional.of(user));
        when(projectRepo.findAllByUser(user)).thenReturn(List.of(project));
        when(fabricRepo.findByIdAndUser(1,user)).thenReturn((fabric));
        when(patternRepo.findByIdAndUser(1,user)).thenReturn((pattern));

        MvcResult result = mockMvc.perform(get("/project/view")
                        .sessionAttr("session_user", user)
                        .sessionAttr("userId", 1))

            .andExpect(status().is2xxSuccessful())
            .andReturn();
        assertEquals("project/showAll", result.getModelAndView().getViewName());
    }

    @Test
    public void testGetProjects_UserNotLoggedIn() throws Exception {
        when(session.getAttribute("userId")).thenReturn(1);
        when(userRepo.findById(1)).thenReturn(java.util.Optional.of(user));
        when(projectRepo.findAllByUser(user)).thenReturn(List.of(project));
        when(fabricRepo.findByIdAndUser(1,user)).thenReturn((fabric));
        when(patternRepo.findByIdAndUser(1,user)).thenReturn((pattern));
        when(session.getAttribute("session_user")).thenReturn(null);

        MvcResult result = mockMvc.perform(get("/project/view"))

            .andExpect(status().is3xxRedirection())
            .andReturn();

        assertEquals("redirect:/login", result.getModelAndView().getViewName());
    }

    @Test
    public void testShowAddProjectPage() throws Exception {
        when(session.getAttribute("session_user")).thenReturn(user);
        when(userRepo.findById(1)).thenReturn(java.util.Optional.of(user));
        when(fabricRepo.findAllByUser(user)).thenReturn(List.of(fabric));
        when(patternRepo.findAllByUser(user)).thenReturn(List.of(pattern));

        MvcResult result = mockMvc.perform(get("/project/add-page")
                        .sessionAttr("session_user", user)
                        .sessionAttr("userId", 1))

            .andExpect(status().is2xxSuccessful())
            .andReturn();
        assertEquals("project/addProject", result.getModelAndView().getViewName());
    }

    @Test
    public void testShowAddProjectPage_UserNotLoggedIn() throws Exception {
        when(session.getAttribute("session_user")).thenReturn(null);
        when(userRepo.findById(1)).thenReturn(java.util.Optional.of(user));
        when(fabricRepo.findAllByUser(user)).thenReturn(List.of(fabric));
        when(patternRepo.findAllByUser(user)).thenReturn(List.of(pattern));

        MvcResult result = mockMvc.perform(get("/project/add-page"))

            .andExpect(status().is3xxRedirection())
            .andReturn();

        assertEquals("redirect:/login", result.getModelAndView().getViewName());
    }

    @Test
    public void testShowEditProjectPage() throws Exception {
        when(session.getAttribute("session_user")).thenReturn(user);
        when(userRepo.findById(1)).thenReturn(java.util.Optional.of(user));
        when(projectRepo.findById(1)).thenReturn((project));
        when(fabricRepo.findAllByUser(user)).thenReturn(List.of(fabric));
        when(patternRepo.findAllByUser(user)).thenReturn(List.of(pattern));

        MvcResult result = mockMvc.perform(get("/project/edit-page?id=1")
                        .sessionAttr("session_user", user)
                        .sessionAttr("userId", 1))

            .andExpect(status().is2xxSuccessful())
            .andReturn();
        assertEquals("project/editProject", result.getModelAndView().getViewName());
    }

    @Test
    public void testShowEditProjectPage_UserNotLoggedIn() throws Exception {
        when(session.getAttribute("session_user")).thenReturn(null);
        when(userRepo.findById(1)).thenReturn(java.util.Optional.of(user));
        when(projectRepo.findById(1)).thenReturn((project));
        when(fabricRepo.findAllByUser(user)).thenReturn(List.of(fabric));
        when(patternRepo.findAllByUser(user)).thenReturn(List.of(pattern));

        MvcResult result = mockMvc.perform(get("/project/edit-page?id=1"))

            .andExpect(status().is3xxRedirection())
            .andReturn();

        assertEquals("redirect:/login", result.getModelAndView().getViewName());
    }

    @Test
    public void testAdd() throws Exception {
        when(session.getAttribute("session_user")).thenReturn(user);
        when(userRepo.findById(1)).thenReturn(java.util.Optional.of(user));
        when(fabricRepo.findByIdAndUser(1,user)).thenReturn((fabric));
        when(patternRepo.findByIdAndUser(1,user)).thenReturn((pattern));
        when(projectRepo.save(any(Project.class))).thenReturn(project);
        when(fileStorageService.uploadFile(any(MultipartFile.class))).thenReturn("http://placeholder.com/150");

        ClassPathResource resource = new ClassPathResource("UserController/614PmbHZHxL.jpg");
        MockMultipartFile file = new MockMultipartFile("file", "UserController/614PmbHZHxL.jpg", "image/jpeg", Files.readAllBytes(resource.getFile().toPath()));

        MvcResult result = mockMvc.perform(multipart("/project/add")
                        .file(file)
                        .sessionAttr("session_user", user)
                        .sessionAttr("userId", 1)
                        .param("name", "Test")
                        .param("description", "Test")
                        .param("type", "Test")
                        .param("fabricId", "1")
                        .param("patternId", "1")
                        .param("shared", "false")
                        .param("progress", "20"))
            .andExpect(status().is3xxRedirection())
            .andReturn();

        assertEquals("redirect:/project/view", result.getModelAndView().getViewName());
        verify(projectRepo, times(1)).save(any(Project.class));
    }

    @Test
    public void testAdd_UserNotLoggedIn() throws Exception {
        when(session.getAttribute("session_user")).thenReturn(null);
        when(userRepo.findById(1)).thenReturn(java.util.Optional.of(user));
        when(fabricRepo.findByIdAndUser(1,user)).thenReturn((fabric));
        when(patternRepo.findByIdAndUser(1,user)).thenReturn((pattern));
        when(projectRepo.save(any(Project.class))).thenReturn(project);
        when(fileStorageService.uploadFile(any(MultipartFile.class))).thenReturn("http://placeholder.com/150");

        ClassPathResource resource = new ClassPathResource("UserController/614PmbHZHxL.jpg");
        MockMultipartFile file = new MockMultipartFile("file", "UserController/614PmbHZHxL.jpg", "image/jpeg", Files.readAllBytes(resource.getFile().toPath()));

        MvcResult result = mockMvc.perform(multipart("/project/add")
                        .file(file)
                        .param("name", "Test")
                        .param("description", "Test")
                        .param("type", "Test")
                        .param("fabricId", "1")
                        .param("patternId", "1")
                        .param("shared", "false")
                        .param("progress", "20"))
            .andExpect(status().is3xxRedirection())
            .andReturn();

        assertEquals("redirect:/login", result.getModelAndView().getViewName());
        verify(projectRepo, times(0)).save(any(Project.class));
    }

    @Test
    public void testEdit() throws Exception {
        when(session.getAttribute("session_user")).thenReturn(user);
        when(userRepo.findById(1)).thenReturn(java.util.Optional.of(user));
        when(fabricRepo.findByIdAndUser(1,user)).thenReturn((fabric));
        when(patternRepo.findByIdAndUser(1,user)).thenReturn((pattern));
        when(projectRepo.findById(1)).thenReturn((project));
        when(fileStorageService.uploadFile(any(MultipartFile.class))).thenReturn("http://placeholder.com/150");

        ClassPathResource resource = new ClassPathResource("UserController/614PmbHZHxL.jpg");
        MockMultipartFile file = new MockMultipartFile("file", "UserController/614PmbHZHxL.jpg", "image/jpeg", Files.readAllBytes(resource.getFile().toPath()));

        MvcResult result = mockMvc.perform(multipart("/project/edit")
                        .file(file)
                        .sessionAttr("session_user", user)
                        .sessionAttr("userId", 1)
                        .param("name", "Test-new")
                        .param("id", "1")
                        .param("description", "Test-new")
                        .param("type", "Test-new")
                        .param("fabricId", "1")
                        .param("patternId", "1")
                        .param("shared", "true")
                        .param("progress", "200"))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        assertEquals("redirect:/project/view", result.getModelAndView().getViewName());
        verify(projectRepo, times(1)).save(any(Project.class));
        assertEquals("Test-new", project.getName());
        assertEquals("Test-new", project.getDescription());
        assertEquals("Test-new", project.getType());
        assertTrue(project.isShared());
        assertEquals(200, project.getProgress());
    }

    @Test
    public void testEdit_UserNotLoggedIn() throws Exception {
        when(session.getAttribute("session_user")).thenReturn(null);
        when(userRepo.findById(1)).thenReturn(java.util.Optional.of(user));
        when(fabricRepo.findByIdAndUser(1,user)).thenReturn((fabric));
        when(patternRepo.findByIdAndUser(1,user)).thenReturn((pattern));
        when(projectRepo.findById(1)).thenReturn((project));
        when(fileStorageService.uploadFile(any(MultipartFile.class))).thenReturn("http://placeholder.com/150");

        ClassPathResource resource = new ClassPathResource("UserController/614PmbHZHxL.jpg");
        MockMultipartFile file = new MockMultipartFile("file", "UserController/614PmbHZHxL.jpg", "image/jpeg", Files.readAllBytes(resource.getFile().toPath()));

        MvcResult result = mockMvc.perform(multipart("/project/edit")
                        .file(file)
                        .param("name", "Test-new")
                        .param("id", "1")
                        .param("description", "Test-new")
                        .param("type", "Test-new")
                        .param("fabricId", "1")
                        .param("patternId", "1")
                        .param("shared", "true")
                        .param("progress", "200"))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        assertEquals("redirect:/login", result.getModelAndView().getViewName());
        verify(projectRepo, times(0)).save(any(Project.class));
    }

    @Test
    public void testDelete() throws Exception {
        doNothing().when(projectRepo).deleteById(1);
        when(session.getAttribute("session_user")).thenReturn(user);
        when(userRepo.findById(1)).thenReturn(java.util.Optional.of(user));
        when(projectRepo.findById(1)).thenReturn((project));

        MvcResult result = mockMvc.perform(post("/project/delete")
                        .sessionAttr("session_user", user)
                        .sessionAttr("userId", 1)
                        .param("id", "1"))
            .andExpect(status().is3xxRedirection())
            .andReturn();

        assertEquals("redirect:/project/view", result.getModelAndView().getViewName());
        verify(projectRepo, times(1)).deleteById(1);
    }

    @Test
    public void testDelete_UserNotLoggedIn() throws Exception {
        doNothing().when(projectRepo).deleteById(1);
        when(session.getAttribute("session_user")).thenReturn(user);
        when(userRepo.findById(1)).thenReturn(null);
        when(projectRepo.findById(1)).thenReturn((project));

        MvcResult result = mockMvc.perform(post("/project/delete")
                        .sessionAttr("session_user", user)
                        .param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        assertEquals("/login", result.getResponse().getRedirectedUrl());
        verify(projectRepo, times(0)).deleteById(1);
    }

    @Test
    public void testDelete_ProjectNotFound() throws Exception {
        doNothing().when(projectRepo).deleteById(1);
        when(session.getAttribute("session_user")).thenReturn(user);
        when(userRepo.findById(1)).thenReturn(java.util.Optional.of(user));
        when(projectRepo.findById(1)).thenReturn((null));

        MvcResult result = mockMvc.perform(post("/project/delete")
                        .sessionAttr("session_user", user)
                        .sessionAttr("userId", 1)
                        .param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        assertEquals("/project/view", result.getResponse().getRedirectedUrl());
        }

}
