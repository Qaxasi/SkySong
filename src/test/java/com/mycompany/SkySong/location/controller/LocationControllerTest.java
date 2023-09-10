package com.mycompany.SkySong.location.controller;

import com.mycompany.SkySong.location.exception.LocationException;
import com.mycompany.SkySong.location.service.LocationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
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

    
}
