package com.ice.registration.repository;

import com.ice.registration.entity.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrackRepository extends JpaRepository<Track, Integer> {
    
    @Query("SELECT t FROM Track t JOIN t.artists a WHERE a.id = :artistId")
    List<Track> findByArtistId(@Param("artistId") Integer artistId);
    
    @Query("SELECT t FROM Track t JOIN FETCH t.genre WHERE t.id = :id")
    Optional<Track> findByIdWithGenre(@Param("id") Integer id);
    
    @Query("SELECT t FROM Track t JOIN FETCH t.genre JOIN t.artists a WHERE a.id = :artistId")
    List<Track> findByArtistIdWithGenre(@Param("artistId") Integer artistId);
}