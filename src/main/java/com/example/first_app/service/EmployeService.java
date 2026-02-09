// EmployeService.java

package com.example.first_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.first_app.model.Employe;
import com.example.first_app.model.Entreprise;
import com.example.first_app.model.Departement;
import com.example.first_app.repository.EmployeRepository;
import com.example.first_app.repository.EntrepriseRepository;
import com.example.first_app.repository.DepartementRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeService {

    @Autowired
    private EmployeRepository employeRepository;

    @Autowired
    private EntrepriseRepository entrepriseRepository;

    @Autowired
    private DepartementRepository departementRepository;

    /**
     * CREATE: Crée un nouvel employé dans la base de données
     * @param nom Le nom de l'employé
     * @param prenom Le prénom de l'employé
     * @param poste Le poste de l'employé
     * @param entrepriseId L'ID de l'entreprise
     * @param departementId L'ID du département
     * @return L'employé créé
     */
    public Employe createEmploye(String nom, String prenom, String poste, int entrepriseId, int departementId) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de l'employé ne peut pas être vide");
        }
        if (prenom == null || prenom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le prénom de l'employé ne peut pas être vide");
        }
        if (poste == null || poste.trim().isEmpty()) {
            throw new IllegalArgumentException("Le poste de l'employé ne peut pas être vide");
        }

        Optional<Entreprise> entrepriseOpt = entrepriseRepository.findById(entrepriseId);
        Optional<Departement> departementOpt = departementRepository.findById(departementId);

        if (entrepriseOpt.isEmpty()) {
            throw new RuntimeException("Entreprise non trouvée avec l'ID " + entrepriseId);
        }
        if (departementOpt.isEmpty()) {
            throw new RuntimeException("Département non trouvé avec l'ID " + departementId);
        }

        Departement departement = departementOpt.get();
        if (departement.getEntreprise().getId() != entrepriseId) {
            throw new RuntimeException("Le département ne fait pas partie de cette entreprise");
        }

        Employe employe = new Employe(nom, prenom, poste, entrepriseOpt.get(), departementOpt.get());
        return employeRepository.save(employe);
    }

    /**
     * READ: Récupère tous les employés de la base de données
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
     * UPDATE: Met à jour les informations d'un employé existant
     * @param matricule Le matricule de l'employé
     * @param nom Le nouveau nom
     * @param prenom Le nouveau prénom
     * @param poste Le nouveau poste
     * @return L'employé mis à jour
     */
    public Employe updateEmployeById(int matricule, String nom, String prenom, String poste) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de l'employé ne peut pas être vide");
        }
        if (prenom == null || prenom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le prénom de l'employé ne peut pas être vide");
        }
        if (poste == null || poste.trim().isEmpty()) {
            throw new IllegalArgumentException("Le poste de l'employé ne peut pas être vide");
        }

        Optional<Employe> employeOpt = employeRepository.findById(matricule);
        if (employeOpt.isPresent()) {
            Employe employe = employeOpt.get();
            employe.setNom(nom);
            employe.setPrenom(prenom);
            employe.setPoste(poste);
            return employeRepository.save(employe);
        } else {
            throw new RuntimeException("Employé non trouvé avec le matricule " + matricule);
        }
    }

    /**
     * DELETE: Supprime un employé de la base de données
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