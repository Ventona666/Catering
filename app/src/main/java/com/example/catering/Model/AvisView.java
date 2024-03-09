package com.example.catering.Model;

public class AvisView {
    private String nomUtilisateur;
    private String commentaire;

    public AvisView(String nomUtilisateur, String commentaire) {
        this.nomUtilisateur = nomUtilisateur;
        this.commentaire = commentaire;
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

    @Override
    public String toString() {
        return nomUtilisateur + "\n" + commentaire;
    }
}
