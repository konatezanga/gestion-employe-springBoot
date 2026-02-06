package com.example.first_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.first_app.model.Employe;

public interface EmployeRepository extends JpaRepository<Employe, Integer> {
}