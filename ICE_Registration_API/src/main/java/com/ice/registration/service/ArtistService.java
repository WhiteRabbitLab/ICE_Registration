package com.ice.registration.service;

import com.ice.registration.dto.ArtistDto;
import com.ice.registration.entity.Artist;
import com.ice.registration.repository.ArtistRepository;
import jakarta.transaction.Transactional;
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

    @Transactional
    public Optional<ArtistDto> updateArtist(Integer id, ArtistDto artistDto) {
        return artistRepository.findById(id)
                .map(artist -> {
                    // Update only the fields that are provided and not null
                    if (artistDto.getName() != null && !artistDto.getName().trim().isEmpty()) {
                        artist.setName(artistDto.getName().trim());
                    }
                    if (artistDto.getDescription() != null) {
                        artist.setDescription(artistDto.getDescription());
                    }
                    if (artistDto.getPhoto() != null) {
                        artist.setPicture(artistDto.getPhoto());
                    }

                    Artist savedArtist = artistRepository.save(artist);
                    return convertToDto(savedArtist);
                });
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