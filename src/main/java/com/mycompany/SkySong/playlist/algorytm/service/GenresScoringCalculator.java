package com.mycompany.SkySong.domain.music.playlist.algorytm.service;

import com.mycompany.SkySong.domain.music.playlist.algorytm.model.AudioFeatures;
import com.mycompany.SkySong.domain.music.playlist.algorytm.model.GenreWeight;
import com.mycompany.SkySong.domain.music.playlist.algorytm.model.GenresType;


import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GenresScoringCalculator {

    public static List<GenresType> calculateTopGenres(AudioFeatures features) {
        Map<GenresType, Double> genresScores = new HashMap<>();

        for (GenresType genre : GenreScoringConfig.getGenreFactors().keySet()) {
            GenreWeight weight = GenreScoringConfig.getFactorsForGenre(genre);
            double score = calculateGenresScore(features, weight);
            genresScores.put(genre, score);
        }

        return genresScores.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private static double calculateGenresScore(AudioFeatures features, GenreWeight weights) {
        return (features.tempo() / weights.getTempoFactor()) * weights.getTempoWeight() +
                (features.energy() * weights.getEnergyWeight()) +
                (features.mood() * weights.getMoodWeight()) +
                (features.instrumentalness() * weights.getInstrumentalWeight()) +
                (features.acousticness() * weights.getAcousticWeight());
    }
}
