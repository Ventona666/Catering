package com.example.catering.Model;

import java.util.ArrayList;
import java.util.List;

public class RestaurantPhotos {

    private String nomRestaurant;

    private double lat;

    private double lon;

    private List<String> filePathPhotos = new ArrayList<>();

    public RestaurantPhotos(){}

    public String getNomRestaurant() {
        return nomRestaurant;
    }

    public void setNomRestaurant(String nomRestaurant) {
        this.nomRestaurant = nomRestaurant;
    }

    public List<String> getFilePathPhotos() {
        return filePathPhotos;
    }

    public void setFilePathPhotos(List<String> filePathPhotos) {
        this.filePathPhotos = filePathPhotos;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "RestaurantPhotos{" +
                "nomRestaurant='" + nomRestaurant + '\'' +
                "lat='" + lat + '\'' +
                "lon='" + lon + '\'' +
                ", filePathPhotos=" + filePathPhotos +
                '}';
    }
}
