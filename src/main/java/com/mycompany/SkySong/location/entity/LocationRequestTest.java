
package com.mycompany.SkySong.location.entity;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.NotNull;

@Generated("jsonschema2pojo")
public class LocationRequestTest {

    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("latitude")
    @Expose
    @NotNull
    private Double latitude;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("longitude")
    @Expose
    @NotNull
    private Double longitude;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("locationName")
    @Expose
    @NotNull
    private String locationName;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("state")
    @Expose
    @NotNull
    private String state;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("country")
    @Expose
    @NotNull
    private String country;

    /**
     * 
     * (Required)
     * 
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     * 
     * (Required)
     * 
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /**
     * 
     * (Required)
     * 
     */
    public String getLocationName() {
        return locationName;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    /**
     * 
     * (Required)
     * 
     */
    public String getState() {
        return state;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * 
     * (Required)
     * 
     */
    public String getCountry() {
        return country;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setCountry(String country) {
        this.country = country;
    }

}
