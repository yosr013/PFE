package com.bezkoder.spring.login.models;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@JsonDeserialize
@Entity
@NoArgsConstructor

@Data
public class Operation {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Ce champ est obligatoire")
    private String nom;

    @NotNull(message = "Ce champ est obligatoire")
    private float temps;

    @NotNull(message = "Ce champ est obligatoire")
    private long code;

    @JsonDeserialize(using = MachineDeserializer.class)
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "machine_id", nullable = false)
    private Machine machine;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "phase_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Phase phase;


   
    public Operation(@NotBlank(message = "Ce champ est obligatoire") String nom,
            @NotNull(message = "Ce champ est obligatoire") float temps,
            @NotNull(message = "Ce champ est obligatoire") long code, Machine machine, Phase phase) {
        this.nom = nom;
        this.temps = temps;
        this.code = code;
        this.machine = machine;
        this.phase = phase;
    }

    
}
