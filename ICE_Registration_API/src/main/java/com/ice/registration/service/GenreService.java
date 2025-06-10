package com.ice.registration.service;

import com.ice.registration.dto.GenreDto;
import com.ice.registration.entity.Genre;
import com.ice.registration.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenreService {
    
    @Autowired
    private GenreRepository genreRepository;
    
    public List<GenreDto> getAllGenres() {
        return genreRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    private GenreDto convertToDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getDescription());
    }
}