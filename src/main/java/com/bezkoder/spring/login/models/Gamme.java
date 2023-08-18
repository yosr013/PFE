package com.bezkoder.spring.login.models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class Gamme {

    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Ce champ est obligatoire")
    private String ref;

    @NotNull(message = "Ce champ est obligatoire")
    private float temps;

    @OneToMany(mappedBy = "gamme")
    private List<Phase> phases;

    public Gamme(String value) {
        // Initialize the Machine instance using the provided String value
    }
    
}
