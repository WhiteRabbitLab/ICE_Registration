package com.ice.registration.entity;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "track")
public class Track {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id")
    private Genre genre;
    
    @Column(name = "length_seconds")
    private Integer lengthSeconds;
    
    @ManyToMany(mappedBy = "tracks", fetch = FetchType.LAZY)
    private Set<Artist> artists;
    
    public Track() {}
    
    public Track(String title, Genre genre, Integer lengthSeconds) {
        this.title = title;
        this.genre = genre;
        this.lengthSeconds = lengthSeconds;
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public Genre getGenre() {
        return genre;
    }
    
    public void setGenre(Genre genre) {
        this.genre = genre;
    }
    
    public Integer getLengthSeconds() {
        return lengthSeconds;
    }
    
    public void setLengthSeconds(Integer lengthSeconds) {
        this.lengthSeconds = lengthSeconds;
    }
    
    public Set<Artist> getArtists() {
        return artists;
    }
    
    public void setArtists(Set<Artist> artists) {
        this.artists = artists;
    }
    
    // Helper method to format length as MM:SS
    public String getFormattedLength() {
        if (lengthSeconds == null) return "0:00";
        int minutes = lengthSeconds / 60;
        int seconds = lengthSeconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }
}