package com.bezkoder.spring.login.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "employees")
public class Employe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Size(min=4)
	@NotBlank(message ="Ce champ est obligatoire!")
    @Column(name = "first_name")
    private String firstName;

    @Size(min=4)
	@NotBlank(message ="Ce champ est obligatoire!")
    @Column(name = "last_name")
    private String lastName;
    
    @Email(message="Veuillez entrer un email valide!")
    @NotBlank(message ="Ce champ est obligatoire!")
    private String email;

    @Size(min = 8)
    @Size(max = 8)
    @NotBlank(message ="Ce champ est obligatoire!")
    private String numTel;

    @Size(min = 4)
    @Size(max = 4)
    @NotBlank(message ="Ce champ est obligatoire!")
    private String matricule;

    public Employe(String value) {
        // Initialize the Machine instance using the provided String value
    }



    
}
