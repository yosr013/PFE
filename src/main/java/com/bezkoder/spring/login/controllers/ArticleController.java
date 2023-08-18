package com.bezkoder.spring.login.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.spring.login.Exception.ResourceNotFoundException;
import com.bezkoder.spring.login.models.Article;
import com.bezkoder.spring.login.repository.ArticleRepository;
import com.bezkoder.spring.login.repository.CommandeRepository;
import com.bezkoder.spring.login.repository.ModeleRepository;

//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("api/v1")
@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials="true")
public class ArticleController {

    @Autowired
    private ModeleRepository modeleRepo;
  
    @Autowired
    private ArticleRepository articleRepo;

    @Autowired
    private CommandeRepository commandeRepo;

    //create article restApi
    @PreAuthorize("hasRole('CHEF')")
    @PostMapping("/articles/{modeleId}")
public ResponseEntity<String> createArticle(@Valid @PathVariable(value = "modeleId") Long modeleId,
                                             @Valid @RequestBody Article article) {

    // Vérifier si le libellé de l'article existe déjà
    if (articleRepo.existsByLibelle(article.getLibelle())) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Cet article existe déjà");
    }

    return modeleRepo.findById(modeleId).map(modele -> {
        article.setModele(modele);
        Article savedArticle = articleRepo.save(article);
        return ResponseEntity.ok("Article créé avec succès");
    }).orElseThrow(() -> new ResourceNotFoundException("modeleId " + modeleId + " not found"));
}

    //get article by Id restApi
    
    @PreAuthorize("hasRole('CHEF') or hasRole('ADMIN')")
    @GetMapping("/articles/{id}")
	    public ResponseEntity<Article> getArticleById(@PathVariable Long id){
	        Article article = articleRepo.findById(id)
	                 .orElseThrow(()-> new ResourceNotFoundException("Article introuvable avec l'identifiant : "+id));
	        return ResponseEntity.ok(article);
	    }


    //get all articles
        @PreAuthorize("hasRole('ADMIN') or hasRole('CHEF')")
		@GetMapping("/articles")
		public List<Article> getAllArticles(){
	        return articleRepo.findAll();
	    }

   

    //update client restApi

        @PreAuthorize("hasRole('CHEF')")
		@PutMapping("/articles/{id}")
public ResponseEntity<String> updateArticle(@PathVariable Long id, @RequestBody Article articleDetails) {
    Article article = articleRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Article not found with id: " + id));

    // Vérifier si le libellé de l'article existe déjà
    if (articleRepo.existsByLibelle(articleDetails.getLibelle())) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Cet article existe déjà !");
    }

    return modeleRepo.findById(articleDetails.getModele().getId()).map(modele -> {
        article.setLibelle(articleDetails.getLibelle());
        article.setTheme(articleDetails.getTheme());
        article.setPrixFacon(articleDetails.getPrixFacon());
        article.setPrixFini(articleDetails.getPrixFini());
        article.setCouleur(articleDetails.getCouleur());
        article.setModele(modele);

        Article updatedArticle = articleRepo.save(article);
        return ResponseEntity.ok("Article mis à jour avec succès");
    }).orElseThrow(() -> new ResourceNotFoundException("modeleId " + articleDetails.getModele().getId() + " not found"));
}



   //delete modele restApi
   @PreAuthorize(" hasRole('CHEF')")
   @DeleteMapping("/articles/{id}")
   public ResponseEntity<Map<String,Boolean>>deleteArticle(@PathVariable Long id){
       Article article=articleRepo.findById(id)
       .orElseThrow(()-> new ResourceNotFoundException("Modele introuvable avec l'identifiant:"+id));
       articleRepo.delete(article);
       Map<String,Boolean> response = new HashMap<>();
       response.put("supprimé avec succès",Boolean.TRUE);
       return ResponseEntity.ok(response);
   }

@GetMapping("/articles/top5")
public List<String> getTopArticles() {
    // Créez l'objet Pageable avec une taille de page de 5 éléments et une page 0 (première page)
    Pageable pageable = PageRequest.of(0, 5);

    // Utilisez le repository de commandes pour récupérer les libellés des articles avec le nombre de commandes le plus élevé
    List<Object[]> topArticles = commandeRepo.findTopArticlesByCommandeCount(pageable);

    List<String> result = new ArrayList<>();
    for (Object[] row : topArticles) {
        String libelle = (String) row[0];
        result.add(libelle);
    }

    // Retournez la liste des libellés des articles
    return result;
}






    
}
