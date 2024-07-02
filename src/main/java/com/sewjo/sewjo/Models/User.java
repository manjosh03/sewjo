package com.sewjo.sewjo.Models;
import jakarta.persistence.*;

import java.util.List;

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
