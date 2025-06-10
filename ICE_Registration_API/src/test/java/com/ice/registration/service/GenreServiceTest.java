package com.ice.registration.service;

import com.ice.registration.dto.GenreDto;
import com.ice.registration.entity.Genre;
import com.ice.registration.entity.Track;
import com.ice.registration.repository.GenreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenreServiceTest {

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private GenreService genreService;

    private Genre testGenre1;
    private Genre testGenre2;
    private Genre testGenre3;
    private GenreDto expectedGenreDto1;
    private GenreDto expectedGenreDto2;
    private GenreDto expectedGenreDto3;

    @BeforeEach
    void setUp() {
        // Create test genres
        testGenre1 = new Genre();
        testGenre1.setId(1);
        testGenre1.setDescription("Rock");
        testGenre1.setTracks(new HashSet<>());

        testGenre2 = new Genre();
        testGenre2.setId(2);
        testGenre2.setDescription("Jazz");
        testGenre2.setTracks(new HashSet<>());

        testGenre3 = new Genre();
        testGenre3.setId(3);
        testGenre3.setDescription("Electronic");
        testGenre3.setTracks(new HashSet<>());

        // Create expected DTOs
        expectedGenreDto1 = new GenreDto(1, "Rock");
        expectedGenreDto2 = new GenreDto(2, "Jazz");
        expectedGenreDto3 = new GenreDto(3, "Electronic");
    }

    @Test
    void getAllGenres_ShouldReturnListOfGenreDtos() {
        // Given
        List<Genre> genres = Arrays.asList(testGenre1, testGenre2, testGenre3);
        when(genreRepository.findAll()).thenReturn(genres);

        // When
        List<GenreDto> result = genreService.getAllGenres();

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        
        // Verify first genre
        GenreDto resultDto1 = result.get(0);
        assertEquals(expectedGenreDto1.getId(), resultDto1.getId());
        assertEquals(expectedGenreDto1.getName(), resultDto1.getName());
        
        // Verify second genre
        GenreDto resultDto2 = result.get(1);
        assertEquals(expectedGenreDto2.getId(), resultDto2.getId());
        assertEquals(expectedGenreDto2.getName(), resultDto2.getName());
        
        // Verify third genre
        GenreDto resultDto3 = result.get(2);
        assertEquals(expectedGenreDto3.getId(), resultDto3.getId());
        assertEquals(expectedGenreDto3.getName(), resultDto3.getName());
        
        verify(genreRepository, times(1)).findAll();
    }

    @Test
    void getAllGenres_ShouldReturnEmptyListWhenNoGenres() {
        // Given
        when(genreRepository.findAll()).thenReturn(Arrays.asList());

        // When
        List<GenreDto> result = genreService.getAllGenres();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.size());
        
        verify(genreRepository, times(1)).findAll();
    }

    @Test
    void getAllGenres_ShouldReturnSingleGenre() {
        // Given
        List<Genre> genres = Arrays.asList(testGenre1);
        when(genreRepository.findAll()).thenReturn(genres);

        // When
        List<GenreDto> result = genreService.getAllGenres();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        
        GenreDto resultDto = result.get(0);
        assertEquals(expectedGenreDto1.getId(), resultDto.getId());
        assertEquals(expectedGenreDto1.getName(), resultDto.getName());
        
        verify(genreRepository, times(1)).findAll();
    }

    @Test
    void getAllGenres_ShouldHandleGenreWithNullTracks() {
        // Given
        Genre genreWithNullTracks = new Genre();
        genreWithNullTracks.setId(4);
        genreWithNullTracks.setDescription("Classical");
        genreWithNullTracks.setTracks(null); // Null tracks
        
        List<Genre> genres = Arrays.asList(genreWithNullTracks);
        when(genreRepository.findAll()).thenReturn(genres);

        // When
        List<GenreDto> result = genreService.getAllGenres();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        
        GenreDto resultDto = result.get(0);
        assertEquals(4, resultDto.getId());
        assertEquals("Classical", resultDto.getName());
        
        verify(genreRepository, times(1)).findAll();
    }

    @Test
    void getAllGenres_ShouldHandleGenreWithTracks() {
        // Given
        Track track1 = new Track();
        track1.setId(1);
        track1.setTitle("Test Track 1");
        
        Track track2 = new Track();
        track2.setId(2);
        track2.setTitle("Test Track 2");
        
        Set<Track> tracks = new HashSet<>();
        tracks.add(track1);
        tracks.add(track2);
        
        testGenre1.setTracks(tracks);
        
        List<Genre> genres = Arrays.asList(testGenre1);
        when(genreRepository.findAll()).thenReturn(genres);

        // When
        List<GenreDto> result = genreService.getAllGenres();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        
        GenreDto resultDto = result.get(0);
        assertEquals(expectedGenreDto1.getId(), resultDto.getId());
        assertEquals(expectedGenreDto1.getName(), resultDto.getName());
        
        // Verify that tracks don't affect the DTO conversion (since GenreDto doesn't include tracks)
        verify(genreRepository, times(1)).findAll();
    }

    @Test
    void getAllGenres_ShouldHandleSpecialCharactersInDescription() {
        // Given
        Genre genreWithSpecialChars = new Genre();
        genreWithSpecialChars.setId(5);
        genreWithSpecialChars.setDescription("Hip-Hop & R&B");
        genreWithSpecialChars.setTracks(new HashSet<>());
        
        List<Genre> genres = Arrays.asList(genreWithSpecialChars);
        when(genreRepository.findAll()).thenReturn(genres);

        // When
        List<GenreDto> result = genreService.getAllGenres();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        
        GenreDto resultDto = result.get(0);
        assertEquals(5, resultDto.getId());
        assertEquals("Hip-Hop & R&B", resultDto.getName());
        
        verify(genreRepository, times(1)).findAll();
    }

    @Test
    void getAllGenres_ShouldHandleLongDescription() {
        // Given
        String longDescription = "A very long genre description that might be close to the 100 character limit set in the entity";
        Genre genreWithLongDescription = new Genre();
        genreWithLongDescription.setId(6);
        genreWithLongDescription.setDescription(longDescription);
        genreWithLongDescription.setTracks(new HashSet<>());
        
        List<Genre> genres = Arrays.asList(genreWithLongDescription);
        when(genreRepository.findAll()).thenReturn(genres);

        // When
        List<GenreDto> result = genreService.getAllGenres();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        
        GenreDto resultDto = result.get(0);
        assertEquals(6, resultDto.getId());
        assertEquals(longDescription, resultDto.getName());
        
        verify(genreRepository, times(1)).findAll();
    }

    @Test
    void getAllGenres_ShouldPreserveOrderFromRepository() {
        // Given
        List<Genre> genres = Arrays.asList(testGenre3, testGenre1, testGenre2); // Different order
        when(genreRepository.findAll()).thenReturn(genres);

        // When
        List<GenreDto> result = genreService.getAllGenres();

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        
        // Verify order is preserved from repository
        assertEquals(3, result.get(0).getId()); // testGenre3 first
        assertEquals("Electronic", result.get(0).getName());
        
        assertEquals(1, result.get(1).getId()); // testGenre1 second
        assertEquals("Rock", result.get(1).getName());
        
        assertEquals(2, result.get(2).getId()); // testGenre2 third
        assertEquals("Jazz", result.get(2).getName());
        
        verify(genreRepository, times(1)).findAll();
    }

    @Test
    void convertToDto_ShouldMapAllFieldsCorrectly() {
        // This test verifies the convertToDto method indirectly through getAllGenres
        // Given
        Genre genre = new Genre();
        genre.setId(100);
        genre.setDescription("Test Genre");
        genre.setTracks(new HashSet<>());
        
        List<Genre> genres = Arrays.asList(genre);
        when(genreRepository.findAll()).thenReturn(genres);

        // When
        List<GenreDto> result = genreService.getAllGenres();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        
        GenreDto dto = result.get(0);
        assertEquals(genre.getId(), dto.getId());
        assertEquals(genre.getDescription(), dto.getName());
        
        verify(genreRepository, times(1)).findAll();
    }

    @Test
    void getAllGenres_ShouldHandleRepositoryException() {
        // Given
        when(genreRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            genreService.getAllGenres();
        });
        
        verify(genreRepository, times(1)).findAll();
    }

    @Test
    void getAllGenres_ShouldCallRepositoryOnlyOnce() {
        // Given
        List<Genre> genres = Arrays.asList(testGenre1, testGenre2);
        when(genreRepository.findAll()).thenReturn(genres);

        // When
        genreService.getAllGenres();

        // Then
        verify(genreRepository, times(1)).findAll();
        verifyNoMoreInteractions(genreRepository);
    }
}