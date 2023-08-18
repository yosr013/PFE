package com.bezkoder.spring.login.models;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SuivieProd {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "ce champs est obligatoire")
    private long nbrePiDef;

    @JsonDeserialize(using = OrdreDeserializer.class)
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ordre_id")
    private OrdreFabrication ordreFab;

    private Date dateFinActuelle;

    private long periodeFabPrevue;

    private long periodeFabActuelle;
}
