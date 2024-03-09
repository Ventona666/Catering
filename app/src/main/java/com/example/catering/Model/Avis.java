package com.example.catering.Model;

import java.io.Serializable;

public class Avis implements Serializable {

    private Long id;

    private String nomUtilisateur;

    private String commentaire;

    private Long restaurantId;

    public Avis() {}

    public Long getId() {
        return id;
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    @Override
    public String toString() {
        return "Avis{" +
                "id=" + id +
                ", nomUtilisateur='" + nomUtilisateur + '\'' +
                ", commentaire='" + commentaire + '\'' +
                ", restaurantId=" + restaurantId +
                '}';
    }
}
