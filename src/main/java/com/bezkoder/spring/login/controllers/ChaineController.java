package com.bezkoder.spring.login.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.spring.login.Exception.ResourceNotFoundException;
import com.bezkoder.spring.login.models.Chaine;
import com.bezkoder.spring.login.repository.ChaineRepository;
//import com.bezkoder.spring.login.repository.MachineRepository;



@RestController
@RequestMapping("api/v1")
@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials="true")
public class ChaineController {
    
    @Autowired
    private ChaineRepository chaineRepo;
    

    //get all modeles
	@PreAuthorize("hasRole('ADMIN') or hasRole('CHEF')")
    @GetMapping("/chaines")
    public List<Chaine> getAllChaines(){
        return chaineRepo.findAll();
    }

    //create chaine restApi
    @PreAuthorize("hasRole('CHEF')")
    @PostMapping("/chaines")
    public ResponseEntity<String> createChaine(@RequestBody Chaine chaine) {
        if (chaineRepo.existsByCode(chaine.getCode())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Cette chaine existe déjà.");
        }
        chaineRepo.save(chaine);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Chaine ajouté avec succès");
    }

    @PreAuthorize("hasRole('CHEF')")
    @GetMapping("/chaines/{id}")
	    public ResponseEntity<Chaine> getChaineById(@PathVariable Long id){
	        Chaine chaine = chaineRepo.findById(id)
	                 .orElseThrow(()-> new ResourceNotFoundException("Chaine introuvable avec l'identifiant : "+id));
	        return ResponseEntity.ok(chaine);
	    }

        
		
	    //delete chaine restApi
        @PreAuthorize("hasRole('CHEF')")
	    @DeleteMapping("/chaines/{id}")
	    public ResponseEntity<Map<String,Boolean>> deleteChaine(@PathVariable Long id){
	        Chaine chaine=chaineRepo.findById(id)
	        .orElseThrow(()-> new ResourceNotFoundException("chaine introuvable avec l'identifiant:"+id));
            chaineRepo.delete(chaine);
	        Map<String,Boolean> response = new HashMap<>();
	        response.put("supprimé avec succès",Boolean.TRUE);
	        return ResponseEntity.ok(response);
	    }


        
		
    
}
