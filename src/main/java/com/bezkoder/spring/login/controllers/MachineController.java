package com.bezkoder.spring.login.controllers;

import java.util.List;
import java.util.Optional;

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
import org.springframework.http.HttpStatus;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.bezkoder.spring.login.models.Machine;
import com.bezkoder.spring.login.repository.ChaineRepository;
import com.bezkoder.spring.login.repository.MachineRepository;
import com.bezkoder.spring.login.Exception.ResourceNotFoundException;


@RestController
@RequestMapping("api/v1")
@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials="true")
public class MachineController {

    @Autowired
    private MachineRepository machineRepo;
  
    @Autowired
    private ChaineRepository chaineRepo;

    @PreAuthorize("hasRole('ADMIN') or hasRole('CHEF')")
    @GetMapping("/machines")
    public List<Machine> getAllMachines(){
        return machineRepo.findAll();
    }

    //create article restApi
    @PreAuthorize("hasRole('CHEF')")
    @PostMapping("/machines/{chaineId}")
public ResponseEntity<String> createMachine(@Valid @PathVariable(value = "chaineId") Long chaineId,
                                            @Valid @RequestBody Machine machine) {
    if (machineRepo.existsByDesignation(machine.getDesignation())) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Cette machine existe déjà !");
    }

    return chaineRepo.findById(chaineId).map(chaine -> {
        machine.setChaine(chaine);
        machineRepo.save(machine);
        return ResponseEntity.status(HttpStatus.CREATED).body("Machine ajoutée avec succès !");
    }).orElseThrow(() -> new ResourceNotFoundException("chaineId " + chaineId + " not found"));
}


    @GetMapping("/listemachines/{chaineId}")
	public Optional<Object> getAllMachinesByChaineId(@PathVariable(value = "chaineId") Long chaineId) {
		
		return chaineRepo.findById(chaineId).map(chaine -> {
			  return chaine.getMachines();
			});	
		
	}

        @PreAuthorize("hasRole('CHEF')")
        @GetMapping("/machines/{id}")
	    public ResponseEntity<Machine> getMachineById(@PathVariable Long id){
	        Machine machine = machineRepo.findById(id)
	                 .orElseThrow(()-> new ResourceNotFoundException("Machine introuvable avec l'identifiant : "+id));
	        return ResponseEntity.ok(machine);
	    }
    
}
