package com.example.first_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.first_app.model.Departement;

public interface DepartementRepository extends JpaRepository<Departement, Integer> {
}
