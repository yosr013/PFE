package com.bezkoder.spring.login.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.spring.login.models.Gamme;
import com.bezkoder.spring.login.repository.GammeRepository;

@RestController
@RequestMapping("api/v1")
@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials="true")
public class GammeController {

    @Autowired
    private GammeRepository gammeRepo;


    @PreAuthorize("hasRole('CHEF')")
    @PostMapping("/gammes")
public ResponseEntity<?> createGamme(@RequestBody Gamme gamme) {
    // Vérifier si la référence de la gamme existe déjà
    if (gammeRepo.existsByRef(gamme.getRef())) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La référence de la gamme existe déjà.");
    }

    // Vérifier si le temps est négatif
    if (gamme.getTemps() < 0) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Le temps ne peut pas être négatif.");
    }

    // Si tout est en ordre, enregistrer la gamme
    Gamme savedGamme = gammeRepo.save(gamme);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedGamme);
}

    @PreAuthorize("hasRole('ADMIN') or hasRole('CHEF')")
    @GetMapping("/gammes")
    public List<Gamme> getAllGammes(){
        return gammeRepo.findAll();
    }
    
}
