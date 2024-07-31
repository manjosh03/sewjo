package com.sewjo.sewjo.Tests;

import com.sewjo.sewjo.Models.Fabric;
import com.sewjo.sewjo.Models.User;
import com.sewjo.sewjo.Controllers.FabricController;

import com.sewjo.sewjo.Models.UserRepo; // Import the User class
import com.sewjo.sewjo.Models.FabricRepo; // Import the Fabric class
import com.sewjo.sewjo.Interfaces.FabricInterface;
import com.sewjo.sewjo.Services.FileStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc

public class FabricControllerTests {

    @Mock
    private UserRepo userRepo;

    @Mock
    private FabricRepo fabricRepo;

    @Mock
    private FileStorageService fileStorageService;

    @Mock
    private Model model;

    @Mock
    private HttpSession session;

    @InjectMocks
    private FabricController fabricController;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        request.setSession(session);
    }

    @Test
    public void testGetAllFabrics_UserNotLoggedIn() {
        when(session.getAttribute("userId")).thenReturn(null);

        String viewName = fabricController.getAllFabrics(request, response, model);

        assertEquals("redirect:/login", viewName);
        assertEquals(401, response.getStatus());
    }

    @Test
    public void testGetAllFabrics_UserLoggedIn() {
        int userId = 1;
        User user = new User();
        user.setId(userId);
        List<Fabric> fabrics = new ArrayList<>();
        List<String> fabricTypes = Arrays.asList("Cotton", "Silk");

        when(session.getAttribute("userId")).thenReturn(userId);
        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(fabricRepo.findAllByUser(user)).thenReturn(fabrics);
        when(FabricInterface.getFabricTypes()).thenReturn(fabricTypes);

        String viewName = fabricController.getAllFabrics(request, response, model);

        assertEquals("fabric/showAll", viewName);
        assertEquals(200, response.getStatus());
        verify(model).addAttribute("fabrics", fabrics);
        verify(model).addAttribute("fabricTypes", fabricTypes);
    }

    @Test
    public void testShowAddFabricPage() {
        List<String> fabricTypes = Arrays.asList("Cotton", "Silk");

        when(FabricInterface.getFabricTypes()).thenReturn(fabricTypes);

        String viewName = fabricController.showAddFabricPage(model, response);

        assertEquals("fabric/addFabric", viewName);
        assertEquals(200, response.getStatus());
        verify(model).addAttribute("fabricTypes", fabricTypes);
    }

    @Test
    public void testShowEditPatternPage() {
        int fabricId = 1;
        Fabric fabric = new Fabric();
        List<String> fabricTypes = Arrays.asList("Cotton", "Silk");

        when(fabricRepo.findById(fabricId)).thenReturn(fabric);
        when(FabricInterface.getFabricTypes()).thenReturn(fabricTypes);

        String viewName = fabricController.showEditPatternPage(fabricId, model, response);

        assertEquals("fabric/editFabric", viewName);
        assertEquals(200, response.getStatus());
        verify(model).addAttribute("fabricTypes", fabricTypes);
        verify(model).addAttribute("fabric", fabric);
    }

    @Test
    public void testAddFabric_UserNotLoggedIn() {
        when(session.getAttribute("userId")).thenReturn(null);

        String viewName = fabricController.addFabric(new HashMap<>(), mock(MultipartFile.class), response, request);

        assertEquals("redirect:/login", viewName);
        assertEquals(401, response.getStatus());
    }

    @Test
    public void testAddFabric_UserLoggedIn() throws IOException {
        int userId = 1;
        User user = new User();
        user.setId(userId);
        MultipartFile file = mock(MultipartFile.class);
        Map<String, String> newFabric = new HashMap<>();
        newFabric.put("name", "FabricName");
        newFabric.put("type", "Cotton");
        newFabric.put("price", "100");
        newFabric.put("width", "50");
        newFabric.put("height", "50");
        newFabric.put("colour", "Red");

        when(session.getAttribute("userId")).thenReturn(userId);
        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(fileStorageService.uploadFile(file)).thenReturn("image_url");

        String viewName = fabricController.addFabric(newFabric, file, response, request);

        assertEquals("redirect:/fabric/view", viewName);
        assertEquals(201, response.getStatus());
        verify(fabricRepo).save(any(Fabric.class));
    }

    @Test
    public void testDeleteFabric_UserNotLoggedIn() {
        when(session.getAttribute("userId")).thenReturn(null);

        String viewName = fabricController.deleteFabric(1, response, request);

        assertEquals("redirect:/login", viewName);
    }

    @Test
    public void testDeleteFabric_UserLoggedIn() {
        int userId = 1;
        int fabricId = 1;
        User user = new User();
        user.setId(userId);
        Fabric fabric = new Fabric();
        fabric.setUser(user);

        when(session.getAttribute("userId")).thenReturn(userId);
        when(fabricRepo.findById(fabricId)).thenReturn(fabric);

        String viewName = fabricController.deleteFabric(fabricId, response, request);

        assertEquals("redirect:/fabric/view", viewName);
        assertEquals(200, response.getStatus());
        verify(fabricRepo).deleteById(fabricId);
    }

    @Test
    public void testGetFabricDetail_UserNotLoggedIn() {
        when(session.getAttribute("userId")).thenReturn(null);

        String viewName = fabricController.getFabricDetail(1, model, response, request);

        assertEquals("redirect:/login", viewName);
        assertEquals(401, response.getStatus());
    }

    @Test
    public void testGetFabricDetail_UserLoggedIn() {
        int userId = 1;
        int fabricId = 1;
        User user = new User();
        user.setId(userId);
        Fabric fabric = new Fabric();

        when(session.getAttribute("userId")).thenReturn(userId);
        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(fabricRepo.findByIdAndUser(fabricId, user)).thenReturn(fabric);

        String viewName = fabricController.getFabricDetail(fabricId, model, response, request);

        assertEquals("fabric/details", viewName);
        assertEquals(200, response.getStatus());
        verify(model).addAttribute("fabric", fabric);
    }

    @Test
    public void testUpdateFabric_UserNotLoggedIn() {
        when(session.getAttribute("userId")).thenReturn(null);

        String viewName = fabricController.updateFabric(new HashMap<>(), mock(MultipartFile.class), response, request);

        assertEquals("redirect:/login", viewName);
        assertEquals(401, response.getStatus());
    }

    @Test
    public void testUpdateFabric_UserLoggedIn() throws IOException {
        int userId = 1;
        int fabricId = 1;
        User user = new User();
        user.setId(userId);
        Fabric fabric = new Fabric();
        fabric.setUser(user);
        MultipartFile file = mock(MultipartFile.class);
        Map<String, String> updatedFabric = new HashMap<>();
        updatedFabric.put("id", String.valueOf(fabricId));
        updatedFabric.put("name", "UpdatedName");
        updatedFabric.put("type", "Cotton");
        updatedFabric.put("price", "100");
        updatedFabric.put("width", "50");
        updatedFabric.put("height", "50");
        updatedFabric.put("colour", "Red");

        when(session.getAttribute("userId")).thenReturn(userId);
        when(fabricRepo.findById(fabricId)).thenReturn(fabric);

        String viewName = fabricController.updateFabric(updatedFabric, file, response, request);

        assertEquals("redirect:/fabric/view", viewName);
        assertEquals(200, response.getStatus());
        verify(fabricRepo).save(fabric);
    }
}