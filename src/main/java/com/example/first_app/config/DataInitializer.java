package com.example.first_app.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.first_app.model.Entreprise;
import com.example.first_app.model.Departement;
import com.example.first_app.repository.EntrepriseRepository;
import com.example.first_app.repository.DepartementRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private EntrepriseRepository entrepriseRepository;

    @Autowired
    private DepartementRepository departementRepository;

    @Override
    @SuppressWarnings("null")
    public void run(String... args) throws Exception {

        // Ne pas supprimer les données existantes : ne créer que si aucune entreprise
        if (entrepriseRepository.count() == 0) {
            Entreprise e = new Entreprise("NSIA Banque", "Cocody Rue lycée mermoz");
            e = entrepriseRepository.save(e);

            Departement d1 = new Departement("Ressources Humaines", e);
            Departement d2 = new Departement("Informatique", e);
            Departement d3 = new Departement("Finance", e);

            departementRepository.saveAll(java.util.Arrays.asList(d1, d2, d3));
        }
    }
}
