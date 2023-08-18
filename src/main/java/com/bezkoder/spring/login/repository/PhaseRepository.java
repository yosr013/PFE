package com.bezkoder.spring.login.repository;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;

import com.bezkoder.spring.login.models.Phase;


public interface PhaseRepository extends JpaRepository<Phase,Long> {
    boolean existsByNom(String nom);
}
