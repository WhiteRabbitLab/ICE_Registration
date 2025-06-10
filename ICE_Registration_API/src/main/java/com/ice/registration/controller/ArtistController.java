package com.ice.registration.controller;

import com.ice.registration.dto.ArtistDto;
import com.ice.registration.service.ArtistService;
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

}