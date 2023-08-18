package com.bezkoder.spring.login.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.bezkoder.spring.login.repository.ModeleRepository;
import com.bezkoder.spring.login.Exception.ResourceNotFoundException;
import com.bezkoder.spring.login.models.Modele;

@RestController
@RequestMapping("api/v1")
@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials="true")
public class ModeleController {

    @Autowired
	private ModeleRepository modeleRepo;

    //get all modeles
	@PreAuthorize("hasRole('ADMIN') or hasRole('CHEF')")
		@GetMapping("/modeles")
		public List<Modele> getAllModeles(){
	        return modeleRepo.findAll();
	    }

        //create modele restApi
		@PreAuthorize("hasRole('ADMIN') or hasRole('CHEF')")
		@PostMapping("/modeles")
public ResponseEntity<String> creatModele(@RequestBody Modele modele) {
    if (modeleRepo.existsByDesignation(modele.getDesignation())) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Une designation similaire existe déjà.");
    }
    modeleRepo.save(modele);
    return ResponseEntity.status(HttpStatus.CREATED)
            .body("Modéle ajouté avec succès");
}



		//get modele by id restApi
        @PreAuthorize("hasRole('CHEF')")
		@GetMapping("/modeles/{id}")
	    public ResponseEntity<Modele> getModeleById(@PathVariable Long id){
	        Modele modele = modeleRepo.findById(id)
	                 .orElseThrow(()-> new ResourceNotFoundException("Modele introuvable avec l'identifiant : "+id));
	        return ResponseEntity.ok(modele);
	    }
		
	    //delete modele restApi
		@PreAuthorize("hasRole('CHEF')")
	    @DeleteMapping("/modeles/{id}")
	    public ResponseEntity<Map<String,Boolean>> deleteModele(@PathVariable Long id){
	        Modele modele=modeleRepo.findById(id)
	        .orElseThrow(()-> new ResourceNotFoundException("Modele introuvable avec l'identifiant:"+id));
            modeleRepo.delete(modele);
	        Map<String,Boolean> response = new HashMap<>();
	        response.put("supprimé avec succès",Boolean.TRUE);
	        return ResponseEntity.ok(response);
	    }


        //update modele restApi
		@PreAuthorize("hasRole('CHEF')")
		@PutMapping("/modeles/{id}")
public ResponseEntity<String> updateModele(@PathVariable Long id, @RequestBody Modele modeleDetails) {
    Optional<Modele> existingModeleOptional = modeleRepo.findById(id);
    if (existingModeleOptional.isEmpty()) {
        return ResponseEntity.notFound().build();
    }

    Modele existingModele = existingModeleOptional.get();
    String newDesignation = modeleDetails.getDesignation();

    // Vérifier si une autre modèle a déjà la même désignation (sauf le modèle en cours de modification)
    if (modeleRepo.existsByDesignationAndIdNot(newDesignation, id)) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Une désignation similaire existe déjà.");
    }

    // Mettre à jour les champs
    existingModele.setDesignation(newDesignation);
    existingModele.setDescription(modeleDetails.getDescription());

    // Sauvegarder les modifications
    modeleRepo.save(existingModele);

    return ResponseEntity.ok("Modèle modifié avec succès");
}





    
}
