package com.bezkoder.spring.login.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.bezkoder.spring.login.models.Article;
import com.bezkoder.spring.login.models.Client;

import javax.servlet.http.HttpServletResponse;


import java.io.ByteArrayOutputStream;
import java.io.IOException;




//import javax.validation.Valid;

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
import com.bezkoder.spring.login.dto.CommandeDTO;
import com.bezkoder.spring.login.models.Article;
import com.bezkoder.spring.login.models.Commande;
import com.bezkoder.spring.login.repository.ArticleRepository;
import com.bezkoder.spring.login.repository.ClientRepository;
import com.bezkoder.spring.login.repository.CommandeRepository;
import com.bezkoder.spring.login.service.CommandeService;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;



@RestController
@RequestMapping("api/v1")
@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials="true")
public class CommandeController {

    @Autowired
    private CommandeRepository commandeRepo;
  
    @Autowired
    private ArticleRepository articleRepo;

    @Autowired
    private ClientRepository clientRepo;

    @Autowired
    private CommandeService commandeService;
    
    //create commande restApi
    @PreAuthorize("hasRole('CHEF')")
    @PostMapping("/commandes/{articleId}/{clientId}")
public ResponseEntity<String> createCommande(@PathVariable(value = "articleId") Long articleId, @PathVariable(value = "clientId") Long clientId, @RequestBody Commande commande) {
    Optional<Article> articleOptional = articleRepo.findById(articleId);
    Optional<Client> clientOptional = clientRepo.findById(clientId);

    if (articleOptional.isPresent() && clientOptional.isPresent()) {
        Article article = articleOptional.get();
        Client client = clientOptional.get();

        // Vérifier si la référence existe déjà
        boolean existingCommande = commandeRepo.existsByRef(commande.getRef());
        if (existingCommande) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("La référence existe déjà.");
        }

        // Vérifier si la date de livraison est supérieure à la date d'entrée
        if (commande.getDateLivraison().before(commande.getDateEntree())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("La date de livraison doit être supérieure à la date d'entrée.");
        }

        // Vérifier si la quantité est positive
        if (commande.getQte() < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("La quantité ne peut pas être négative.");
        }

        Commande c = new Commande(commande.getDateEntree(), commande.getDateLivraison(), commande.getQte(), commande.getSaison(), article, commande.getRef(), client);
        commandeRepo.save(c);

        return ResponseEntity.status(HttpStatus.CREATED)
        .body("Commande ajouté avec succès");
    }

    return ResponseEntity.notFound().build();
}



         //get all commandes
         @PreAuthorize("hasRole('ADMIN') or hasRole('CHEF')")
         @GetMapping("/commandes")
public ResponseEntity<List<Commande>> getAllCommandes() {
    List<Commande> commandes = commandeRepo.findByEtatNot(1);
    return new ResponseEntity<>(commandes, HttpStatus.OK);
}

@GetMapping("/commandes/nombre")
public ResponseEntity<Integer> getNombreCommandesEtatZero() {
    int nombreCommandes = commandeRepo.countByEtat(0);
    return new ResponseEntity<>(nombreCommandes, HttpStatus.OK);
}

@GetMapping("/commandes/etat")
public ResponseEntity<Integer> getNombreCommandesEtatUn() {
    int nombreCommandes = commandeRepo.countByEtat(1);
    return new ResponseEntity<>(nombreCommandes, HttpStatus.OK);
}

@GetMapping("/commandes/etat-un/percentage")
public ResponseEntity<Double> getPourcentageCommandesEtatUn() {
    int nombreCommandesEtatUn = commandeRepo.countByEtat(1);
    int nombreTotalCommandes = (int) commandeRepo.count();
    
    double pourcentage = 0.0;
    if (nombreTotalCommandes > 0) {
        pourcentage = (nombreCommandesEtatUn / (double) nombreTotalCommandes) * 100;
        pourcentage = Math.round(pourcentage * 100.0) / 100.0; // Arrondissement à 2 décimales
    }
    
    return new ResponseEntity<>(pourcentage, HttpStatus.OK);
}

@GetMapping("/commandes/etat-zero/percentage")
public ResponseEntity<Double> getPourcentageCommandesEtatZero() {
    int nombreCommandesEtatUn = commandeRepo.countByEtat(0);
    int nombreTotalCommandes = (int) commandeRepo.count();
    
    double pourcentage = 0.0;
    if (nombreTotalCommandes > 0) {
        pourcentage = (nombreCommandesEtatUn / (double) nombreTotalCommandes) * 100;
        pourcentage = Math.round(pourcentage * 100.0) / 100.0; // Arrondissement à 2 décimales
    }
    
    return new ResponseEntity<>(pourcentage, HttpStatus.OK);
}
		

        @PreAuthorize("hasRole('CHEF')")
        @GetMapping("/commandes/{id}")
   public ResponseEntity<Commande> getCommandeById(@PathVariable Long id){
       Commande commande = commandeRepo.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Commande introuvable avec l'identifiant : "+id));
       return ResponseEntity.ok(commande);
   }


        @PreAuthorize("hasRole('CHEF')")
        @PutMapping("/commandes/{id}")
public ResponseEntity<String> updateCommande(@PathVariable Long id, @RequestBody Commande commande) {
    Optional<Commande> commandeOptional = commandeRepo.findById(id);

    if (commandeOptional.isPresent()) {
        Commande existingCommande = commandeOptional.get();

        // Vérifier si la référence existe déjà pour une autre commande
        boolean existingRefCommande = commandeRepo.existsByRef(commande.getRef());
        if (existingRefCommande && !existingCommande.getRef().equals(commande.getRef())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("La référence existe déjà pour une autre commande.");
        }

        // Vérifier si la date de livraison est supérieure à la date d'entrée
        if (commande.getDateLivraison().before(commande.getDateEntree())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("La date de livraison doit être supérieure à la date d'entrée.");
        }

        // Vérifier si la quantité est positive
        if (commande.getQte() < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("La quantité ne peut pas être négative.");
        }

        existingCommande.setQte(commande.getQte());
        existingCommande.setDateEntree(commande.getDateEntree());
        existingCommande.setDateLivraison(commande.getDateLivraison());
        existingCommande.setSaison(commande.getSaison());
        existingCommande.setRef(commande.getRef());

        commandeRepo.save(existingCommande);

        return ResponseEntity.ok("Commande modifiée avec succès");
    }

    return ResponseEntity.notFound().build();
}

		




        //delete modele restApi
    @PreAuthorize("hasRole('CHEF')")
   @DeleteMapping("/commandes/{id}")
   public ResponseEntity<Map<String,Boolean>>deleteCommande(@PathVariable Long id){
       Commande commande=commandeRepo.findById(id)
       .orElseThrow(()-> new ResourceNotFoundException("Modele introuvable avec l'identifiant:"+id));
       commandeRepo.delete(commande);
       Map<String,Boolean> response = new HashMap<>();
       response.put("supprimé avec succès",Boolean.TRUE);
       return ResponseEntity.ok(response);
   }


   @GetMapping("/articles/plus-commande")
   public ResponseEntity<String> getMostOrderedArticleLabel() {
       String mostOrderedArticleLabel = commandeService.getMostOrderedArticleLabel();
       return ResponseEntity.ok(mostOrderedArticleLabel);
   }

   
   @GetMapping("/articles/plus-commande/count")
    public ResponseEntity<Integer> getMostOrderedArticleCount() {
        int count = commandeService.getMostOrderedArticleCount();
        return ResponseEntity.ok(count);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('CHEF')")
    @GetMapping("commandes/tri-par-date-livraison")
    public ResponseEntity<List<CommandeDTO>> getCommandesTrieesParDateLivraison() {
        List<Commande> commandes = commandeRepo.findAll();

        // Triez les commandes par ordre croissant de la date de livraison
        Collections.sort(commandes, Comparator.comparing(Commande::getDateLivraison));

        // Obtenez la date actuelle
        Date currentDate = new Date();

        // Créez une liste de DTO pour stocker les informations des commandes triées
        List<CommandeDTO> commandeDTOs = new ArrayList<>();

        // Parcourez les commandes et créez les DTO avec la date de livraison et le nombre de jours restant
        for (Commande commande : commandes) {
            long diffInMilliseconds = commande.getDateLivraison().getTime() - currentDate.getTime();
            long diffInDays = diffInMilliseconds / (24 * 60 * 60 * 1000);

            CommandeDTO commandeDTO = new CommandeDTO();
            commandeDTO.setReference(commande.getRef());
            commandeDTO.setDateLivraison(commande.getDateLivraison());
            commandeDTO.setJoursRestants(diffInDays);

            commandeDTOs.add(commandeDTO);
        }

        return ResponseEntity.ok(commandeDTOs);
    }

    
    // ...

@GetMapping("commandes/pdf/{commandeId}")
public void generatePDF(@PathVariable("commandeId") long commandeId, HttpServletResponse response) {
    // Récupérez la commande à partir de l'ID en utilisant le repository
    Optional<Commande> optionalCommande = commandeRepo.findById(commandeId);
    if (optionalCommande.isPresent()) {
        Commande commande = optionalCommande.get();

        // Créez le document PDF
        com.itextpdf.text.Document document = new com.itextpdf.text.Document();
        try {
            // Initialisez le writer pour écrire le document vers un flux de sortie en mémoire
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);

            document.addTitle(commande.getRef());

            // Ouvrez le document
            document.open();

            String imagePath = "C:\\Users\\MSI\\Desktop\\apple-touch-icon.png";
            Image image = Image.getInstance(imagePath);

// Réglages de la taille et de la position de l'image
            image.scaleToFit(40, 40);
             // Ajustez la taille de l'image selon vos besoins

// Réglages de l'alignement horizontal de l'image
            image.setAlignment(Image.MIDDLE);

// Définir les marges pour positionner l'image en haut du document
            document.setMargins(0, 0, 0, image.getScaledHeight());

// Ajout de l'image au document
            document.add(image);

            // Ajoutez le titre général
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            Chunk titleChunk = new Chunk("Détails de la commande", titleFont);
            titleFont.setColor(BaseColor.BLUE);
            
            // Ajoutez le titre au paragraphe
            Paragraph titleParagraph = new Paragraph(titleChunk);
            titleParagraph.setAlignment(Element.ALIGN_CENTER);
            
            // Ajoutez le paragraphe au document
            document.add(titleParagraph);
            document.add(Chunk.NEWLINE);

            
            // Ajoutez les titres et les valeurs des champs de la commande
            addField(document, "Référence:", commande.getRef());
            addField(document, "Date d'entrée:", commande.getDateEntree().toString());
            addField(document, "Date de livraison:", commande.getDateLivraison().toString());
            addField(document, "Quantité:", String.valueOf(commande.getQte()));
            addField(document, "Saison:", commande.getSaison());
            addField(document, "Article:", commande.getArticle().getLibelle());
            addField(document, "Client:", commande.getClient().getRaisonSociale());

            // Fermez le document
            document.close();

            // Récupérez le contenu du PDF généré en mémoire
            byte[] pdfContent = outputStream.toByteArray();

            // Définissez les en-têtes de la réponse HTTP
            response.setContentType("application/pdf"); //le type de contenu de la réponse HTTP 
            response.setHeader("Content-Disposition", "attachment; filename=\"" + commande.getRef() + ".pdf\""); //Cela spécifie le comportement du navigateur lors de la réception de la réponse.télécharger le fichier PDF 
            response.setHeader("X-Filename", commande.getRef()); // Ajoutez cet en-tête personnalisé pour le nom de fichier généré            
            response.setContentLength(pdfContent.length);

            // Écrivez le contenu du PDF généré dans la réponse HTTP
            response.getOutputStream().write(pdfContent);
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (DocumentException | IOException e) {
            // Gérez les exceptions ici
        }
    } else {
        // Gérez le cas où la commande n'est pas trouvée
    }
}

/**
 * Ajoute un champ (titre et valeur) au document PDF.
 * @param document le document PDF
 * @param fieldTitle le titre du champ
 * @param fieldValue la valeur du champ
 * @throws DocumentException en cas d'erreur lors de l'ajout du champ au document
 */
private void addField(com.itextpdf.text.Document document, String fieldTitle, String fieldValue) throws DocumentException {
    Paragraph fieldParagraph = new Paragraph();
    fieldParagraph.add(new Phrase(fieldTitle, FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD)));
    fieldParagraph.add(new Phrase(" " + fieldValue, FontFactory.getFont(FontFactory.HELVETICA, 12)));
    document.add(fieldParagraph);
}


}