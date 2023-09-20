package com.mycompany.SkySong.location.controller;

import com.mycompany.SkySong.location.entity.LocationRequest;
import com.mycompany.SkySong.location.exception.NullOrEmptyInputException;
import com.mycompany.SkySong.location.exception.LocationServiceException;
import com.mycompany.SkySong.location.service.LocationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
                new LocationServiceException("Location Exception"));
        mockMvc.perform(get("/api/v1/location/coordinates/City"))
                .andExpect(status().is5xxServerError());
        verify(locationService).fetchAndSaveCoordinates(anyString());

    }

    @Test
    void shouldReturnStatusSuccessful() throws Exception {
        String locationName = "Test-Location";

        mockMvc.perform(get("/api/v1/location/coordinates/" + locationName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        verify(locationService).fetchAndSaveCoordinates(locationName);
    }

    @Test
    void shouldReturnBadRequestWhenLocationNameIsEmpty() throws Exception {
        String locationName = "";

        when(locationService.fetchAndSaveCoordinates(locationName))
                .thenThrow(new NullOrEmptyInputException("Test-Error"));

        mockMvc.perform(get("/api/v1/location/coordinates/" + locationName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    @Test
    void shouldReturnBadRequestWhenLocationNameIsNull() throws Exception {
        when(locationService.fetchAndSaveCoordinates(null))
                .thenThrow(new NullOrEmptyInputException("Test-Error"));

        mockMvc.perform(get("/api/location/coordinates/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnLocationDetailsForTestedCityWhenFetched() throws Exception {
        LocationRequest locationRequest = new LocationRequest("Kielce", 50.85403585,
                20.609914352101452, "PL", "Świętokrzyskie Voivodeship");

        when(locationService.fetchAndSaveCoordinates("Kielce")).thenReturn(locationRequest);

        mockMvc.perform(get("/api/v1/location/coordinates/Kielce")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Kielce"))
                .andExpect(jsonPath("$.lat").value(50.85403585))
                .andExpect(jsonPath("$.lon").value(20.609914352101452))
                .andExpect(jsonPath("$.country").value("PL"))
                .andExpect(jsonPath("$.state").value("Świętokrzyskie Voivodeship"));
    }

}
