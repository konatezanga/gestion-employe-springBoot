package com.example.first_app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;


@Entity //indique que la classe est une entité JPA
@Table(name = "employe") //lie la classe à la table employe

public class Employe {

    @Id //indique que l'attribut est la clé primaire
    @GeneratedValue(strategy = GenerationType.IDENTITY) //indique que la valeur est auto-générée
    private int matricule;

    private String nom;

    public Employe() {
        // constructeur vide pour JPA
    }

    public Employe(String nom) {
        this.nom = nom;
    }

    public int getMatricule() {
        return matricule;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}