package com.sewjo.login.models;

import jakarta.persistence.*;

@Entity
@Table(name = "Fabrics")
public class Fabric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String color;
    private int width;
    private int height;
    private int price;
    private String type;
    private int projectId;

    public Fabric() {
    }

    public Fabric(String name, String color, int width, int height, int price, String type) {
        this.name = name;
        this.color = color;
        this.width = width;
        this.height = height;
        this.price = price;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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

    public int getId() {
        return id;
    }

}
