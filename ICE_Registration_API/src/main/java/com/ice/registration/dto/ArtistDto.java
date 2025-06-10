package com.ice.registration.dto;

public class ArtistDto {

    private Integer id;

    private String name;

    private String photo;

    private String description;

    private int trackCount;
    
    // Default constructor
    public ArtistDto() {}
    
    // Constructor
    public ArtistDto(Integer id, String name, String photo, String description, int trackCount) {
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.description = description;
        this.trackCount = trackCount;
    }
    
    // Getters and Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getPhoto() {
        return photo;
    }
    
    public void setPhoto(String photo) {
        this.photo = photo;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public int getTrackCount() {
        return trackCount;
    }
    
    public void setTrackCount(int trackCount) {
        this.trackCount = trackCount;
    }
}