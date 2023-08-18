package com.bezkoder.spring.login.repository;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;

import com.bezkoder.spring.login.models.Gamme;

public interface GammeRepository extends JpaRepository<Gamme,Long> {
    boolean existsByRef(String ref);
}
