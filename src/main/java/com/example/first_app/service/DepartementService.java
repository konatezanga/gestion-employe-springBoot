package com.example.first_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.first_app.model.Departement;
import com.example.first_app.repository.DepartementRepository;

import java.util.List;
@Service
public class DepartementService {

    @Autowired
    private DepartementRepository departementRepository;

    /**
     * READ: Récupère tous les départements
     * @return Liste de tous les départements
     */
    public List<Departement> getAllDepartements() {
        return departementRepository.findAll();
    }
}
