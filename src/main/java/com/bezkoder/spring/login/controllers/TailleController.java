package com.bezkoder.spring.login.controllers;

import java.util.List;
import java.util.Optional;

//import java.util.List;

//import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.spring.login.Exception.ResourceNotFoundException;
import com.bezkoder.spring.login.models.Article;
import com.bezkoder.spring.login.models.Taille;
import com.bezkoder.spring.login.repository.ArticleRepository;
import com.bezkoder.spring.login.repository.TailleRepository;

@RestController
@RequestMapping("api/v1")
@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials="true")
public class TailleController {

    @Autowired
	private TailleRepository tailleRepo;

    @Autowired
    private ArticleRepository articleRepo;

    @PreAuthorize("hasRole('ADMIN') or hasRole('CHEF')")
		@GetMapping("/taille")
		public List<Taille> getAllTailles(){
	        return tailleRepo.findAll();
	    }

      @PreAuthorize("hasRole('CHEF')")
      @PostMapping("/taille/{articleId}")
public ResponseEntity<String> addTaille(@PathVariable(value = "articleId") Long articleId, @RequestBody Taille tailleRequest) {
    Optional<Article> optionalArticle = articleRepo.findById(articleId);
    if (optionalArticle.isPresent()) {
        Article article = optionalArticle.get();
        // Vérifier si une taille avec la même désignation existe déjà pour l'article
        boolean tailleExists = article.getTailles().stream()
                .anyMatch(t -> t.getDesignation().equalsIgnoreCase(tailleRequest.getDesignation()));
        if (tailleExists) {
            return new ResponseEntity<>("Une taille avec la même désignation existe déjà pour cet article.", HttpStatus.BAD_REQUEST);
        }

        // Ajouter la nouvelle taille à l'article
        article.addTaille(tailleRequest);
        tailleRepo.save(tailleRequest);

        return new ResponseEntity<>("Taille ajoutée avec succès.", HttpStatus.CREATED);
    } else {
        throw new ResourceNotFoundException("Article introuvable avec l'identifiant : " + articleId);
    }
}

    

    

    
}
