package com.sewjo.sewjo.Models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    private String image;
    private String type;
    private int progress;
    private boolean shared;
    @ElementCollection
    private List<Integer> patternIds = new ArrayList<>();
    @ElementCollection
    private List<Integer> fabricIds = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Project() {
    }

    public Project(String name, String description, String image, User user, String type, int fabricId, int patternId,
                   boolean shared, int progress) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.user = user;
        this.type = type;
        this.fabricIds.add(fabricId);
        this.patternIds.add(patternId);
        this.shared = shared;
        this.progress = progress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Integer> getPatternIds() {
        return patternIds;
    }

    public void setPatternIds(List<Integer> patternIds) {
        this.patternIds = patternIds;
    }

    public List<Integer> getFabricIds() {
        return fabricIds;
    }

    public void setFabricIds(List<Integer> fabricIds) {
        this.fabricIds = fabricIds;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }

    public int getId() {
        return id;
    }
}
