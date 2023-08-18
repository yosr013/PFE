package com.bezkoder.spring.login.models;

import java.util.List;


import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Client {
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	

	@Pattern(regexp = "^[a-zA-Z]+$", message = "La raison sociale doit contenir uniquement des lettres")
    @NotBlank(message = "Ce champ est obligatoire")
    @Column(name="raisonSociale")
	private String raisonSociale;


    @NotBlank(message = "Ce champ est obligatoire")
	@Column(name="code")
	private String code ;

	
    @NotBlank(message = "Ce champ est obligatoire")
	@Column(name="activite")
	private String activite;

	
    @NotBlank(message = "Ce champ est obligatoire")
	@Column(name="adresse")
	private String adresse;

    @NotBlank(message = "Ce champ est obligatoire")
	@Column(name="ville")
	private String ville;

	
	@Column(name="region")
	private String region;

    @NotBlank(message = "Ce champ est obligatoire")
	@Column(name="codePostal")
	private String codePostal;

	@Column(name="fax")
	private String fax ;

	@Column(name="telfix")
	private String telFix ;

	@Pattern(regexp = "^[0-9]+$", message = "Le numéro de téléphone doit contenir uniquement des chiffres")
    @Size(min = 8, message = "Le numéro de téléphone doit avoir au moins 8 chiffres")
    @Size(max = 8, message = "Le numéro de téléphone ne doit pas dépasser 8 chiffres")
    @NotBlank(message = "Ce champ est obligatoire")
	@Column(name="numTel")
	private String numTel ;

    @Email(message = "Donner un email valid")
    @NotBlank(message = "Ce champ est obligatoire")
	@Column(name="email")
	private String  email;

	
    @NotBlank(message = "Ce champ est obligatoire")
    @Column(name="pays")
    private String pays;

	public Client(String value) {
        // Initialize the Machine instance using the provided String value
    }

	@JsonIgnore
	@OneToMany(mappedBy = "client")
private List<Commande> commandes;
	public List<Commande> getCommandes() {
		return commandes;
	}
    
}
