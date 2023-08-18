package com.bezkoder.spring.login.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bezkoder.spring.login.models.Chaine;

public interface ChaineRepository extends JpaRepository<Chaine,Long> {
    boolean existsByCode(String code);
}
