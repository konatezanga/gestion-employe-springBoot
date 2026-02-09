package com.example.first_app.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;

import com.example.first_app.model.Entreprise;
import com.example.first_app.service.EntrepriseService;

@RestController
@RequestMapping("/api/entreprises")
public class EntrepriseRestController {

    @Autowired
    private EntrepriseService entrepriseService;

    /**
     * Récupère l'unique entreprise
     * GET /api/entreprises
     * Accessible par: ADMIN et EMPLOYEE (consultation)
     */
    @GetMapping
    @Secured({"ROLE_ADMIN", "ROLE_EMPLOYEE"})
    public ResponseEntity<?> getEntreprise() {
        Optional<Entreprise> entreprise = entrepriseService.getEntreprise();
        if (entreprise.isPresent()) {
            return ResponseEntity.ok(entreprise.get());
        }
        return ResponseEntity.notFound().build();
    }

}
