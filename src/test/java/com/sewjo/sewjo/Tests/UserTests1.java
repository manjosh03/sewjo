package com.sewjo.sewjo.Tests;

import com.sewjo.sewjo.Models.User;
import com.sewjo.sewjo.Models.Fabric;
import com.sewjo.sewjo.Models.Pattern;
import com.sewjo.sewjo.Models.Project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTests1 {

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("John Doe", "password123", "john.doe@example.com", "profilePic.jpg");
    }

    @Test
    public void testAddProfilePicture() {
        user.addProfilePicture("newProfilePic.jpg");
        assertEquals("newProfilePic.jpg", user.getProfilePicture());
    }

    @Test
    public void testAddBio() {
        user.addBio("This is a bio.");
        assertEquals("This is a bio.", user.getBio());
    }

    @Test
    public void testAddFabric() {
        Fabric fabric = new Fabric();
        user.addFabric(fabric);
        assertTrue(user.getFabrics().contains(fabric));
    }

    @Test
    public void testAddPattern() {
        Pattern pattern = new Pattern();
        user.addPattern(pattern);
        assertTrue(user.getPatterns().contains(pattern));
    }

    @Test
    public void testAddProject() {
        Project project = new Project();
        user.addProject(project);
        assertTrue(user.getProjects().contains(project));
    }

    @Test
    public void testRemoveFabric() {
        Fabric fabric = new Fabric();
        user.addFabric(fabric);
        user.removeFabric(fabric);
        assertFalse(user.getFabrics().contains(fabric));
    }

    @Test
    public void testRemovePattern() {
        Pattern pattern = new Pattern();
        user.addPattern(pattern);
        user.removePattern(pattern);
        assertFalse(user.getPatterns().contains(pattern));
    }

    @Test
    public void testRemoveProject() {
        Project project = new Project();
        user.addProject(project);
        user.removeProject(project);
        assertFalse(user.getProjects().contains(project));
    }

    @Test
    public void testGettersAndSetters() {
        user.setId(1);
        assertEquals(1, user.getId());

        user.setName("Jane Doe");
        assertEquals("Jane Doe", user.getName());

        user.setPassword("newPassword");
        assertEquals("newPassword", user.getPassword());

        user.setEmail("jane.doe@example.com");
        assertEquals("jane.doe@example.com", user.getEmail());
    }
}