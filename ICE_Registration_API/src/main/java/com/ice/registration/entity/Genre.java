package com.ice.registration.entity;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "genre")
public class Genre {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false, unique = true, length = 100)
    private String description;
    
    @OneToMany(mappedBy = "genre")
    private Set<Track> tracks;
    
    public Genre() {}
    
    public Genre(String description) {
        this.description = description;
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Set<Track> getTracks() {
        return tracks;
    }
    
    public void setTracks(Set<Track> tracks) {
        this.tracks = tracks;
    }
}