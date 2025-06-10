package com.ice.registration.controller;

import com.ice.registration.dto.TrackDto;
import com.ice.registration.service.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tracks")
@CrossOrigin(origins = "http://localhost:3000")
public class TrackController {
    
    @Autowired
    private TrackService trackService;

    /**
     * POST /api/tracks - Create a new track
     */
    @PostMapping
    public ResponseEntity<TrackDto> createTrack(@RequestBody TrackDto trackDto) {
        try {
            TrackDto createdTrack = trackService.createTrack(trackDto);
            return ResponseEntity.ok(createdTrack);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}