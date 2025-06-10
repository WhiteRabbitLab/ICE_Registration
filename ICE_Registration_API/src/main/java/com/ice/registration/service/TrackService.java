package com.ice.registration.service;

import com.ice.registration.dto.TrackDto;
import com.ice.registration.entity.Artist;
import com.ice.registration.entity.Genre;
import com.ice.registration.entity.Track;
import com.ice.registration.repository.ArtistRepository;
import com.ice.registration.repository.GenreRepository;
import com.ice.registration.repository.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TrackService {
    
    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private GenreRepository genreRepository;
    
    public List<TrackDto> getTracksByArtistId(Integer artistId) {
        return trackRepository.findByArtistIdWithGenre(artistId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public TrackDto createTrack(TrackDto trackDto) {

        if (trackDto.getTitle() == null || trackDto.getTitle().trim().isEmpty()) {
            throw new RuntimeException("Track title is required");
        }
        if (trackDto.getGenreId() == null) {
            throw new RuntimeException("Genre is required");
        }
        if (trackDto.getArtistIds() == null || trackDto.getArtistIds().isEmpty()) {
            throw new RuntimeException("At least one artist is required");
        }

        Track track = new Track();
        track.setTitle(trackDto.getTitle().trim());
        track.setLengthSeconds(trackDto.getLengthSeconds());

        Genre genre = genreRepository.findById(trackDto.getGenreId())
                .orElseThrow(() -> new RuntimeException("Genre not found"));
        track.setGenre(genre);

        Track savedTrack = trackRepository.save(track);

        Set<Artist> artists = new HashSet<>();
        for (Integer artistId : trackDto.getArtistIds()) {
            Artist artist = artistRepository.findById(artistId)
                    .orElseThrow(() -> new RuntimeException("Artist not found: " + artistId));
            artists.add(artist);

            if (artist.getTracks() == null) {
                artist.setTracks(new HashSet<>());
            }
            artist.getTracks().add(savedTrack);
        }

        savedTrack.setArtists(artists);

        artistRepository.saveAll(artists);
        savedTrack = trackRepository.save(savedTrack);

        return convertToDto(savedTrack);
    }
    
    private TrackDto convertToDto(Track track) {
        String genreDescription = track.getGenre() != null ? track.getGenre().getDescription() : "Unknown";
        return new TrackDto(
                track.getId(),
                track.getTitle(),
                genreDescription,
                track.getLengthSeconds(),
                track.getFormattedLength()
        );
    }
}