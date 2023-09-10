package com.mycompany.SkySong.location.controller;

import com.mycompany.SkySong.exception.ValidationException;
import com.mycompany.SkySong.location.exception.LocationException;
import com.mycompany.SkySong.location.exception.LocationNotGiven;
import com.mycompany.SkySong.location.service.LocationService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LocationController.class)
class LocationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private LocationService locationService;

    @Test
    void shouldReturnLocationExceptionDuringFetchAndSaveCoordinates() throws Exception {
        when(locationService.fetchAndSaveCoordinates(anyString())).thenThrow(
                new LocationException("Location Exception"));
        mockMvc.perform(get("/api/location/coordinates/Warszawa"))
                .andExpect(status().is5xxServerError());
        verify(locationService).fetchAndSaveCoordinates(anyString());

    }

    @Test
    void shouldReturnGeocodingLocation() throws Exception {
        String locationName = "Kraków";

        mockMvc.perform(get("/api/location/coordinates/" + locationName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        verify(locationService).fetchAndSaveCoordinates(locationName);
    }

    @Test
    void shouldReturnBadRequestWhenLocationNameIsEmpty() throws Exception {
        String locationName = "";

        when(locationService.fetchAndSaveCoordinates(locationName))
                .thenThrow(new LocationNotGiven("Location name cannot be empty"));

        mockMvc.perform(get("/api/location/coordinates/" + locationName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    @Test
    void shouldReturnBadRequestWhenLocationNameIsNull() throws Exception {
        when(locationService.fetchAndSaveCoordinates(null))
                .thenThrow(new LocationNotGiven("Location name cannot be null"));

        mockMvc.perform(get("/api/location/coordinates/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
