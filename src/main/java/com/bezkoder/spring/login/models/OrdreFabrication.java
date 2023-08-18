package com.bezkoder.spring.login.models;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
//import javax.persistence.MapsId;

import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

//import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class OrdreFabrication {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Ce champ est obligatoire")
    private String ref;

    
    private Date dateFab;

    
    private Date dateFin;

    @NotBlank(message = "Ce champ est obligatoire")
    private String qteParTailles;


    @JsonDeserialize(using =CommandeDeserializer.class)
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "commande_id")
    @JsonIgnoreProperties("ordreFabrication")
    private Commande commande;

    @JsonDeserialize(using =GammeDeserializer.class)
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gamme_id")
    private Gamme gamme;

    

    public OrdreFabrication(String value) {
        // Initialize the Machine instance using the provided String value
    }


    
}
