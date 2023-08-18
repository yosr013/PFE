package com.bezkoder.spring.login.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.spring.login.Exception.ResourceNotFoundException;
import com.bezkoder.spring.login.models.Phase;
import com.bezkoder.spring.login.repository.GammeRepository;
import com.bezkoder.spring.login.repository.PhaseRepository;

@RestController
@RequestMapping("api/v1")
@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials="true")
public class PhaseController {


    @Autowired
    private PhaseRepository phaseRepo;

    @Autowired
    private GammeRepository gammeRepo;

    //get all phases
    @PreAuthorize("hasRole('ADMIN') or hasRole('CHEF')")
    @GetMapping("/phases")
    public List<Phase> getAllPhases(){
        return phaseRepo.findAll();
    }

    @PreAuthorize("hasRole('CHEF')")
    @PostMapping("/phases/{gammeId}")
public ResponseEntity<String> createPhase(@Valid @PathVariable(value = "gammeId") Long gammeId,
                                          @Valid @RequestBody Phase phase) {
    if (phaseRepo.existsByNom(phase.getNom())) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Cette phase existe déjà !");
    }

    return gammeRepo.findById(gammeId).map(gamme -> {
        phase.setGamme(gamme);
        phaseRepo.save(phase);
        return ResponseEntity.status(HttpStatus.CREATED).body("Phase ajoutée avec succès !");
    }).orElseThrow(() -> new ResourceNotFoundException("gammeId " + gammeId + " not found"));
}

    
}
