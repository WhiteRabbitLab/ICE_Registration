package com.ice.registration.service;

import com.ice.registration.dto.GenreResponse;
import com.ice.registration.entity.Track;
import com.ice.registration.entity.Artist;
import com.ice.registration.entity.Genre;
import com.ice.registration.repository.TrackRepository;
import com.ice.registration.repository.ArtistRepository;
import com.ice.registration.repository.GenreRepository;
import com.ice.registration.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TrackService {
    
    private final TrackRepository trackRepository;
    private final ArtistRepository artistRepository;
    private final GenreRepository genreRepository;
    
    public TrackResponse addTrackToArtist(Long artistId, CreateTrackRequest request) {
        Artist artist = artistRepository.findById(artistId)
            .orElseThrow(() -> new ResourceNotFoundException("Artist not found with id: " + artistId));
        
        Track track = new Track();
        track.setTitle(request.getTitle());
        track.setLengthSeconds(request.getLengthSeconds());
        
        // Set genre if provided
        if (request.getGenreId() != null) {
            Genre genre = genreRepository.findById(request.getGenreId())
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found with id: " + request.getGenreId()));
            track.setGenre(genre);
        }
        
        // Add artist to track
        track.getArtists().add(artist);
        
        Track savedTrack = trackRepository.save(track);
        return mapToTrackResponse(savedTrack);
    }
    
    @Transactional(readOnly = true)
    public TrackResponse getTrackById(Long id) {
        Track track = trackRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Track not found with id: " + id));
        return mapToTrackResponse(track);
    }
    
    private TrackResponse mapToTrackResponse(Track track) {
        Set<ArtistResponse> artistResponses = track.getArtists().stream()
            .map(artist -> ArtistResponse.builder()
                .id(artist.getId())
                .name(artist.getName())
                .picture(artist.getPicture())
                .description(artist.getDescription())
                .build())
            .collect(Collectors.toSet());
        
        GenreResponse genreResponse = null;
        if (track.getGenre() != null) {
            genreResponse = GenreResponse.builder()
                .id(track.getGenre().getId())
                .description(track.getGenre().getDescription())
                .build();
        }
        
        return TrackResponse.builder()
            .id(track.getId())
            .title(track.getTitle())
            .genre(genreResponse)
            .lengthSeconds(track.getLengthSeconds())
            .artists(artistResponses)
            .build();
    }
}