package com.ice.registration.repository;

import com.ice.registration.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {

    List<Artist> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT a FROM Artist a JOIN a.tracks t WHERE t.id = :trackId")
    List<Artist> findByTrackId(@Param("trackId") Long trackId);

}