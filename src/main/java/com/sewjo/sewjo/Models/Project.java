package com.sewjo.sewjo.Models;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    private String image;
    private List<Integer> patternIds;
    private List<Integer> fabricIds;

    public Project() {
    }

    public Project(String name, String description, String image) {
        this.name = name;
        this.description = description;
        this.image = image;
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
}
