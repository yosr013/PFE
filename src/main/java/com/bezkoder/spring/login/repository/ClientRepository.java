package com.bezkoder.spring.login.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bezkoder.spring.login.models.Client;

public interface ClientRepository extends JpaRepository<Client,Long> {

    boolean existsByRaisonSociale(String raisonSociale);
    
}
