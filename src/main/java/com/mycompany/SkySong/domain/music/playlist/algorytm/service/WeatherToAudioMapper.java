package com.mycompany.SkySong.domain.music.playlist.algorytm.service;

import com.mycompany.SkySong.domain.music.playlist.algorytm.model.AudioFeatures;

class WeatherToAudioMapper {

    public AudioFeatures calculateAudioFeatures(double temperature, double humidity, double windSpeed, double cloudiness,
                                                double rainVolume, boolean isNight, boolean isSnowing) {

        double tempo = calculateTempo(temperature, humidity, windSpeed, isSnowing);
        double energy = calculateEnergy(humidity, windSpeed, rainVolume);
        double mood = calculateMood(cloudiness, isNight);
        double instrumentalness = calculateInstrumentalness(humidity, windSpeed, cloudiness, rainVolume);
        double acousticness = calculateAcousticness(temperature, humidity, cloudiness, isSnowing);

        return new AudioFeatures(tempo, energy, mood, instrumentalness, acousticness);
    }

    private double calculateTempo(double temperature, double humidity, double windSpeed, boolean isSnowing) {
        final double BASE_TEMPO = 120.0;
        final double NEUTRAL_TEMPERATURE = 20.0;
        final double TEMPERATURE_SCALE = 1.5;
        final double HUMIDITY_SCALE = 0.1;
        final double WIND_SCALE = 1.0;
        final double MIN_TEMPO = 60.0;
        final double MAX_TEMPO = 180.0;

        final double COLD_TEMPERATURE_THRESHOLD = 0.0;
        final double SNOW_TEMPO_SLOWDOWN = 10.0;
        final double MAX_COLD_TEMPO_SLOWDOWN = 15.0;

        double tempo = BASE_TEMPO +
                       (temperature - NEUTRAL_TEMPERATURE) * TEMPERATURE_SCALE -
                       (humidity * HUMIDITY_SCALE) +
                       (windSpeed * WIND_SCALE);

        if (temperature < COLD_TEMPERATURE_THRESHOLD) {
            tempo -= Math.min(Math.abs(temperature) * TEMPERATURE_SCALE, MAX_COLD_TEMPO_SLOWDOWN);
        }

        if (isSnowing) {
            tempo -= SNOW_TEMPO_SLOWDOWN;
        }

        return  clamp(tempo, MIN_TEMPO, MAX_TEMPO);
    }

    private double calculateEnergy(double humidity, double windSpeed, double rainVolume) {
        final double BASE_ENERGY = 1.0;
        final double HUMIDITY_SCALE = 0.01;
        final double WIND_SCALE = 0.05;
        final double ENERGY_MIN = 0.0;
        final double ENERGY_MAX = 1.0;

        final double HIGH_WIND_THRESHOLD = 15.0;
        final double HEAVY_RAIN_THRESHOLD = 5.0;
        final double STORM_IMPACT = 0.3;
        final double HIGH_WIND_IMPACT= 0.2;

        double energy = BASE_ENERGY - (humidity * HUMIDITY_SCALE) + (windSpeed * WIND_SCALE);

        if (windSpeed > HIGH_WIND_THRESHOLD && rainVolume > HEAVY_RAIN_THRESHOLD) {
            energy -= STORM_IMPACT;
        } else if (windSpeed > HIGH_WIND_THRESHOLD) {
            energy -= HIGH_WIND_IMPACT;
        }

        return clamp(energy, ENERGY_MIN, ENERGY_MAX);
    }

    private double calculateMood(double cloudiness, boolean isNight) {
        final double BASE_MOOD = 1.0;
        final double CLOUDINESS_SCALE = 0.01;
        final double DAYTIME_SCALE = 1.0;
        final double NIGHT_SCALE = 0.7;
        final double MOOD_MIN = 0.0;
        final double MOOD_MAX = 1.0;

        double mood = (BASE_MOOD - (cloudiness * CLOUDINESS_SCALE)) *
                      (isNight ? NIGHT_SCALE : DAYTIME_SCALE);

        return clamp(mood, MOOD_MIN, MOOD_MAX);
    }

    private double calculateInstrumentalness(double humidity, double windSpeed, double cloudiness, double rainVolume) {
        final double MAX_HUMIDITY = 100.0;
        final double MAX_CLOUDINESS = 100.0;
        final double HUMIDITY_SCALE = 0.5;
        final double CLOUDINESS_SCALE = 0.3;
        final double WIND_SCALE = 0.2;
        final double WIND_SPEED_NORMALIZATION_FACTOR = 20.0;

        final double HEAVY_RAIN_THRESHOLD = 5.0;
        final double LIGHT_RAIN_THRESHOLD = 0.1;

        final double INSTRUMENTALNESS_MIN = 0.0;
        final double INSTRUMENTALNESS_MAX = 1.0;

        double instrumentalness = (humidity / MAX_HUMIDITY) * HUMIDITY_SCALE +
                                  (cloudiness / MAX_CLOUDINESS) * CLOUDINESS_SCALE +
                                  (windSpeed / WIND_SPEED_NORMALIZATION_FACTOR) * WIND_SCALE;

        if (rainVolume > HEAVY_RAIN_THRESHOLD) {
            instrumentalness += 0.3;
        } else if (rainVolume > LIGHT_RAIN_THRESHOLD) {
            instrumentalness += 0.1;
        }

        return clamp(instrumentalness, INSTRUMENTALNESS_MIN, INSTRUMENTALNESS_MAX);
    }

    private double calculateAcousticness(double temperature, double humidity, double cloudiness, boolean isSnowing) {
        final double MAX_HUMIDITY = 100.0;
        final double MAX_CLOUDINESS = 100.0;
        final double HUMIDITY_SCALE = 1.0;
        final double CLOUDINESS_SCALE = 0.5;

        final double COLD_TEMPERATURE_THRESHOLD = 0.0;
        final double LOW_HUMIDITY_THRESHOLD = 50.0;

        final double ACOUSTICNESS_MIN = 0.0;
        final double ACOUSTICNESS_MAX = 1.0;

        double acousticness = (humidity / MAX_HUMIDITY) * HUMIDITY_SCALE +
                              (cloudiness / MAX_CLOUDINESS) * CLOUDINESS_SCALE;

        if (temperature < COLD_TEMPERATURE_THRESHOLD && humidity < LOW_HUMIDITY_THRESHOLD) {
            acousticness += 0.2;
        }

        if (isSnowing) {
            acousticness += 0.2;
        }

        return clamp(acousticness, ACOUSTICNESS_MIN, ACOUSTICNESS_MAX);
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
