package com.ice.registration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "track")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Track {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "title", nullable = false)
    @NotBlank(message = "Track title is required")
    @Size(max = 255, message = "Track title must not exceed 255 characters")
    private String title;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id")
    private Genre genre;
    
    @Column(name = "length_seconds")
    @Positive(message = "Track length must be positive")
    private Integer lengthSeconds;
    
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
        name = "artist_track",
        joinColumns = @JoinColumn(name = "track_id"),
        inverseJoinColumns = @JoinColumn(name = "artist_id")
    )

    private Set<Artist> artists = new HashSet<>();

}