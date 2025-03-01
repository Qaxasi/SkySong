package com.mycompany.SkySong.domain.music.playlist.algorytm.model;

public record AudioFeatures(
        double tempo,
        double energy,
        double mood,
        double instrumentalness,
        double acousticness
) {
}
