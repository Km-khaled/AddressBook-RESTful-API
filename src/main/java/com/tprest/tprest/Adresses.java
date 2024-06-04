package com.tprest.tprest;

import java.io.Serializable;

import org.json.JSONObject;



public class Adresses implements Serializable  {
    private String rue;
    private String ville;
    private Personnes personne;

    public Adresses(String rue, String ville, Personnes personne) {
        this.rue = rue;
        this.ville = ville;
        this.personne = personne;
    }

    public String getRue() {
        return rue;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public Personnes getPersonne() {
        return personne;
    }

    public void setPersonne(Personnes personne) {
        this.personne = personne;
    }

    @Override
    public String toString() {

    	StringBuilder sb = new StringBuilder();
        sb.append("rue:").append(rue).append(",");
        sb.append("ville:").append(ville).append(",");
        sb.append("personne.nom:").append(personne.getNom());
        return sb.toString();
    }

    public JSONObject toJSON() {

    	JSONObject json = new JSONObject();
        json.put("rue", rue);
        json.put("ville", ville);
        JSONObject personneJson = new JSONObject();
        personneJson.put("nom", personne.getNom());
        json.put("personne", personneJson);
        return json;
    }
    
}