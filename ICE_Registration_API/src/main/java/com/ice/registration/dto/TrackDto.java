package com.ice.registration.dto;

import java.util.List;

public class TrackDto {

    private Integer id;
    private String title;
    private String genre;
    private Integer genreId; // For creation
    private Integer lengthSeconds;
    private String formattedLength;
    private List<Integer> artistIds; // For creation


    public TrackDto() {}

    public TrackDto(Integer id, String title, String genre, Integer lengthSeconds, String formattedLength) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.lengthSeconds = lengthSeconds;
        this.formattedLength = formattedLength;
    }

    // Getters and setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public Integer getLengthSeconds() { return lengthSeconds; }
    public void setLengthSeconds(Integer lengthSeconds) { this.lengthSeconds = lengthSeconds; }

    public String getFormattedLength() { return formattedLength; }
    public void setFormattedLength(String formattedLength) { this.formattedLength = formattedLength; }

    public Integer getGenreId() { return genreId; }
    public void setGenreId(Integer genreId) { this.genreId = genreId; }

    public List<Integer> getArtistIds() { return artistIds; }
    public void setArtistIds(List<Integer> artistIds) { this.artistIds = artistIds; }
}