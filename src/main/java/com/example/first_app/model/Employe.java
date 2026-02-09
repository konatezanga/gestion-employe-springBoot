package com.example.first_app.model;

import jakarta.persistence.*;

@Entity //indique que la classe est une entité JPA
@Table(name = "employe") //lie la classe à la table employe

public class Employe {

    @Id //indique que l'attribut est la clé primaire
    @GeneratedValue(strategy = GenerationType.IDENTITY) //indique que la valeur est auto-générée
    private int matricule;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false)
    private String poste;

    @ManyToOne
    @JoinColumn(name = "entreprise_id", nullable = false)
    private Entreprise entreprise;

    @ManyToOne
    @JoinColumn(name = "departement_id", nullable = false)
    private Departement departement;

    public Employe() {
        // constructeur vide pour JPA
    }

    public Employe(String nom, String prenom, String poste, Entreprise entreprise, Departement departement) {
        this.nom = nom;
        this.prenom = prenom;
        this.poste = poste;
        this.entreprise = entreprise;
        this.departement = departement;
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

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getPoste() {
        return poste;
    }

    public void setPoste(String poste) {
        this.poste = poste;
    }

    public Entreprise getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(Entreprise entreprise) {
        this.entreprise = entreprise;
    }

    public Departement getDepartement() {
        return departement;
    }

    public void setDepartement(Departement departement) {
        this.departement = departement;
    }
}