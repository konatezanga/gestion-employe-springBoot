// EmployeService.java

package com.example.first_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.first_app.model.Employe;
import com.example.first_app.repository.EmployeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeService {

    @Autowired
    private EmployeRepository employeRepository;

    /**
     * CREATE: Crée un nouvel employé dans la base de données H2
     * @param nom Le nom de l'employé
     * @return L'employé créé
     */
    public Employe createEmploye(String nom) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de l'employé ne peut pas être vide");
        }
        Employe employe = new Employe(nom);
        Employe savedEmploye = employeRepository.save(employe);
        return savedEmploye;
    }

    /**
     * READ: Récupère tous les employés de la base de données H2
     * @return Liste de tous les employés
     */
    public List<Employe> getAllEmployes() {
        return employeRepository.findAll();
    }

    /**
     * READ: Récupère un employé par son matricule
     * @param matricule Le matricule de l'employé
     * @return Un Optional contenant l'employé si trouvé, sinon vide
     */
    public Optional<Employe> getEmployeById(int matricule) {
        return employeRepository.findById(matricule);
    }

    /**
     * UPDATE: Met à jour le nom d'un employé existant
     * @param matricule Le matricule de l'employé
     * @param newNom Le nouveau nom
     * @return L'employé mis à jour
     */
    public Employe updateEmployeById(int matricule, String newNom) {
        if (newNom == null || newNom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de l'employé ne peut pas être vide");
        }
        Optional<Employe> employeOpt = employeRepository.findById(matricule);
        if (employeOpt.isPresent()) {
            Employe employe = employeOpt.get();
            employe.setNom(newNom);
            return employeRepository.save(employe);
        } else {
            throw new RuntimeException("Employé non trouvé avec le matricule " + matricule);
        }
    }

    /**
     * DELETE: Supprime un employé de la base de données H2
     * @param matricule Le matricule de l'employé à supprimer
     */
    public void deleteEmployeById(int matricule) {
        if (employeRepository.existsById(matricule)) {
            employeRepository.deleteById(matricule);
        } else {
            throw new RuntimeException("Employé non trouvé avec le matricule " + matricule);
        }
    }

    /**
     * Utility: Compte le nombre total d'employés
     * @return Le nombre d'employés
     */
    public long countEmployes() {
        return employeRepository.count();
    }
}