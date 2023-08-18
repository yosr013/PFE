package com.bezkoder.spring.login.models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@JsonDeserialize
public class Machine {

    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    @NotBlank(message="Ce champ est obligatoire")
    private String designation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chaine_id", nullable = false)
    @JsonIgnore
    private Chaine chaine;

    
    public Machine(@NotBlank(message = "Ce champ est obligatoire") String designation, Chaine chaine) {
        this.designation = designation;
        this.chaine = chaine;
    }

    public Machine(String value) {
        // Initialize the Machine instance using the provided String value
    }

    

    
}
