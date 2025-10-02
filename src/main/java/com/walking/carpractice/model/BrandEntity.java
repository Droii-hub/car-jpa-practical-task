package com.walking.carpractice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.walking.carpractice.dto.brand.BrandCreateDto;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "brand")
public class BrandEntity {
    public BrandEntity(){}
    public BrandEntity(BrandCreateDto newBrand){
        this.name= newBrand.getName();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 30)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "brand")//, fetch = FetchType.EAGER)
    private List<ModelEntity> models=new ArrayList<>();

    private LocalDateTime created;

    private LocalDateTime updated;

    //Setters
    public void setName(String name) {
        this.name = name;
    }

    //Getters
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<ModelEntity> getModels() {
        return models;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    @PrePersist
    public void prePersist() {
        created = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updated = LocalDateTime.now();
    }
}
