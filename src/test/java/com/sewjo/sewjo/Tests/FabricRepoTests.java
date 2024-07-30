// File: src/test/java/com/sewjo/sewjo/Models/FabricRepoTest.java

package com.sewjo.sewjo.Tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.sewjo.sewjo.Models.Fabric; // Import the Fabric class
import com.sewjo.sewjo.Models.User; // Import the User class
import com.sewjo.sewjo.Models.FabricRepo; // Import the Fabric class

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class FabricRepoTests {

    @Autowired
    private FabricRepo fabricRepo;

    private User testUser;
    private Fabric testFabric;

    @BeforeEach
    public void setUp() {
        testUser = new User();
        testUser.setId(1);
        testUser.setName("Test User");

        testFabric = new Fabric();

        testFabric.setName("Test Fabric");
        testFabric.setUser(testUser);

        fabricRepo.save(testFabric);
    }

    @Test
    public void testFindByIdAndUser() {
        Fabric found = fabricRepo.findByIdAndUser(1, testUser);
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Test Fabric");
    }

    @Test
    public void testFindAll() {
        List<Fabric> fabrics = fabricRepo.findAll();
        assertThat(fabrics).isNotEmpty();
    }

    @Test
    public void testFindAllByUser() {
        List<Fabric> fabrics = fabricRepo.findAllByUser(testUser);
        assertThat(fabrics).isNotEmpty();
    }

    @Test
    public void testFindByName() {
        List<Fabric> fabrics = fabricRepo.findByName("Test Fabric");
        assertThat(fabrics).isNotEmpty();
    }

    @Test
    public void testFindByProjectId() {
        // Assuming Fabric has a projectId field

        fabricRepo.save(testFabric);

        List<Fabric> fabrics = fabricRepo.findByProjectId(1);
        assertThat(fabrics).isNotEmpty();
    }

    @Test
    public void testFindById() {
        Fabric found = fabricRepo.findById(1);
        assertThat(found).isNotNull();
    }

    @Test
    public void testDeleteById() {
        fabricRepo.deleteById(1);
        Fabric found = fabricRepo.findById(1);
        assertThat(found).isNull();
    }
}