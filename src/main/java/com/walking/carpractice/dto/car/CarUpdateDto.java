package com.walking.carpractice.dto.car;

import com.walking.carpractice.dto.model.ModelUpdateDto;
import com.walking.carpractice.model.ModelEntity;

public class CarUpdateDto {
    private long id;
    private int creation_year;
    private String number;
    private String color;
    private ModelEntity model;
    private boolean actualTechnicalInspection;

    public CarUpdateDto(){}

    public CarUpdateDto(long id, int creation_year, String number, String color, ModelEntity model, boolean actualTechnicalInspection) {
        this.id=id;
        this.creation_year = creation_year;
        this.number = number;
        this.color = color;
        this.model = model;
        this.actualTechnicalInspection = actualTechnicalInspection;
    }

    public long getId(){ return id;}

    public void setId(long id){ this.id=id;}

    public int getCreation_year() {
        return creation_year;
    }

    public void setCreation_year(int creation_year) {
        this.creation_year = creation_year;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public ModelEntity getModel() {
        return model;
    }

    public void setModel(ModelEntity model) {
        this.model = model;
    }

    public boolean isActualTechnicalInspection() {
        return actualTechnicalInspection;
    }

    public void setActualTechnicalInspection(boolean actualTechnicalInspection) {
        this.actualTechnicalInspection = actualTechnicalInspection;
    }
}
