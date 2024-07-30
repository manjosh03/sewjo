package com.sewjo.sewjo.Tests;

import com.sewjo.sewjo.Models.Pattern;
import com.sewjo.sewjo.Models.PatternRepo;
import com.sewjo.sewjo.Models.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.test.annotation.Rollback;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class PatternRepoTests {

    @Autowired
    private PatternRepo patternRepo;

    @PersistenceContext
    private EntityManager entityManager;

    private User user;
    private Pattern pattern1;
    private Pattern pattern2;

    @BeforeEach
    public void setUp() {
        user = new User("John Doe", "password123", "john.doe@example.com", "profilePic.jpg");
        entityManager.persist(user);

        pattern1 = new Pattern("Pattern1", "Type1", "Description1", "image1.jpg", 100, user);
        pattern2 = new Pattern("Pattern2", "Type2", "Description2", "image2.jpg", 200, user);
        entityManager.persist(pattern1);
        entityManager.persist(pattern2);
    }

    @Test
    public void testFindAll() {
        List<Pattern> patterns = patternRepo.findAll();
        assertFalse(patterns.isEmpty());
    }

    @Test
    public void testFindAllByUser() {
        List<Pattern> patterns = patternRepo.findAllByUser(user);
        assertEquals(2, patterns.size());
    }

    @Test
    public void testFindByIdAndUser() {
        Pattern foundPattern = patternRepo.findByIdAndUser(pattern1.getId(), user);
        assertNotNull(foundPattern);
        assertEquals(pattern1.getName(), foundPattern.getName());
    }

    @Test
    public void testFindByProjectId() {
        pattern1.setProjectId(1);
        entityManager.persist(pattern1);

        List<Pattern> patterns = patternRepo.findByProjectId(1);
        assertEquals(1, patterns.size());
    }

    @Test
    public void testFindById() {
        Pattern foundPattern = patternRepo.findById(pattern1.getId());
        assertNotNull(foundPattern);
        assertEquals(pattern1.getName(), foundPattern.getName());
    }

    @Test
    public void testDeleteById() {
        patternRepo.deleteById(pattern1.getId());
        Pattern foundPattern = patternRepo.findById(pattern1.getId());
        assertNull(foundPattern);
    }

    @Test
    public void testDeleteByIdAndUser() {
        patternRepo.deleteByIdAndUser(pattern2.getId(), user);
        Pattern foundPattern = patternRepo.findByIdAndUser(pattern2.getId(), user);
        assertNull(foundPattern);
    }
}
