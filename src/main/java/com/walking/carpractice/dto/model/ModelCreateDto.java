package com.walking.carpractice.dto.model;

import com.walking.carpractice.model.BrandEntity;

public class ModelCreateDto {
    private String name;
    private BrandEntity brand;

    public ModelCreateDto(){}
    public ModelCreateDto(String name, BrandEntity brand){
        this.name=name;
        this.brand=brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BrandEntity getBrand() {
        return brand;
    }

    public void setBrand(BrandEntity brand) {
        this.brand = brand;
    }
}
