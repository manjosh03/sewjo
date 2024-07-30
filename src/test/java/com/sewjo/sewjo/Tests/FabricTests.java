package com.sewjo.sewjo.Tests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.sewjo.sewjo.Models.Fabric; // Import the Fabric class
import com.sewjo.sewjo.Models.User; // Import the User class

class FabricTests {

    @Test
    void testFabricGettersAndSetters() {
        User user = new User(); // Assuming you have a User class
        Fabric fabric = new Fabric("Cotton", "Blue", 100, 200, 1500, "Plain", user, "image.jpg");

        assertEquals("Cotton", fabric.getName());
        assertEquals("Blue", fabric.getColour());
        assertEquals(100, fabric.getWidth());
        assertEquals(200, fabric.getHeight());
        assertEquals(1500, fabric.getPrice());
        assertEquals("Plain", fabric.getType());
        assertEquals(user, fabric.getUser());
        assertEquals("image.jpg", fabric.getImage());

        fabric.setName("Silk");
        assertEquals("Silk", fabric.getName());

        fabric.setColour("Red");
        assertEquals("Red", fabric.getColour());

        fabric.setWidth(150);
        assertEquals(150, fabric.getWidth());

        fabric.setHeight(250);
        assertEquals(250, fabric.getHeight());

        fabric.setPrice(2000);
        assertEquals(2000, fabric.getPrice());

        fabric.setType("Patterned");
        assertEquals("Patterned", fabric.getType());

        fabric.setImage("newimage.jpg");
        assertEquals("newimage.jpg", fabric.getImage());
    }
}