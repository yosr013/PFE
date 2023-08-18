package com.bezkoder.spring.login.repository;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


//import com.bezkoder.spring.login.models.Article;
import com.bezkoder.spring.login.models.Commande;

public interface CommandeRepository extends JpaRepository<Commande,Long> {

    List<Commande> findByEtatNot(int etat);
    int countByEtat(int etat);

    @Query(value = "SELECT c.article.libelle FROM Commande c GROUP BY c.article.libelle ORDER BY COUNT(c.article.libelle) DESC")
    List<String> findMostOrderedArticleLabel();

    @Query(value = "SELECT COUNT(c) FROM Commande c WHERE c.article.id = :articleId")
    int findMostOrderedArticleCount(@Param("articleId") Long articleId);

    //cette méthode permet de récupérer les IDs des articles les plus commandés dans la base de données, en les triant par ordre décroissant du nombre de commandes
    @Query(value = "SELECT c.article.id FROM Commande c GROUP BY c.article.id ORDER BY COUNT(c) DESC")
List<Long> findMostOrderedArticleIds(org.springframework.data.domain.Pageable pageable);



@Query("SELECT c.article.libelle, COUNT(c) " +
       "FROM Commande c " +
       "GROUP BY c.article.libelle " +
       "ORDER BY COUNT(c) DESC ")
List<Object[]> findTopArticlesByCommandeCount(Pageable pageable);

boolean existsByRef(String ref);



}
