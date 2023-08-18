package com.bezkoder.spring.login.repository;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;

import com.bezkoder.spring.login.models.Employe;

//@Repository
public interface EmployeRepository extends JpaRepository<Employe,Long> {
    boolean existsByMatricule(String matricule);
}
