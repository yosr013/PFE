package com.bezkoder.spring.login.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.spring.login.Exception.ResourceNotFoundException;
import com.bezkoder.spring.login.models.User;
import com.bezkoder.spring.login.repository.UserRepository;

@RestController
@RequestMapping("api/v1")
@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials="true")
public class UserController {

    @Autowired
    private UserRepository userRepo;


    @PreAuthorize("hasRole('ADMIN')")
		@GetMapping("/users")
		public List<User> getAllUsers(){
	         return userRepo.getUsersExceptAdmin();
	    }

 
        @PreAuthorize("hasRole('ADMIN')")
        @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id){
        User user = userRepo.findById(id)
                 .orElseThrow(()-> new ResourceNotFoundException("User not found with id : "+id));
        return ResponseEntity.ok(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
		@PutMapping("/users/{id}")
public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
    User user = userRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User introuvable avec l'identifiant : " + id));

    // Vérifier si le nom d'utilisateur existe déjà
    if (userRepo.existsByUsername(userDetails.getUsername())) {
        return ResponseEntity.badRequest().body("Le nom d'utilisateur existe déjà.");
    }

    // Vérifier si l'adresse e-mail existe déjà
    if (userRepo.existsByEmail(userDetails.getEmail())) {
        return ResponseEntity.badRequest().body("L'adresse e-mail existe déjà.");
    }

    user.setUsername(userDetails.getUsername());
    user.setEmail(userDetails.getEmail());
    user.setNumTel(userDetails.getNumTel());
    user.setAdresse(userDetails.getAdresse());
    user.setName(userDetails.getName());

    User updatedUser = userRepo.save(user);
    return ResponseEntity.ok("Utilisateur mis à jour avec succès.");
}


        @PreAuthorize("hasRole('ADMIN')")
        @DeleteMapping("/users/{id}")
   public ResponseEntity<Map<String,Boolean>>deleteUser(@PathVariable Long id){
       User user=userRepo.findById(id)
       .orElseThrow(()-> new ResourceNotFoundException("Utilisateur introuvable avec l'identifiant:"+id));
       userRepo.delete(user);
       Map<String,Boolean> response = new HashMap<>();
       response.put("Utiisateur supprimé avec succès",Boolean.TRUE);
       return ResponseEntity.ok(response);
   }
    
}
