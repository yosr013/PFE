package com.bezkoder.spring.login.repository;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;

import com.bezkoder.spring.login.models.Operation;


public interface OperationRepository extends JpaRepository<Operation,Long> {
    boolean existsByNom(String nom);
}
