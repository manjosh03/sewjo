package com.sewjo.sewjo.Tests;

import com.sewjo.sewjo.Controllers.FabricController;
import com.sewjo.sewjo.Models.Fabric;
import com.sewjo.sewjo.Models.User;

import com.sewjo.sewjo.Models.UserRepo; // Import the User class
import com.sewjo.sewjo.Models.FabricRepo; // Import the Fabric class
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FabricControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepo userRepo;

    @MockBean
    private FabricRepo fabricRepo;

    @Test
    public void testGetAllFabrics_Unauthenticated() throws Exception {
        mockMvc.perform(get("/fabric/view"))
                .andExpect(status().isUnauthorized())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    public void testGetAllFabrics_Authenticated() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", 1);

        User user = new User();
        user.setId(1);
        Mockito.when(userRepo.findById(1)).thenReturn(Optional.of(user).orElse(null));
        Mockito.when(fabricRepo.findAllByUser(user)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/fabric/view").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("fabric/showAll"))
                .andExpect(model().attributeExists("fabrics", "fabricTypes"));
    }

    @Test
    public void testShowAddFabricPage() throws Exception {
        mockMvc.perform(get("/fabric/add-page"))
                .andExpect(status().isOk())
                .andExpect(view().name("fabric/addFabric"))
                .andExpect(model().attributeExists("fabricTypes"));
    }

    @Test
    public void testAddFabric_Unauthenticated() throws Exception {
        mockMvc.perform(post("/fabric/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "Cotton")
                .param("type", "Natural")
                .param("price", "10")
                .param("width", "100")
                .param("height", "200")
                .param("image", "")
                .param("colour", "White"))
                .andExpect(status().isUnauthorized())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    public void testAddFabric_Authenticated() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", 1);

        User user = new User();
        user.setId(1);
        Mockito.when(userRepo.findById(1)).thenReturn(Optional.of(user).orElse(null));

        mockMvc.perform(post("/fabric/add").session(session)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "Cotton")
                .param("type", "Natural")
                .param("price", "10")
                .param("width", "100")
                .param("height", "200")
                .param("image", "")
                .param("colour", "White"))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrl("/fabric/view"));
    }

    // Add more tests for other endpoints similarly
}