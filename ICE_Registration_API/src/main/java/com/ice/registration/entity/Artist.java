package com.ice.registration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "artist")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name", nullable = false)
    @NotBlank(message = "Artist name is required")
    @Size(max = 255, message = "Artist name must not exceed 255 characters")
    private String name;
    
    @Column(name = "picture", columnDefinition = "TEXT")
    private String picture;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @ManyToMany(mappedBy = "artists", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Track> tracks = new HashSet<>();
}