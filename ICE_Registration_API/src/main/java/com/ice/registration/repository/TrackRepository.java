package com.ice.registration.repository;

import com.ice.registration.entity.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackRepository extends JpaRepository<Track, Long> {
    List<Track> findByTitleContainingIgnoreCase(String title);
    
    @Query("SELECT t FROM Track t JOIN t.artists a WHERE a.id = :artistId")
    List<Track> findByArtistId(@Param("artistId") Long artistId);
    
    @Query("SELECT t FROM Track t WHERE t.genre.id = :genreId")
    List<Track> findByGenreId(@Param("genreId") Long genreId);
}
