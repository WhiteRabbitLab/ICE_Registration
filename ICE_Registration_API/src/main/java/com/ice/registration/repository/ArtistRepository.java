package com.ice.registration.repository;

import com.ice.registration.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Integer> {
    
    @Query("SELECT a FROM Artist a LEFT JOIN FETCH a.tracks")
    List<Artist> findAllWithTracks();

    @Query("SELECT a.id FROM Artist a ORDER BY a.id")
    List<Integer> findAllIds();
}