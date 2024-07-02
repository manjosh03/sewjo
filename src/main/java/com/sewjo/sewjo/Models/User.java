package com.sewjo.sewjo.Models;
import jakarta.persistence.*;

import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String email;
    private String password;
    @OneToMany
    private List<Project> projects;
    @OneToMany
    private List<Fabric> fabrics;
    @OneToMany
    private List<Pattern> patterns;

    public User() {
    }

    public User(String password, String email) {
        this.password = password;
        this.email = email;
        this.fabrics = new ArrayList<>();
        this.patterns = new ArrayList<>();
        this.projects = new ArrayList<>();
    }

    public void addFabric(Fabric fabric) {
        this.fabrics.add(fabric);
    }

    public void addPattern(Pattern pattern) {
        this.patterns.add(pattern);
    }

    public void addProject(Project project) {
        this.projects.add(project);
    }

    public List<Project> getProjects() {
        return projects;
    }

    public List<Fabric> getFabrics() {
        return fabrics;
    }

    public List<Pattern> getPatterns() {
        return patterns;
    }

    public void removeFabric(Fabric fabric) {
        this.fabrics.remove(fabric);
    }

    public void removePattern(Pattern pattern) {
        this.patterns.remove(pattern);
    }

    public void removeProject(Project project) {
        this.projects.remove(project);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
