package Model;

import java.io.Serializable;

public class Restaurant implements Serializable {

    private String nom;

    private String adresse;

    private int codePostal;

    private String description;

    private String mail;

    private String telephone;

    private double lat;

    private double lon;

    public Restaurant(){}

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(int codePostal) {
        this.codePostal = codePostal;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
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
        return "Restaurant{" +
                "nom='" + nom + '\'' +
                ", adresse='" + adresse + '\'' +
                ", codePosatl=" + codePostal +
                ", description='" + description + '\'' +
                ", mail='" + mail + '\'' +
                ", telephone='" + telephone + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}
