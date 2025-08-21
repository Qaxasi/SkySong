package com.mycompany.SkySong.domain.music.playlist.algorytm.service;

import com.mycompany.SkySong.domain.music.playlist.algorytm.model.GenreWeight;
import com.mycompany.SkySong.domain.music.playlist.algorytm.model.GenresType;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class GenreScoringConfig {
    private static final Map<GenresType, GenreWeight> GENRE_FACTORS = new HashMap<>();

    static {
        List.of(
                GenreWeight.builder(GenresType.ELECTRONIC)
                        .tempoFactor(180.0)
                        .tempoWeight(0.5)
                        .energyWeight(0.5)
                        .build(),

                GenreWeight.builder(GenresType.DANCE)
                        .tempoFactor(160)
                        .tempoWeight(0.6)
                        .energyWeight(0.4)
                        .build(),

                GenreWeight.builder(GenresType.POP)
                        .tempoFactor(150)
                        .tempoWeight(0.3)
                        .energyWeight(0.7)
                        .acousticWeight(0.1)
                        .build(),

                GenreWeight.builder(GenresType.ROCK)
                        .tempoFactor(140)
                        .tempoWeight(0.4)
                        .energyWeight(0.6)
                        .build(),

                GenreWeight.builder(GenresType.METAL)
                        .tempoFactor(150)
                        .tempoWeight(0.3)
                        .energyWeight(0.7)
                        .moodWeight(-0.05)
                        .build(),

                GenreWeight.builder(GenresType.JAZZ)
                        .tempoFactor(120)
                        .tempoWeight(0.1)
                        .instrumentalWeight(0.85)
                        .build(),

                GenreWeight.builder(GenresType.BLUES)
                        .tempoFactor(100)
                        .tempoWeight(0.15)
                        .moodWeight(0.3)
                        .instrumentalWeight(0.5)
                        .build(),

                GenreWeight.builder(GenresType.CHILLOUT)
                        .moodWeight(0.5)
                        .acousticWeight(0.5)
                        .build(),

                GenreWeight.builder(GenresType.AMBIENT)
                        .moodWeight(0.7)
                        .acousticWeight(0.3)
                        .build(),

                GenreWeight.builder(GenresType.LOFI)
                        .tempoFactor(90)
                        .tempoWeight(0.1)
                        .moodWeight(0.8)
                        .acousticWeight(0.2)
                        .build(),

                GenreWeight.builder(GenresType.INDIE)
                        .tempoFactor(110)
                        .tempoWeight(0.3)
                        .moodWeight(0.7)
                        .instrumentalWeight(0.05)
                        .build(),

                GenreWeight.builder(GenresType.HIPHOP)
                        .tempoFactor(140)
                        .tempoWeight(0.5)
                        .energyWeight(0.5)
                        .build(),

                GenreWeight.builder(GenresType.REGGAE)
                        .tempoFactor(100)
                        .tempoWeight(0.3)
                        .energyWeight(0.7)
                        .build(),

                GenreWeight.builder(GenresType.CLASSICAL)
                        .moodWeight(0.1)
                        .instrumentalWeight(0.9)
                        .build(),

                GenreWeight.builder(GenresType.FOLK)
                        .moodWeight(0.5)
                        .instrumentalWeight(0.5)
                        .build(),

                GenreWeight.builder(GenresType.RB)
                        .tempoFactor(130)
                        .tempoWeight(0.4)
                        .energyWeight(0.6)
                        .build()
        ).forEach(factor -> GENRE_FACTORS.put(factor.getGenre(), factor));
    }

    public static Map<GenresType, GenreWeight> getGenreFactors() {
        return Collections.unmodifiableMap(GENRE_FACTORS);
    }

    public static GenreWeight getFactorsForGenre(GenresType genre) {
        return GENRE_FACTORS.get(genre);
    }
}