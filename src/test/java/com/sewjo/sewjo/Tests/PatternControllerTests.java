package com.sewjo.sewjo.Tests;

import com.sewjo.sewjo.Models.Pattern;

import com.sewjo.sewjo.Models.User;
import com.sewjo.sewjo.Models.UserRepo;

import com.sewjo.sewjo.Models.PatternRepo;
import com.sewjo.sewjo.Controllers.PatternController;

import com.sewjo.sewjo.Services.FileStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PatternControllerTests {

    @Mock
    private UserRepo userRepo;

    @Mock
    private PatternRepo patternRepo;

    @Mock
    private FileStorageService fileStorageService;

    @Mock
    private Model model;

    @InjectMocks
    private PatternController patternController;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private HttpSession session;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        session = request.getSession();
    }

    @Test
    public void testGetAllPatterns_UserNotLoggedIn() {
        String viewName = patternController.getAllPatterns(request, response, model);
        assertEquals("redirect:/login", viewName);
        assertEquals(401, response.getStatus());
    }

    @Test
    public void testGetAllPatterns_UserLoggedIn() {
        session.setAttribute("userId", 1);
        User user = new User();
        user.setId(1);
        List<Pattern> patterns = new ArrayList<>();
        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        when(patternRepo.findAllByUser(user)).thenReturn(patterns);

        String viewName = patternController.getAllPatterns(request, response, model);

        assertEquals("pattern/showAll", viewName);
        assertEquals(200, response.getStatus());
        verify(model, times(1)).addAttribute("patterns", patterns);
    }

    @Test
    public void testShowAddPatternPage() {
        String viewName = patternController.showAddPatternPage(model, response);
        assertEquals("pattern/addPattern", viewName);
        assertEquals(200, response.getStatus());
        verify(model, times(1)).addAttribute(eq("patternTypes"), anyList());
    }

    @Test
    public void testShowEditPatternPage() {
        Pattern pattern = new Pattern();
        when(patternRepo.findById(1)).thenReturn(pattern);

        String viewName = patternController.showEditPatternPage(1, model, response);

        assertEquals("pattern/editPattern", viewName);
        assertEquals(200, response.getStatus());
        verify(model, times(1)).addAttribute(eq("patternTypes"), anyList());
        verify(model, times(1)).addAttribute("pattern", pattern);
    }

    @Test
    public void testAddPattern_UserNotLoggedIn() {
        String viewName = patternController.addPattern(Map.of(), mock(MultipartFile.class), response, request);
        assertEquals("redirect:/login", viewName);
        assertEquals(401, response.getStatus());
    }

    @Test
    public void testAddPattern_UserLoggedIn() throws IOException {
        session.setAttribute("userId", 1);
        User user = new User();
        user.setId(1);
        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        MultipartFile file = mock(MultipartFile.class);
        when(fileStorageService.uploadFile(file)).thenReturn("image_url");

        String viewName = patternController.addPattern(
                Map.of("name", "Pattern1", "type", "Type1", "description", "Description1", "price", "100"), file,
                response, request);

        assertEquals("redirect:/pattern/view", viewName);
        assertEquals(201, response.getStatus());
        verify(patternRepo, times(1)).save(any(Pattern.class));
    }

    @Test
    public void testDeletePattern_UserNotLoggedIn() {
        String viewName = patternController.deletePattern(1, response, request);
        assertEquals("redirect:/login", viewName);
    }

    @Test
    public void testDeletePattern_UserLoggedIn() {
        session.setAttribute("userId", 1);
        User user = new User();
        user.setId(1);
        Pattern pattern = new Pattern();
        when(patternRepo.findById(1)).thenReturn(pattern);

        String viewName = patternController.deletePattern(1, response, request);

        assertEquals("redirect:/pattern/view", viewName);
        assertEquals(200, response.getStatus());
        verify(patternRepo, times(1)).deleteById(1);
    }

    @Test
    public void testGetPatternDetail_UserNotLoggedIn() {
        String viewName = patternController.getPatternDetail(1, model, response, request);
        assertEquals("redirect:/login", viewName);
        assertEquals(401, response.getStatus());
    }

    @Test
    public void testGetPatternDetail_UserLoggedIn() {
        session.setAttribute("userId", 1);
        User user = new User();
        user.setId(1);
        Pattern pattern = new Pattern();
        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        when(patternRepo.findByIdAndUser(1, user)).thenReturn(pattern);

        String viewName = patternController.getPatternDetail(1, model, response, request);

        assertEquals("pattern/details", viewName);
        assertEquals(200, response.getStatus());
        verify(model, times(1)).addAttribute("pattern", pattern);
    }

    @Test
    public void testUpdatePattern_UserNotLoggedIn() {
        String viewName = patternController.updatePattern(Map.of(), mock(MultipartFile.class), response, request);
        assertEquals("redirect:/login", viewName);
        assertEquals(401, response.getStatus());
    }

    @Test
    public void testUpdatePattern_UserLoggedIn() throws IOException {
        session.setAttribute("userId", 1);
        User user = new User();
        user.setId(1);
        Pattern pattern = new Pattern();
        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        when(patternRepo.findById(1)).thenReturn(pattern);
        MultipartFile file = mock(MultipartFile.class);
        when(fileStorageService.uploadFile(file)).thenReturn("image_url");

        String viewName = patternController.updatePattern(
                Map.of("id", "1", "name", "Pattern1", "type", "Type1", "description", "Description1", "price", "100"),
                file, response, request);

        assertEquals("redirect:/pattern/view", viewName);
        assertEquals(200, response.getStatus());
        verify(patternRepo, times(1)).save(pattern);
    }
}