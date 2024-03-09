package com.example.catering.Model;

import java.io.Serializable;

public class Avis implements Serializable {

    private String id;

    private String nomUtilisateur;

    private String commentaire;

    private Integer note;

    private Long restaurantId;

    public Avis() {}

    public Avis(String nomUtilisateur, String commentaire, Long restaurantId) {
        this.nomUtilisateur = nomUtilisateur;
        this.commentaire = commentaire;
        this.restaurantId = restaurantId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Integer getNote() {
        return note;
    }

    public void setNote(Integer note) {
        this.note = note;
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
                ", note='" + note + '\'' +
                ", restaurantId=" + restaurantId +
                '}';
    }
}
