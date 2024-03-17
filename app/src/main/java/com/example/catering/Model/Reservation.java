package com.example.catering.Model;

import java.io.Serializable;
import java.util.Date;

public class Reservation implements Serializable {
    private String id;
    private String nom;
    private String prenom;
    private Integer nbPersonne;
    private Date date;
    private Long restaurantId;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Integer getNbPersonne() {
        return nbPersonne;
    }

    public void setNbPersonne(Integer nbPersonne) {
        this.nbPersonne = nbPersonne;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Reservation(String nom, String prenom, Integer nbPersonne, Date date, Long restaurantId) {
        this.nom = nom;
        this.prenom = prenom;
        this.nbPersonne = nbPersonne;
        this.date = date;
        this.restaurantId = restaurantId;
    }

    public Reservation(){};

    @Override
    public String toString() {
        return "Reservation{" +
                "nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", nbPersonne=" + nbPersonne +
                ", date=" + date +
                ", restaurantId=" + restaurantId +
                '}';
    }
}
