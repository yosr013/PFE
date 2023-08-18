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

import com.bezkoder.spring.login.Exception.ResourceNotFoundException;
import com.bezkoder.spring.login.models.Client;
import com.bezkoder.spring.login.models.Employe;
import com.bezkoder.spring.login.models.Machine;
import com.bezkoder.spring.login.models.Operation;
import com.bezkoder.spring.login.models.Phase;
import com.bezkoder.spring.login.repository.MachineRepository;
import com.bezkoder.spring.login.repository.OperationRepository;
import com.bezkoder.spring.login.repository.PhaseRepository;

@RestController
@RequestMapping("api/v1")
@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials="true")
public class OperationController {

    @Autowired
    private OperationRepository operationRepo;
  
    @Autowired
    private MachineRepository machineRepo;

    @Autowired
    private PhaseRepository phaseRepo;

    @PreAuthorize("hasRole('CHEF')")
    @PostMapping("/operation/{phaseId}/{machineId}")
public ResponseEntity<String> createOperation(@PathVariable(value = "phaseId") Long phaseId,
                                              @PathVariable(value = "machineId") Long machineId,
                                              @RequestBody Operation operationRequest) {
    Optional<Phase> phaseOptional = phaseRepo.findById(phaseId);
    Optional<Machine> machineOptional = machineRepo.findById(machineId);

    if (phaseOptional.isPresent() && machineOptional.isPresent()) {
        Phase phase = phaseOptional.get();
        Machine machine = machineOptional.get();

        String operationNom = operationRequest.getNom();
        float operationTemps = operationRequest.getTemps();

        // Vérifier si le nom de l'opération existe déjà
        if (operationRepo.existsByNom(operationNom)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Cette opération existe déjà !");
        }

        // Vérifier si le temps de l'opération est négatif
        if (operationTemps < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Le temps de l'opération ne peut pas être négatif !");
        }

        operationRequest.setPhase(phase);
        operationRequest.setMachine(machine);
        Operation operation = operationRepo.save(operationRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body("Opération ajoutée avec succès !");
    }

    return ResponseEntity.notFound().build();
}

@PreAuthorize("hasRole('CHEF')")
@PutMapping("/operation/{operationId}")
public ResponseEntity<String> updateOperation(@PathVariable(value = "operationId") Long operationId,
                                              @RequestBody Operation operationRequest) {
    Optional<Operation> operationOptional = operationRepo.findById(operationId);

    if (operationOptional.isPresent()) {
        Operation operation = operationOptional.get();

        String operationNom = operationRequest.getNom();
        float operationTemps = operationRequest.getTemps();

        // Vérifier si le nom de l'opération existe déjà (sauf si c'est le nom actuel de l'opération)
        if (!operation.getNom().equals(operationNom) && operationRepo.existsByNom(operationNom)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Cette opération existe déjà !");
        }

        // Vérifier si le temps de l'opération est négatif
        if (operationTemps < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Le temps de l'opération ne peut pas être négatif !");
        }

        // Mettre à jour les propriétés de l'opération
        operation.setNom(operationNom);
        operation.setTemps(operationTemps);
        // Mettez à jour d'autres propriétés de l'opération si nécessaire

        operation = operationRepo.save(operation);

        return ResponseEntity.status(HttpStatus.OK).body("Opération modifiée avec succès !");
    }

    return ResponseEntity.notFound().build();
}


    

    //get all operations 
    @PreAuthorize("hasRole('ADMIN') or hasRole('CHEF')")
    @GetMapping("/operations")
    public List<Operation> getAllOperations(){
        return operationRepo.findAll();
    }
		
    @PreAuthorize("hasRole('CHEF')")
		@GetMapping("/operations/{id}")
	    public ResponseEntity<Operation> getOperationById(@PathVariable Long id){
	        Operation operation = operationRepo.findById(id)
	                 .orElseThrow(()-> new ResourceNotFoundException("Opération introuvable avec l'identifiant : "+id));
	        return ResponseEntity.ok(operation);
	    }
	
        @PreAuthorize("hasRole('CHEF')")
    @DeleteMapping("/operations/{id}")
    public ResponseEntity<Map<String,Boolean>> deleteOperation(@PathVariable Long id){
        Operation operation=operationRepo.findById(id)
        .orElseThrow(()-> new ResourceNotFoundException("Opération introuvable avec l'identifiant:"+id));
        operationRepo.delete(operation);
        Map<String,Boolean> response = new HashMap<>();
        response.put("deleted",Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
    

