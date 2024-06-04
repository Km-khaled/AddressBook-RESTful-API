package com.tprest.tprest;

import java.io.Serializable;



public class Personnes implements Serializable  {

    @Override
	public String toString() {
		return  nom ;
	}

	private String nom;

    public Personnes(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
