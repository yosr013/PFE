package com.bezkoder.spring.login.controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

//import javax.validation.Valid;

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
import com.bezkoder.spring.login.models.Commande;
import com.bezkoder.spring.login.models.Gamme;
import com.bezkoder.spring.login.models.OrdreFabrication;
import com.bezkoder.spring.login.repository.CommandeRepository;
import com.bezkoder.spring.login.repository.GammeRepository;
import com.bezkoder.spring.login.repository.OrdreFabRepository;
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
public class OrdreFabController {

    @Autowired
    private CommandeRepository commandeRepo;

    @Autowired
    private GammeRepository gammeRepo;

    @Autowired
    private OrdreFabRepository ordreFabRepo;

    @PreAuthorize("hasRole('ADMIN') or hasRole('CHEF')")
    @GetMapping("/ordres")
    public List<OrdreFabrication> getAllOF(){
        return ordreFabRepo.findAll();
    }

    @PreAuthorize("hasRole('CHEF')")
    @PostMapping("/ordres/{commandeId}/{gammeId}")
public ResponseEntity<?> createOF(
    @PathVariable(value = "commandeId") Long commandeId,
    @PathVariable(value = "gammeId") Long gammeId,
    @RequestBody OrdreFabrication ordreRequest
) {
    // Vérifier si la référence existe déjà
    boolean exists = ordreFabRepo.existsByRef(ordreRequest.getRef());
    if (exists) {
        return ResponseEntity.badRequest().body("La référence existe déjà.");
    }

    Commande commande = commandeRepo.findById(commandeId)
        .orElseThrow(() -> new ResourceNotFoundException("Commande not found with id: " + commandeId));

    Gamme gamme = gammeRepo.findById(gammeId)
        .orElseThrow(() -> new ResourceNotFoundException("Gamme not found with id: " + gammeId));

    ordreRequest.setCommande(commande);
    ordreRequest.setGamme(gamme);

    OrdreFabrication ordre = ordreFabRepo.save(ordreRequest);

    // Mettre à jour l'état de la commande
    commande.setEtat(1); // 1 pour indiquer que la commande est traitée
    commandeRepo.save(commande);

    return new ResponseEntity<>(ordre, HttpStatus.CREATED);
}


    @PreAuthorize("hasRole('CHEF') or hasRole('ADMIN')")
    @GetMapping("/ordres/{id}")
    public ResponseEntity<OrdreFabrication> getOFById(@PathVariable Long id){
        OrdreFabrication ordre = ordreFabRepo.findById(id)
                 .orElseThrow(()-> new ResourceNotFoundException("Ordre de fabrication not found with id : "+id));
        return ResponseEntity.ok(ordre);
    }


    @GetMapping("ordres/pdf/{ordreId}")
public void generatePDF(@PathVariable("ordreId") long ordreId, HttpServletResponse response) {
    // Récupérez la commande à partir de l'ID en utilisant le repository
    Optional<OrdreFabrication> optionalOrdre = ordreFabRepo.findById(ordreId);
    if (optionalOrdre.isPresent()) {
        OrdreFabrication ordre = optionalOrdre.get();

        // Créez le document PDF
        com.itextpdf.text.Document document = new com.itextpdf.text.Document();
        try {
            // Initialisez le writer pour écrire le document vers un flux de sortie en mémoire
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);

            document.addTitle(ordre.getRef());

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
            Chunk titleChunk = new Chunk("Détails de l'ordre de fabrication", titleFont);
            titleFont.setColor(BaseColor.BLUE);
            
            // Ajoutez le titre au paragraphe
            Paragraph titleParagraph = new Paragraph(titleChunk);
            titleParagraph.setAlignment(Element.ALIGN_CENTER);
            
            // Ajoutez le paragraphe au document
            document.add(titleParagraph);
            document.add(Chunk.NEWLINE);

            
            // Ajoutez les titres et les valeurs des champs de la commande
            addField(document, "Référence:", ordre.getRef());
            addField(document, "Date fabrication:", ordre.getDateFab().toString());
            addField(document, "Date de fin prévue:", ordre.getDateFin().toString());
            addField(document, "Quantité:", String.valueOf(ordre.getCommande().getQte()));
            addField(document, "Quantité par taille:", String.valueOf(ordre.getQteParTailles()));
            addField(document, "Article:", ordre.getCommande().getArticle().getLibelle());
            addField(document, "Couleur article:", ordre.getCommande().getArticle().getCouleur());
            addField(document, " Client :", ordre.getCommande().getClient().getRaisonSociale());
            addField(document, "Référence gamme de montage:", ordre.getGamme().getRef());
            addField(document, "Temps gamme de montage:", String.valueOf(ordre.getGamme().getTemps()));

            // Fermez le document
            document.close();

            // Récupérez le contenu du PDF généré en mémoire
            byte[] pdfContent = outputStream.toByteArray();

            // Définissez les en-têtes de la réponse HTTP
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\"commande.pdf\"");
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


   
