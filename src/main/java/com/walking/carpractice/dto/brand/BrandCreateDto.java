package com.walking.carpractice.dto.brand;

public class BrandCreateDto {
    public BrandCreateDto(String name){
        this.name=name;
    }
    public BrandCreateDto(){}
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
