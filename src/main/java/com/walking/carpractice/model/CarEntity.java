package com.walking.carpractice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "car")
@NamedEntityGraph(
        name="car-with-owners",
        attributeNodes = @NamedAttributeNode(value = "owners")
)
public class CarEntity {
    public CarEntity(){}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private int creation_year;

    @Column(precision = 10, nullable = false)
    private String number;

    @Column(nullable = false)
    private String color;

    @JsonIgnore
    @ManyToMany(mappedBy = "cars")
//    @JoinTable(name = "owner_car",
//    joinColumns = @JoinColumn(name = "fk_car"),
//    inverseJoinColumns = @JoinColumn(name = "fk_owner"))
    private List<OwnerEntity> owners=new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "model_id")
    private ModelEntity model;

    private LocalDateTime created;

    private LocalDateTime updated;

    private boolean actualTechnicalInspection;

    //Setters
    public void setCreation_year(int creation_year) {
        this.creation_year = creation_year;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setModel(ModelEntity model) {
        this.model = model;
    }

    public void setActualTechnicalInspection(boolean actualTechnicalInspection) {
        this.actualTechnicalInspection = actualTechnicalInspection;
    }

    //Getters
    public long getId() {
        return id;
    }

    public int getCreation_year() {
        return creation_year;
    }

    public String getNumber() {
        return number;
    }

    public String getColor() {
        return color;
    }

    public List<OwnerEntity> getOwners() {
        return owners;
    }

    public ModelEntity getModel() {
        return model;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public boolean isActualTechnicalInspection() {
        return actualTechnicalInspection;
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
