package com.walking.carpractice.dto.model;

import com.walking.carpractice.model.BrandEntity;

public class ModelUpdateDto {
    private long id;
    private String name;
    private BrandEntity brand;

    public ModelUpdateDto(){}
    public ModelUpdateDto(long id, String name, BrandEntity brand) {
        this.id = id;
        this.name = name;
        this.brand = brand;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
