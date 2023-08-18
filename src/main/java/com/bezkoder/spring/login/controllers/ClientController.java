package com.bezkoder.spring.login.controllers;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
import com.bezkoder.spring.login.dto.ClientCommandeCountDTO;
import com.bezkoder.spring.login.models.Client;
import com.bezkoder.spring.login.repository.ClientRepository;
import com.bezkoder.spring.login.service.ClientService;


@RestController
@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials="true")
@RequestMapping("api/v1")
public class ClientController {


    @Autowired
	private ClientRepository clientRepo;

	@Autowired
    private ClientService clientService;


    
	//get all clients 
	    @PreAuthorize("hasRole('ADMIN') or hasRole('CHEF')")
		@GetMapping("/clients")
		public List<Client> getAllClient(){
	        return clientRepo.findAll();
	    }
		//create user restApi
		
		@PreAuthorize("hasRole('CHEF')")
		@PostMapping("/clients") //Les erreurs de validation seront collectées dans l'objet bindingResult
public ResponseEntity<String> createClient(@Valid @RequestBody Client client, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) { //vérifie s'il y a des erreurs de validation dans l'objet bindingResult.
        List<FieldError> fieldErrors = bindingResult.getFieldErrors(); ////récupère la liste des erreurs de validation.
        List<String> errorMessages = new ArrayList<>();

        for (FieldError fieldError : fieldErrors) {
            String fieldName = fieldError.getField(); ////récupère le nom du champ associé à l'erreur de validation.
            String errorMessage = fieldError.getDefaultMessage(); //récupère le message d'erreur par défaut associé à l'erreur de validation.
            
            if (isPatternField(fieldName)) { //vérifier si le champ est un champ avec une contrainte de motif (Pattern)
                errorMessages.add(errorMessage); //Si le champ est un champ avec une contrainte de motif, le message d'erreur correspondant est ajouté à la liste errorMessages
            }
        }

        //vérifie si la liste errorMessages n'est pas vide.
        if (!errorMessages.isEmpty()) {
            String errorResponse = String.join(", ", errorMessages);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(errorResponse);
        }
    }

    if (clientRepo.existsByRaisonSociale(client.getRaisonSociale())) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(" Ce client existe déja ! ");
    }

    Client savedClient = clientRepo.save(client);
    return ResponseEntity.status(HttpStatus.CREATED)
            .body("Client ajouté avec succès");
}



		
		//get client by id restApi
		@PreAuthorize("hasRole('CHEF') or hasRole('ADMIN')")
		@GetMapping("/clients/{id}")
	    public ResponseEntity<Client> getClientById(@PathVariable Long id){
	        Client client = clientRepo.findById(id)
	                 .orElseThrow(()-> new ResourceNotFoundException("Client introuvable avec l'identifiant : "+id));
	        return ResponseEntity.ok(client);
	    }
		
	    //delete client restApi
		@PreAuthorize("hasRole('CHEF')")
	    @DeleteMapping("/clients/{id}")
	    public ResponseEntity<Map<String,Boolean>> deleteClient(@PathVariable Long id){
	        Client client=clientRepo.findById(id)
	        .orElseThrow(()-> new ResourceNotFoundException("Client introuvable avec l'identifiant:"+id));
	        clientRepo.delete(client);
	        Map<String,Boolean> response = new HashMap<>();
	        response.put("supprimé avec succès",Boolean.TRUE);
	        return ResponseEntity.ok(response);
	    }


        //update client restApi
		@PreAuthorize("hasRole('ADMIN') or hasRole('CHEF')")
		@PutMapping("/clients/{id}") // Les erreurs de validation seront collectées dans l'objet bindingResult
    public ResponseEntity<String> updateClient(@PathVariable Long id, @Valid @RequestBody Client clientDetails, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors(); //récupère la liste des erreurs de validation.

            for (FieldError fieldError : fieldErrors) {
                String fieldName = fieldError.getField(); //récupère le nom du champ associé à l'erreur de validation.
                String errorMessage = fieldError.getDefaultMessage(); //récupère le message d'erreur par défaut associé à l'erreur de validation.

                if (isPatternField(fieldName)) { //vérifier si le champ est un champ avec une contrainte de motif (Pattern)
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST) // si le champs est pattern renvoie une réponse HTTP avec le statut BAD_REQUEST (400) et le message d'erreur correspondant.
                            .body(errorMessage);
                }
            }
        }

        Client client = clientRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));

        if (!client.getRaisonSociale().equals(clientDetails.getRaisonSociale()) &&
                clientRepo.existsByRaisonSociale(clientDetails.getRaisonSociale())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(" Ce client existe déja ! ");
        }

        client.setRaisonSociale(clientDetails.getRaisonSociale());
        client.setCode(clientDetails.getCode());
        client.setActivite(clientDetails.getActivite());
        client.setAdresse(clientDetails.getAdresse());
        client.setVille(clientDetails.getVille());
        client.setRegion(clientDetails.getRegion());
        client.setCodePostal(clientDetails.getCodePostal());
        client.setFax(clientDetails.getFax());
        client.setTelFix(clientDetails.getTelFix());
        client.setNumTel(clientDetails.getNumTel());
        client.setEmail(clientDetails.getEmail());
        client.setPays(clientDetails.getPays());

        Client updatedClient = clientRepo.save(client);
        return ResponseEntity.ok("Client modifié avec succès");
    }


		@GetMapping("/clients/commandes/count")
    public ResponseEntity<List<ClientCommandeCountDTO>> getClientCommandeCount() {
        List<ClientCommandeCountDTO> clientCommandeCountList = clientService.getClientCommandeCount();
        return ResponseEntity.ok(clientCommandeCountList);
    }

    //une méthode privée qui vérifie si un champ spécifié est un champ avec une contrainte de motif (Pattern)
	private boolean isPatternField(String fieldName) { //fieldName, qui représente le nom du champ à vérifier.
		// Ajoutez les noms des attributs avec l'annotation @Pattern que vous souhaitez valider
		List<String> patternFields = Arrays.asList("numTel", "raisonSociale"); //Une liste de noms de champs avec une annotation @Pattern est définie dans la variable patternFields
		
		return patternFields.contains(fieldName); //la méthode contains de la classe List pour vérifier si la patternFields contient le fieldName spécifié.
	}


	

	

		
	  
    
}
