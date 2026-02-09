package com.example.first_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.first_app.model.Entreprise;
import com.example.first_app.repository.EntrepriseRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EntrepriseService {

    @Autowired
    private EntrepriseRepository entrepriseRepository;

    
    /**
     * READ: Récupère l'unique entreprise
     * @return L'entreprise si elle existe
     */
    public Optional<Entreprise> getEntreprise() {
        List<Entreprise> entreprises = entrepriseRepository.findAll();
        if (entreprises.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(entreprises.get(0));
    }
   
}
