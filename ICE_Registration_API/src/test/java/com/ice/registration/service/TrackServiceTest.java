package com.ice.registration.service;

import com.ice.registration.dto.TrackDto;
import com.ice.registration.entity.Artist;
import com.ice.registration.entity.Genre;
import com.ice.registration.entity.Track;
import com.ice.registration.repository.ArtistRepository;
import com.ice.registration.repository.GenreRepository;
import com.ice.registration.repository.TrackRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrackServiceTest {

    @Mock
    private TrackRepository trackRepository;

    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private TrackService trackService;

    private Track testTrack1;
    private Track testTrack2;
    private Artist testArtist1;
    private Artist testArtist2;
    private Genre testGenre;
    private TrackDto testTrackDto;

    @BeforeEach
    void setUp() {
        // Create test genre
        testGenre = new Genre();
        testGenre.setId(1);
        testGenre.setDescription("Rock");

        // Create test artists
        testArtist1 = new Artist();
        testArtist1.setId(1);
        testArtist1.setName("Artist 1");
        testArtist1.setTracks(new HashSet<>());

        testArtist2 = new Artist();
        testArtist2.setId(2);
        testArtist2.setName("Artist 2");
        testArtist2.setTracks(new HashSet<>());

        // Create test tracks
        testTrack1 = new Track();
        testTrack1.setId(1);
        testTrack1.setTitle("Test Track 1");
        testTrack1.setGenre(testGenre);
        testTrack1.setLengthSeconds(180); // 3 minutes
        testTrack1.setArtists(new HashSet<>(Arrays.asList(testArtist1)));

        testTrack2 = new Track();
        testTrack2.setId(2);
        testTrack2.setTitle("Test Track 2");
        testTrack2.setGenre(testGenre);
        testTrack2.setLengthSeconds(240); // 4 minutes
        testTrack2.setArtists(new HashSet<>(Arrays.asList(testArtist1, testArtist2)));

        // Create test TrackDto for creation
        testTrackDto = new TrackDto();
        testTrackDto.setTitle("New Track");
        testTrackDto.setGenreId(1);
        testTrackDto.setLengthSeconds(200);
        testTrackDto.setArtistIds(Arrays.asList(1, 2));
    }

    // Tests for getTracksByArtistId method

    @Test
    void getTracksByArtistId_ShouldReturnListOfTrackDtos() {
        // Given
        List<Track> tracks = Arrays.asList(testTrack1, testTrack2);
        when(trackRepository.findByArtistIdWithGenre(1)).thenReturn(tracks);

        // When
        List<TrackDto> result = trackService.getTracksByArtistId(1);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());

        TrackDto resultDto1 = result.get(0);
        assertEquals(testTrack1.getId(), resultDto1.getId());
        assertEquals(testTrack1.getTitle(), resultDto1.getTitle());
        assertEquals("Rock", resultDto1.getGenre());
        assertEquals(180, resultDto1.getLengthSeconds());
        assertEquals("3:00", resultDto1.getFormattedLength());

        TrackDto resultDto2 = result.get(1);
        assertEquals(testTrack2.getId(), resultDto2.getId());
        assertEquals(testTrack2.getTitle(), resultDto2.getTitle());
        assertEquals("Rock", resultDto2.getGenre());
        assertEquals(240, resultDto2.getLengthSeconds());
        assertEquals("4:00", resultDto2.getFormattedLength());

        verify(trackRepository, times(1)).findByArtistIdWithGenre(1);
    }

    @Test
    void getTracksByArtistId_ShouldReturnEmptyListWhenNoTracks() {
        // Given
        when(trackRepository.findByArtistIdWithGenre(anyInt())).thenReturn(Arrays.asList());

        // When
        List<TrackDto> result = trackService.getTracksByArtistId(999);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(trackRepository, times(1)).findByArtistIdWithGenre(999);
    }

    @Test
    void getTracksByArtistId_ShouldHandleTrackWithNullGenre() {
        // Given
        Track trackWithNullGenre = new Track();
        trackWithNullGenre.setId(3);
        trackWithNullGenre.setTitle("Track No Genre");
        trackWithNullGenre.setGenre(null);
        trackWithNullGenre.setLengthSeconds(150);

        when(trackRepository.findByArtistIdWithGenre(1)).thenReturn(Arrays.asList(trackWithNullGenre));

        // When
        List<TrackDto> result = trackService.getTracksByArtistId(1);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Unknown", result.get(0).getGenre());
        verify(trackRepository, times(1)).findByArtistIdWithGenre(1);
    }

    // Tests for createTrack method

    @Test
    void createTrack_ShouldCreateTrackSuccessfully() {
        // Given
        Track savedTrack = new Track();
        savedTrack.setId(1);
        savedTrack.setTitle("New Track");
        savedTrack.setGenre(testGenre);
        savedTrack.setLengthSeconds(200);
        savedTrack.setArtists(new HashSet<>(Arrays.asList(testArtist1, testArtist2)));

        when(genreRepository.findById(1)).thenReturn(Optional.of(testGenre));
        when(artistRepository.findById(1)).thenReturn(Optional.of(testArtist1));
        when(artistRepository.findById(2)).thenReturn(Optional.of(testArtist2));
        when(trackRepository.save(any(Track.class))).thenReturn(savedTrack);
        when(artistRepository.saveAll(any())).thenReturn(Arrays.asList(testArtist1, testArtist2));

        // When
        TrackDto result = trackService.createTrack(testTrackDto);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("New Track", result.getTitle());
        assertEquals("Rock", result.getGenre());
        assertEquals(200, result.getLengthSeconds());
        assertEquals("3:20", result.getFormattedLength());

        verify(genreRepository, times(1)).findById(1);
        verify(artistRepository, times(1)).findById(1);
        verify(artistRepository, times(1)).findById(2);
        verify(trackRepository, times(2)).save(any(Track.class)); // Once before artists, once after
        verify(artistRepository, times(1)).saveAll(any());
    }

    @Test
    void createTrack_ShouldThrowExceptionWhenTitleIsNull() {
        // Given
        testTrackDto.setTitle(null);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            trackService.createTrack(testTrackDto);
        });

        assertEquals("Track title is required", exception.getMessage());
        verifyNoInteractions(trackRepository, artistRepository, genreRepository);
    }

    @Test
    void createTrack_ShouldThrowExceptionWhenTitleIsEmpty() {
        // Given
        testTrackDto.setTitle("");

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            trackService.createTrack(testTrackDto);
        });

        assertEquals("Track title is required", exception.getMessage());
        verifyNoInteractions(trackRepository, artistRepository, genreRepository);
    }

    @Test
    void createTrack_ShouldThrowExceptionWhenTitleIsOnlyWhitespace() {
        // Given
        testTrackDto.setTitle("   ");

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            trackService.createTrack(testTrackDto);
        });

        assertEquals("Track title is required", exception.getMessage());
        verifyNoInteractions(trackRepository, artistRepository, genreRepository);
    }

    @Test
    void createTrack_ShouldTrimTitleWhenCreating() {
        // Given
        testTrackDto.setTitle("  Trimmed Track  ");
        
        Track savedTrack = new Track();
        savedTrack.setId(1);
        savedTrack.setTitle("Trimmed Track");
        savedTrack.setGenre(testGenre);
        savedTrack.setLengthSeconds(200);
        savedTrack.setArtists(new HashSet<>(Arrays.asList(testArtist1)));

        when(genreRepository.findById(1)).thenReturn(Optional.of(testGenre));
        when(artistRepository.findById(1)).thenReturn(Optional.of(testArtist1));
        when(artistRepository.findById(2)).thenReturn(Optional.of(testArtist2));
        when(trackRepository.save(any(Track.class))).thenReturn(savedTrack);
        when(artistRepository.saveAll(any())).thenReturn(Arrays.asList(testArtist1, testArtist2));

        // When
        TrackDto result = trackService.createTrack(testTrackDto);

        // Then
        assertEquals("Trimmed Track", result.getTitle());
        verify(trackRepository, times(2)).save(argThat(track -> "Trimmed Track".equals(track.getTitle())));
    }

    @Test
    void createTrack_ShouldThrowExceptionWhenGenreIdIsNull() {
        // Given
        testTrackDto.setGenreId(null);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            trackService.createTrack(testTrackDto);
        });

        assertEquals("Genre is required", exception.getMessage());
        verifyNoInteractions(trackRepository, artistRepository, genreRepository);
    }

    @Test
    void createTrack_ShouldThrowExceptionWhenArtistIdsIsNull() {
        // Given
        testTrackDto.setArtistIds(null);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            trackService.createTrack(testTrackDto);
        });

        assertEquals("At least one artist is required", exception.getMessage());
        verifyNoInteractions(trackRepository, artistRepository, genreRepository);
    }

    @Test
    void createTrack_ShouldThrowExceptionWhenArtistIdsIsEmpty() {
        // Given
        testTrackDto.setArtistIds(Arrays.asList());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            trackService.createTrack(testTrackDto);
        });

        assertEquals("At least one artist is required", exception.getMessage());
        verifyNoInteractions(trackRepository, artistRepository, genreRepository);
    }

    @Test
    void createTrack_ShouldThrowExceptionWhenGenreNotFound() {
        // Given
        when(genreRepository.findById(1)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            trackService.createTrack(testTrackDto);
        });

        assertEquals("Genre not found", exception.getMessage());
        verify(genreRepository, times(1)).findById(1);
        verifyNoInteractions(trackRepository, artistRepository);
    }

    @Test
    void createTrack_ShouldThrowExceptionWhenArtistNotFound() {
        // Given
        when(genreRepository.findById(1)).thenReturn(Optional.of(testGenre));
        when(artistRepository.findById(1)).thenReturn(Optional.of(testArtist1));
        when(artistRepository.findById(2)).thenReturn(Optional.empty());
        when(trackRepository.save(any(Track.class))).thenReturn(testTrack1);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            trackService.createTrack(testTrackDto);
        });

        assertEquals("Artist not found: 2", exception.getMessage());
        verify(genreRepository, times(1)).findById(1);
        verify(artistRepository, times(1)).findById(1);
        verify(artistRepository, times(1)).findById(2);
        verify(trackRepository, times(1)).save(any(Track.class)); // Initial save happens before artist lookup fails
    }

    @Test
    void createTrack_ShouldHandleArtistWithNullTracks() {
        // Given
        testArtist1.setTracks(null); // Null tracks collection
        
        Track savedTrack = new Track();
        savedTrack.setId(1);
        savedTrack.setTitle("New Track");
        savedTrack.setGenre(testGenre);
        savedTrack.setLengthSeconds(200);
        savedTrack.setArtists(new HashSet<>(Arrays.asList(testArtist1)));

        when(genreRepository.findById(1)).thenReturn(Optional.of(testGenre));
        when(artistRepository.findById(1)).thenReturn(Optional.of(testArtist1));
        when(trackRepository.save(any(Track.class))).thenReturn(savedTrack);
        when(artistRepository.saveAll(any())).thenReturn(Arrays.asList(testArtist1));

        testTrackDto.setArtistIds(Arrays.asList(1)); // Only one artist

        // When
        TrackDto result = trackService.createTrack(testTrackDto);

        // Then
        assertNotNull(result);
        assertNotNull(testArtist1.getTracks()); // Should be initialized
        assertTrue(testArtist1.getTracks().contains(savedTrack));
        
        verify(genreRepository, times(1)).findById(1);
        verify(artistRepository, times(1)).findById(1);
        verify(trackRepository, times(2)).save(any(Track.class));
        verify(artistRepository, times(1)).saveAll(any());
    }

    @Test
    void createTrack_ShouldCreateTrackWithSingleArtist() {
        // Given
        testTrackDto.setArtistIds(Arrays.asList(1)); // Only one artist
        
        Track savedTrack = new Track();
        savedTrack.setId(1);
        savedTrack.setTitle("New Track");
        savedTrack.setGenre(testGenre);
        savedTrack.setLengthSeconds(200);
        savedTrack.setArtists(new HashSet<>(Arrays.asList(testArtist1)));

        when(genreRepository.findById(1)).thenReturn(Optional.of(testGenre));
        when(artistRepository.findById(1)).thenReturn(Optional.of(testArtist1));
        when(trackRepository.save(any(Track.class))).thenReturn(savedTrack);
        when(artistRepository.saveAll(any())).thenReturn(Arrays.asList(testArtist1));

        // When
        TrackDto result = trackService.createTrack(testTrackDto);

        // Then
        assertNotNull(result);
        assertEquals("New Track", result.getTitle());
        verify(artistRepository, times(1)).findById(1);
        verify(artistRepository, never()).findById(2); // Second artist not looked up
    }

    @Test
    void createTrack_ShouldHandleNullLengthSeconds() {
        // Given
        testTrackDto.setLengthSeconds(null);
        
        Track savedTrack = new Track();
        savedTrack.setId(1);
        savedTrack.setTitle("New Track");
        savedTrack.setGenre(testGenre);
        savedTrack.setLengthSeconds(null);
        savedTrack.setArtists(new HashSet<>(Arrays.asList(testArtist1, testArtist2)));

        when(genreRepository.findById(1)).thenReturn(Optional.of(testGenre));
        when(artistRepository.findById(1)).thenReturn(Optional.of(testArtist1));
        when(artistRepository.findById(2)).thenReturn(Optional.of(testArtist2));
        when(trackRepository.save(any(Track.class))).thenReturn(savedTrack);
        when(artistRepository.saveAll(any())).thenReturn(Arrays.asList(testArtist1, testArtist2));

        // When
        TrackDto result = trackService.createTrack(testTrackDto);

        // Then
        assertNotNull(result);
        assertNull(result.getLengthSeconds());
        assertEquals("0:00", result.getFormattedLength()); // Should handle null gracefully
    }

    // Tests for convertToDto method (tested indirectly)

    @Test
    void convertToDto_ShouldHandleNullGenre() {
        // Given
        Track trackWithNullGenre = new Track();
        trackWithNullGenre.setId(1);
        trackWithNullGenre.setTitle("No Genre Track");
        trackWithNullGenre.setGenre(null);
        trackWithNullGenre.setLengthSeconds(120);

        when(trackRepository.findByArtistIdWithGenre(1)).thenReturn(Arrays.asList(trackWithNullGenre));

        // When
        List<TrackDto> result = trackService.getTracksByArtistId(1);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Unknown", result.get(0).getGenre());
    }

    @Test
    void convertToDto_ShouldFormatLengthCorrectly() {
        // Given
        Track shortTrack = new Track();
        shortTrack.setId(1);
        shortTrack.setTitle("Short Track");
        shortTrack.setGenre(testGenre);
        shortTrack.setLengthSeconds(65); // 1:05

        Track longTrack = new Track();
        longTrack.setId(2);
        longTrack.setTitle("Long Track");
        longTrack.setGenre(testGenre);
        longTrack.setLengthSeconds(3725); // 62:05

        when(trackRepository.findByArtistIdWithGenre(1)).thenReturn(Arrays.asList(shortTrack, longTrack));

        // When
        List<TrackDto> result = trackService.getTracksByArtistId(1);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("1:05", result.get(0).getFormattedLength());
        assertEquals("62:05", result.get(1).getFormattedLength());
    }
}