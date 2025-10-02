package com.walking.carpractice.model;

import com.walking.carpractice.dto.model.ModelCreateDto;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "model")
public class ModelEntity {
    public ModelEntity(){}
    public ModelEntity(ModelCreateDto newModel){
        this.name= newModel.getName();
        this.brand=newModel.getBrand();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 30)
    private String name;

    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    private BrandEntity brand;

    private LocalDateTime created;

    private LocalDateTime updated;

    //Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setBrand(BrandEntity brand) {
        this.brand = brand;
    }

    //Getters
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BrandEntity getBrand() {
        return brand;
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
