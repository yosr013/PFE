package com.bezkoder.spring.login.models;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Rendement {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    @NotNull(message="Ce champ est obligatoire")
    private Date date;

    @NotNull(message="Ce champ est obligatoire")
    private int nbrePiProduites;

    @NotNull(message="Ce champ est obligatoire")
    private float tempsOpe;

    @NotNull(message="Ce champ est obligatoire")
    private float tempsOpeEs;

    @NotNull(message="Ce champ est obligatoire")
    private long tempsPre;

    @NotNull(message="Ce champ est obligatoire")
    private long rendementEs;

    @NotNull(message="Ce champ est obligatoire")
    private long rendement;

    @NotNull(message = "date obligatoire")
    private Date dateDebut ;
    @NotNull(message = "date obligatoire")
    private Date dateFin ;



    @JsonDeserialize(using =EmployeDeserializer.class)
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employe_id")
    private Employe employe;


    
}
