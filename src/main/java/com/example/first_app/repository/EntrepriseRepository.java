package com.example.first_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.first_app.model.Entreprise;

public interface EntrepriseRepository extends JpaRepository<Entreprise, Integer> {
}
