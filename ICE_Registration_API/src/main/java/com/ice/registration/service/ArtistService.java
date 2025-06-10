package com.ice.registration.service;

import com.ice.registration.dto.ArtistDto;
import com.ice.registration.entity.Artist;
import com.ice.registration.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArtistService {
    
    @Autowired
    private ArtistRepository artistRepository;

    public List<ArtistDto> getAllArtists() {
        List<Artist> artists = artistRepository.findAllWithTracks();
        return artists.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<ArtistDto> getArtistById(Integer id) {
        return artistRepository.findById(id)
                .map(this::convertToDto);
    }
    

    private ArtistDto convertToDto(Artist artist) {
        return new ArtistDto(
                artist.getId(),
                artist.getName(),
                artist.getPicture(),
                artist.getDescription(),
                artist.getTrackCount()
        );
    }
}