package com.ice.registration.controller;

import com.ice.registration.dto.ArtistDto;
import com.ice.registration.dto.TrackDto;
import com.ice.registration.service.ArtistService;
import com.ice.registration.service.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/artists")
@CrossOrigin(origins = "http://localhost:3000")
public class ArtistController {
    
    @Autowired
    private ArtistService artistService;

    @Autowired
    private TrackService trackService;

    /**
     * GET /api/artists - Get all artists
     */
    @GetMapping
    public ResponseEntity<List<ArtistDto>> getAllArtists() {
        List<ArtistDto> artists = artistService.getAllArtists();
        return ResponseEntity.ok(artists);
    }
    
    /**
     * GET /api/artists/{id} - Get artist by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ArtistDto> getArtistById(@PathVariable Integer id) {
        Optional<ArtistDto> artist = artistService.getArtistById(id);
        return artist.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/artists/{id}/tracks - Get tracks by artist ID
     */
    @GetMapping("/{id}/tracks")
    public ResponseEntity<List<TrackDto>> getTracksByArtistId(@PathVariable Integer id) {
        List<TrackDto> tracks = trackService.getTracksByArtistId(id);
        return ResponseEntity.ok(tracks);
    }

}