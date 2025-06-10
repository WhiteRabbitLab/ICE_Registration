package com.ice.registration.service;

import com.ice.registration.dto.ArtistDto;
import com.ice.registration.entity.Artist;
import com.ice.registration.entity.Track;
import com.ice.registration.repository.ArtistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArtistServiceTest {

    @Mock
    private ArtistRepository artistRepository;

    @InjectMocks
    private ArtistService artistService;

    private Artist testArtist;
    private ArtistDto testArtistDto;
    private Track testTrack1;
    private Track testTrack2;

    @BeforeEach
    void setUp() {
        // Create test tracks
        testTrack1 = new Track();
        testTrack1.setId(1);
        testTrack1.setTitle("Test Track 1");

        testTrack2 = new Track();
        testTrack2.setId(2);
        testTrack2.setTitle("Test Track 2");

        // Create test artist with tracks
        testArtist = new Artist();
        testArtist.setId(1);
        testArtist.setName("Test Artist");
        testArtist.setPicture("test-photo.jpg");
        testArtist.setDescription("Test Description");

        Set<Track> tracks = new HashSet<>();
        tracks.add(testTrack1);
        tracks.add(testTrack2);
        testArtist.setTracks(tracks);

        testArtistDto = new ArtistDto(1, "Test Artist", "test-photo.jpg", "Test Description", 2);
    }

    @Test
    void getAllArtists_ShouldReturnListOfArtistDtos() {
        // Given
        List<Artist> artists = Arrays.asList(testArtist);
        when(artistRepository.findAllWithTracks()).thenReturn(artists);

        // When
        List<ArtistDto> result = artistService.getAllArtists();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        ArtistDto resultDto = result.get(0);
        assertEquals(testArtistDto.getId(), resultDto.getId());
        assertEquals(testArtistDto.getName(), resultDto.getName());
        assertEquals(testArtistDto.getPhoto(), resultDto.getPhoto());
        assertEquals(testArtistDto.getDescription(), resultDto.getDescription());
        assertEquals(2, resultDto.getTrackCount()); // Should be calculated from tracks collection

        verify(artistRepository, times(1)).findAllWithTracks();
    }

    @Test
    void getAllArtists_ShouldReturnEmptyListWhenNoArtists() {
        // Given
        when(artistRepository.findAllWithTracks()).thenReturn(Arrays.asList());

        // When
        List<ArtistDto> result = artistService.getAllArtists();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(artistRepository, times(1)).findAllWithTracks();
    }

    @Test
    void getAllArtists_ShouldHandleArtistWithNoTracks() {
        // Given
        Artist artistWithNoTracks = new Artist();
        artistWithNoTracks.setId(2);
        artistWithNoTracks.setName("Artist No Tracks");
        artistWithNoTracks.setPicture("no-tracks.jpg");
        artistWithNoTracks.setDescription("No tracks description");
        artistWithNoTracks.setTracks(new HashSet<>()); // Empty set

        when(artistRepository.findAllWithTracks()).thenReturn(Arrays.asList(artistWithNoTracks));

        // When
        List<ArtistDto> result = artistService.getAllArtists();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(0, result.get(0).getTrackCount());

        verify(artistRepository, times(1)).findAllWithTracks();
    }

    @Test
    void getAllArtists_ShouldHandleArtistWithNullTracks() {
        // Given
        Artist artistWithNullTracks = new Artist();
        artistWithNullTracks.setId(3);
        artistWithNullTracks.setName("Artist Null Tracks");
        artistWithNullTracks.setPicture("null-tracks.jpg");
        artistWithNullTracks.setDescription("Null tracks description");
        artistWithNullTracks.setTracks(null); // Null tracks

        when(artistRepository.findAllWithTracks()).thenReturn(Arrays.asList(artistWithNullTracks));

        // When
        List<ArtistDto> result = artistService.getAllArtists();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(0, result.get(0).getTrackCount()); // Should handle null gracefully

        verify(artistRepository, times(1)).findAllWithTracks();
    }

    @Test
    void getArtistById_ShouldReturnArtistDtoWhenFound() {
        // Given
        when(artistRepository.findById(1)).thenReturn(Optional.of(testArtist));

        // When
        Optional<ArtistDto> result = artistService.getArtistById(1);

        // Then
        assertTrue(result.isPresent());
        ArtistDto resultDto = result.get();
        assertEquals(testArtistDto.getId(), resultDto.getId());
        assertEquals(testArtistDto.getName(), resultDto.getName());
        assertEquals(testArtistDto.getPhoto(), resultDto.getPhoto());
        assertEquals(testArtistDto.getDescription(), resultDto.getDescription());
        assertEquals(2, resultDto.getTrackCount());

        verify(artistRepository, times(1)).findById(1);
    }

    @Test
    void getArtistById_ShouldReturnEmptyOptionalWhenNotFound() {
        // Given
        when(artistRepository.findById(anyInt())).thenReturn(Optional.empty());

        // When
        Optional<ArtistDto> result = artistService.getArtistById(999);

        // Then
        assertFalse(result.isPresent());
        verify(artistRepository, times(1)).findById(999);
    }

    @Test
    void updateArtist_ShouldUpdateAllFieldsWhenAllProvided() {
        // Given
        ArtistDto updateDto = new ArtistDto(null, "Updated Artist", "updated-photo.jpg", "Updated Description", 1);
        when(artistRepository.findById(1)).thenReturn(Optional.of(testArtist));
        when(artistRepository.save(any(Artist.class))).thenReturn(testArtist);

        // When
        Optional<ArtistDto> result = artistService.updateArtist(1, updateDto);

        // Then
        assertTrue(result.isPresent());
        assertEquals("Updated Artist", testArtist.getName());
        assertEquals("updated-photo.jpg", testArtist.getPicture());
        assertEquals("Updated Description", testArtist.getDescription());
        assertEquals(2, result.get().getTrackCount()); // Track count should remain based on collection

        verify(artistRepository, times(1)).findById(1);
        verify(artistRepository, times(1)).save(testArtist);
    }

    @Test
    void updateArtist_ShouldUpdateOnlyNameWhenOnlyNameProvided() {
        // Given
        String originalDescription = testArtist.getDescription();
        String originalPicture = testArtist.getPicture();

        ArtistDto updateDto = new ArtistDto(null, "Updated Name Only", null, null, 1);
        when(artistRepository.findById(1)).thenReturn(Optional.of(testArtist));
        when(artistRepository.save(any(Artist.class))).thenReturn(testArtist);

        // When
        Optional<ArtistDto> result = artistService.updateArtist(1, updateDto);

        // Then
        assertTrue(result.isPresent());
        assertEquals("Updated Name Only", testArtist.getName());
        assertEquals(originalDescription, testArtist.getDescription()); // Should remain unchanged
        assertEquals(originalPicture, testArtist.getPicture()); // Should remain unchanged
        assertEquals(2, result.get().getTrackCount()); // Track count based on collection

        verify(artistRepository, times(1)).findById(1);
        verify(artistRepository, times(1)).save(testArtist);
    }

    @Test
    void updateArtist_ShouldTrimNameWhenUpdating() {
        // Given
        ArtistDto updateDto = new ArtistDto(null, "  Trimmed Name  ", null, null, 1);
        when(artistRepository.findById(1)).thenReturn(Optional.of(testArtist));
        when(artistRepository.save(any(Artist.class))).thenReturn(testArtist);

        // When
        Optional<ArtistDto> result = artistService.updateArtist(1, updateDto);

        // Then
        assertTrue(result.isPresent());
        assertEquals("Trimmed Name", testArtist.getName());

        verify(artistRepository, times(1)).findById(1);
        verify(artistRepository, times(1)).save(testArtist);
    }

    @Test
    void updateArtist_ShouldNotUpdateNameWhenEmpty() {
        // Given
        String originalName = testArtist.getName();
        ArtistDto updateDto = new ArtistDto(null, "", null, null, 1);
        when(artistRepository.findById(1)).thenReturn(Optional.of(testArtist));
        when(artistRepository.save(any(Artist.class))).thenReturn(testArtist);

        // When
        Optional<ArtistDto> result = artistService.updateArtist(1, updateDto);

        // Then
        assertTrue(result.isPresent());
        assertEquals(originalName, testArtist.getName()); // Should remain unchanged

        verify(artistRepository, times(1)).findById(1);
        verify(artistRepository, times(1)).save(testArtist);
    }

    @Test
    void updateArtist_ShouldNotUpdateNameWhenOnlyWhitespace() {
        // Given
        String originalName = testArtist.getName();
        ArtistDto updateDto = new ArtistDto(null, "   ", null, null, 1);
        when(artistRepository.findById(1)).thenReturn(Optional.of(testArtist));
        when(artistRepository.save(any(Artist.class))).thenReturn(testArtist);

        // When
        Optional<ArtistDto> result = artistService.updateArtist(1, updateDto);

        // Then
        assertTrue(result.isPresent());
        assertEquals(originalName, testArtist.getName()); // Should remain unchanged

        verify(artistRepository, times(1)).findById(1);
        verify(artistRepository, times(1)).save(testArtist);
    }

    @Test
    void updateArtist_ShouldReturnEmptyOptionalWhenArtistNotFound() {
        // Given
        ArtistDto updateDto = new ArtistDto(null, "Updated Artist", null, null, 1);
        when(artistRepository.findById(anyInt())).thenReturn(Optional.empty());

        // When
        Optional<ArtistDto> result = artistService.updateArtist(999, updateDto);

        // Then
        assertFalse(result.isPresent());
        verify(artistRepository, times(1)).findById(999);
        verify(artistRepository, never()).save(any(Artist.class));
    }

    @Test
    void updateArtist_ShouldHandleNullFields() {
        // Given
        String originalName = testArtist.getName();
        String originalDescription = testArtist.getDescription();
        String originalPicture = testArtist.getPicture();

        ArtistDto updateDto = new ArtistDto(null, null, null, null, 1);
        when(artistRepository.findById(1)).thenReturn(Optional.of(testArtist));
        when(artistRepository.save(any(Artist.class))).thenReturn(testArtist);

        // When
        Optional<ArtistDto> result = artistService.updateArtist(1, updateDto);

        // Then
        assertTrue(result.isPresent());
        assertEquals(originalName, testArtist.getName()); // Should remain unchanged
        assertEquals(originalDescription, testArtist.getDescription()); // Should remain unchanged
        assertEquals(originalPicture, testArtist.getPicture()); // Should remain unchanged
        assertEquals(2, result.get().getTrackCount()); // Track count from collection

        verify(artistRepository, times(1)).findById(1);
        verify(artistRepository, times(1)).save(testArtist);
    }

    @Test
    void updateArtist_ShouldPreserveTracksCollection() {
        // Given
        Set<Track> originalTracks = new HashSet<>(testArtist.getTracks());
        ArtistDto updateDto = new ArtistDto(null, "Updated Artist", null, null, 1);
        when(artistRepository.findById(1)).thenReturn(Optional.of(testArtist));
        when(artistRepository.save(any(Artist.class))).thenReturn(testArtist);

        // When
        Optional<ArtistDto> result = artistService.updateArtist(1, updateDto);

        // Then
        assertTrue(result.isPresent());
        assertEquals(originalTracks, testArtist.getTracks()); // Tracks should remain unchanged
        assertEquals(2, result.get().getTrackCount());

        verify(artistRepository, times(1)).findById(1);
        verify(artistRepository, times(1)).save(testArtist);
    }

    @Test
    void convertToDto_ShouldHandleArtistWithVariousTrackCounts() {
        // This test verifies the convertToDto method indirectly through other operations

        // Test with multiple tracks
        when(artistRepository.findById(1)).thenReturn(Optional.of(testArtist));
        Optional<ArtistDto> result1 = artistService.getArtistById(1);
        assertTrue(result1.isPresent());
        assertEquals(2, result1.get().getTrackCount());

        // Test with no tracks
        Artist artistNoTracks = new Artist();
        artistNoTracks.setId(2);
        artistNoTracks.setName("No Tracks Artist");
        artistNoTracks.setTracks(new HashSet<>());

        when(artistRepository.findById(2)).thenReturn(Optional.of(artistNoTracks));
        Optional<ArtistDto> result2 = artistService.getArtistById(2);
        assertTrue(result2.isPresent());
        assertEquals(0, result2.get().getTrackCount());

        verify(artistRepository, times(1)).findById(1);
        verify(artistRepository, times(1)).findById(2);
    }
}