package com.ice.registration.service;

import com.ice.registration.dto.TrackDto;
import com.ice.registration.entity.Track;
import com.ice.registration.repository.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TrackService {
    
    @Autowired
    private TrackRepository trackRepository;
    
    public List<TrackDto> getTracksByArtistId(Integer artistId) {
        return trackRepository.findByArtistIdWithGenre(artistId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
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