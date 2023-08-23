package com.mycompany.SkySong.repository;

import com.mycompany.SkySong.entity.Track;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackDAO extends JpaRepository<Track, Integer> {

    Track findTrackById(int TrackId);
}
