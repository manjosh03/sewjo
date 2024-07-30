package com.sewjo.sewjo.Tests;

import com.sewjo.sewjo.Models.Pattern;
import com.sewjo.sewjo.Models.User;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PatternTests {

    @Test
    public void testPatternConstructorWithImage() {
        User user = new User("John Doe", "password123", "john.doe@example.com", "profilePic.jpg");
        Pattern pattern = new Pattern("Pattern1", "Type1", "Description1", "image1.jpg", 100, user);

        assertEquals("Pattern1", pattern.getName());
        assertEquals("Type1", pattern.getType());
        assertEquals("Description1", pattern.getDescription());
        assertEquals("image1.jpg", pattern.getImage());
        assertEquals(100, pattern.getPrice());
        assertEquals(user, pattern.getUser());
    }

    @Test
    public void testPatternConstructorWithoutImage() {
        User user = new User("Jane Doe", "password456", "jane.doe@example.com", "profilePic2.jpg");
        Pattern pattern = new Pattern("Pattern2", "Type2", "Description2", 200, user);

        assertEquals("Pattern2", pattern.getName());
        assertEquals("Type2", pattern.getType());
        assertEquals("Description2", pattern.getDescription());
        assertEquals("https://via.placeholder.com/150", pattern.getImage());
        assertEquals(200, pattern.getPrice());
        assertEquals(user, pattern.getUser());
    }

    @Test
    public void testPatternSettersAndGetters() {
        User user = new User("John Doe", "password123", "john.doe@example.com", "profilePic.jpg");
        Pattern pattern = new Pattern();
        pattern.setName("Pattern3");
        pattern.setType("Type3");
        pattern.setDescription("Description3");
        pattern.setImage("image3.jpg");
        pattern.setPrice(300);
        pattern.setProjectId(1);

        assertEquals("Pattern3", pattern.getName());
        assertEquals("Type3", pattern.getType());
        assertEquals("Description3", pattern.getDescription());
        assertEquals("image3.jpg", pattern.getImage());
        assertEquals(300, pattern.getPrice());
        assertEquals(1, pattern.getProjectId());
        assertEquals(user, pattern.getUser());
    }
}