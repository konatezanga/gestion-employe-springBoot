package com.example.first_app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.first_app.model.Departement;
import com.example.first_app.service.DepartementService;

@RestController
@RequestMapping("/api/departements")
public class DepartementRestController {

    @Autowired
    private DepartementService departementService;

    /**
     * Récupère tous les départements
     * GET /api/departements
     * Accessible par: ADMIN et EMPLOYEE (consultation)
     */
    @GetMapping
    @Secured({"ROLE_ADMIN", "ROLE_EMPLOYEE"})
    public List<Departement> getAllDepartements() {
        return departementService.getAllDepartements();
    }
}
