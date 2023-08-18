package com.bezkoder.spring.login.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bezkoder.spring.login.models.Taille;

public interface TailleRepository extends JpaRepository<Taille,Long> {

    List<Taille> findTaillesByArticlesId(Long articleId);
    
}
