package com.bezkoder.spring.login.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.bezkoder.spring.login.models.Commande;
import com.bezkoder.spring.login.models.OrdreFabrication;
import com.bezkoder.spring.login.models.SuivieProd;
import com.bezkoder.spring.login.repository.OrdreFabRepository;
import com.bezkoder.spring.login.repository.SuiviProdRepository;

@RestController
@RequestMapping("api/v1")
@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials="true")
public class SuiviProdController {

    @Autowired
	private OrdreFabRepository ordreRepo;

	@Autowired
    private SuiviProdRepository suiviRepo;
    
    
    @PreAuthorize("hasRole('CHEF')")
    @PostMapping("/suivi/{ordreId}")
    public SuivieProd createSuivi(@Valid @PathVariable (value = "ordreId") Long ordreId,
                                 @Valid @RequestBody SuivieProd suivi) {
        return ordreRepo.findById(ordreId).map(ordre -> {
            suivi.setOrdreFab(ordre);
            return suiviRepo.save(suivi);
        }).orElseThrow(() -> new ResourceNotFoundException("ordreId " + ordreId + " not found"));
    }


    @PreAuthorize("hasRole('ADMIN') or hasRole('CHEF')")
	@GetMapping("/suivi")
	public List<SuivieProd> getAllSuivi(){
	        return suiviRepo.findAll();
	    }

        @PreAuthorize("hasRole('CHEF') or hasRole('ADMIN')")
        @GetMapping("suivi/pourcentage-pieces-defaillantes/{ordreId}")
public double calculerPourcentagePiecesDefaillantes(@PathVariable("ordreId") long ordreId) {
    // Récupérer l'ordre de fabrication correspondant à l'ID
    Optional<OrdreFabrication> ordreFabricationOptional = ordreRepo.findById(ordreId);

    if (ordreFabricationOptional.isPresent()) {
        OrdreFabrication ordreFabrication = ordreFabricationOptional.get();
        
        // Récupérer l'objet SuivieProd correspondant à l'ordre de fabrication
        SuivieProd suivieProd = suiviRepo.findByOrdreFab(ordreFabrication);
        
        if (suivieProd != null) {
            long nbrePiDef = suivieProd.getNbrePiDef();
            
            // Récupérer la quantité totale à partir de l'objet Commande associé
            Commande commande = ordreFabrication.getCommande();
            int quantiteTotale = commande.getQte();
            
            // Calculer le pourcentage de pièces défaillantes par rapport à la quantité totale
            double pourcentagePiecesDefaillantes = (double) nbrePiDef / quantiteTotale * 100;

            return pourcentagePiecesDefaillantes;
        } else {
            throw new ResourceNotFoundException("SuivieProd non trouvé pour l'ordre de fabrication avec l'ID : " + ordreId);
        }
    } else {
        throw new ResourceNotFoundException("Ordre de fabrication non trouvé avec l'ID : " + ordreId);
    }
}
@PreAuthorize("hasRole('CHEF') or hasRole('ADMIN')")
@GetMapping("/suivieProd/{id}/periodesFab")
public Map<String, Long> getPeriodesFab(@PathVariable("id") long ordreFabId) {
    Optional<OrdreFabrication> ordreFabOptional = ordreRepo.findById(ordreFabId);
    
    if (ordreFabOptional.isPresent()) {
        OrdreFabrication ordreFab = ordreFabOptional.get();
        
        SuivieProd suivieProd = suiviRepo.findByOrdreFab(ordreFab);
        
        if (suivieProd != null) {
            long periodeFabActuelle = suivieProd.getPeriodeFabActuelle();
            long periodeFabPrevue = suivieProd.getPeriodeFabPrevue();
            
            Map<String, Long> resultat = new HashMap<>();
            resultat.put("periodeFabActuelle", periodeFabActuelle);
            resultat.put("periodeFabPrevue", periodeFabPrevue);
            
            return resultat;
        }
    }
    
    return null; //Si aucun ordre de fabrication correspondant n'est trouvé ou si aucun suivi de production n'est associé à l'ordre de fabrication, la méthode renvoie null.
}

}












