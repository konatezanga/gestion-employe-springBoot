package com.example.first_app.controller;

import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.example.first_app.model.Employe;
import com.example.first_app.service.EmployeService;

@RestController
@RequestMapping("/api/employes")
public class EmployeRestController {

    @Autowired
    private EmployeService employeService;

    /**
     * Récupère les informations de l'utilisateur courant
     * GET /api/employes/current-user
     * Retourne le username et les rôles
     */
    @GetMapping("/current-user")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("username", authentication.getName());
        
        // Récupérer les rôles de l'utilisateur
        List<String> roles = authentication.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .toList();
        
        userInfo.put("roles", roles);
        userInfo.put("isAdmin", roles.contains("ROLE_ADMIN"));
        
        return ResponseEntity.ok(userInfo);
    }

    /**
     * Récupère la liste de tous les employés
     * GET /api/employes
     * Accessible par: ADMIN et EMPLOYEE
     */
    @GetMapping
    @Secured({"ROLE_ADMIN", "ROLE_EMPLOYEE"})
    public List<Employe> getAllEmployes() {
        return employeService.getAllEmployes();
    }

    /**
     * Récupère un employé par son id
     * GET /api/employes/{matricule}
     * Accessible par: ADMIN et EMPLOYEE
     */
    @GetMapping("/{matricule}")
    @Secured({"ROLE_ADMIN", "ROLE_EMPLOYEE"})
    public Optional<Employe> getEmployeById(@PathVariable int matricule) {
        return employeService.getEmployeById(matricule);
    }

    /**
     * Ajoute un nouvel employé
     * POST /api/employes
     * Accessible par: ADMIN uniquement
     */
    @PostMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> createEmploye(@RequestBody Employe employe) {
        try {
            Employe newEmploye = employeService.createEmploye(employe.getNom());
            return ResponseEntity.status(HttpStatus.CREATED).body(newEmploye);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse("Vous n'avez pas les permissions pour créer un employé"));
        }
    }

    /**
     * Met à jour un employé existant
     * PUT /api/employes/{matricule}
     * Accessible par: ADMIN uniquement
     */
    @PutMapping("/{matricule}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> updateEmploye(@PathVariable int matricule, @RequestBody Employe employe) {
        try {
            Employe updatedEmploye = employeService.updateEmployeById(matricule, employe.getNom());
            return ResponseEntity.ok(updatedEmploye);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse("Vous n'avez pas les permissions pour modifier un employé"));
        }
    }

    /**
     * Supprime un employé
     * DELETE /api/employes/{matricule}
     * Accessible par: ADMIN uniquement
     */
    @DeleteMapping("/{matricule}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> deleteEmploye(@PathVariable int matricule) {
        try {
            employeService.deleteEmployeById(matricule);
            return ResponseEntity.ok(new SuccessResponse("Employé supprimé avec succès"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse("Vous n'avez pas les permissions pour supprimer un employé"));
        }
    }

    /**
     * Classe interne pour les réponses d'erreur
     */
    public static class ErrorResponse {
        public String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    /**
     * Classe interne pour les réponses de succès
     */
    public static class SuccessResponse {
        public String message;

        public SuccessResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
