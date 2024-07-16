package com.sewjo.sewjo.Models;

import jakarta.persistence.*;

@Entity
public class Pattern {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String type;
    private String description;
    private String image;
    private int price;
    private int projectId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Pattern() {
    }

    public Pattern(String name, String type, String description, String image, int price, User user) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.image = image;
        this.price = price;
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
