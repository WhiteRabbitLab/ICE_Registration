package com.ice.registration.dto;

public class GenreDto {
    private Integer id;
    private String name;
    
    public GenreDto() {}
    
    public GenreDto(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
    
    // Getters and setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}