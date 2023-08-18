package com.bezkoder.spring.login.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;

import com.bezkoder.spring.login.models.Modele;


public interface ModeleRepository extends JpaRepository<Modele,Long> {
    boolean existsByDesignation(String designation);

    Optional<Modele> findByDesignation(String designation);
    
        boolean existsByDesignationAndIdNot(String designation, Long id);
    
    
}
