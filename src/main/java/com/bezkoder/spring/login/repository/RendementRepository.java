package com.bezkoder.spring.login.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bezkoder.spring.login.models.Rendement;

public interface RendementRepository extends JpaRepository<Rendement,Long> {
    
}
