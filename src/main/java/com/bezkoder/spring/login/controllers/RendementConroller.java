package com.bezkoder.spring.login.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.spring.login.Exception.ResourceNotFoundException;
import com.bezkoder.spring.login.models.Rendement;
import com.bezkoder.spring.login.repository.EmployeRepository;
import com.bezkoder.spring.login.repository.RendementRepository;

@RestController
@RequestMapping("api/v1")
@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials="true")
public class RendementConroller {

    @Autowired
    private RendementRepository rendementRepo;
  
    @Autowired
    private EmployeRepository employeRepo;


    @PreAuthorize("hasRole('ADMIN') or hasRole('CHEF')")
    @GetMapping("/rendement")
    public List<Rendement> getAllRendements(){
        return rendementRepo.findAll();
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/rendement/{employeId}")
    public Rendement createRendement(@Valid @PathVariable (value = "employeId") Long employeId,
                                 @Valid @RequestBody Rendement rendement) {
        return employeRepo.findById(employeId).map(employe -> {
            rendement.setEmploye(employe);
            return rendementRepo.save(rendement);
        }).orElseThrow(() -> new ResourceNotFoundException("employeId " + employeId + " not found"));
    }
    
}
