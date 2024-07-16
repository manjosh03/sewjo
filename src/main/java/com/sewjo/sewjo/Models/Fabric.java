package com.sewjo.sewjo.Models;
import jakarta.persistence.*;

@Entity
public class Fabric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String colour;
    private int width;
    private int height;
    private int price;
    private String type;
    private int projectId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Fabric() {
    }

    public Fabric(String name, String colour, int width, int height, int price, String type, User user) {
        this.name = name;
        this.colour = colour;
        this.width = width;
        this.height = height;
        this.price = price;
        this.type = type;
        this.user = user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getId() {return id;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProject(int projectId) {
        this.projectId = projectId;
    }
}
