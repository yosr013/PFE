package com.bezkoder.spring.login.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;

import com.bezkoder.spring.login.models.Article;


public interface ArticleRepository extends JpaRepository<Article,Long> {

    Page<Article> findByModeleId(Long modeleId, Pageable pageable);

    Optional<Article> findByIdAndModeleId(Long id, Long modeleId);

    List<Article> findArticlesByTaillesId(Long tailleId);

    boolean existsByLibelle(String libelle);

    
}
