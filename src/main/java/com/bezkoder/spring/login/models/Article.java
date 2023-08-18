package com.bezkoder.spring.login.models;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

//import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
public class Article {

    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
    
    @NotBlank
    
    @NotBlank(message = "Ce champ est obligatoire")
    private String libelle;

    @NotBlank
    private String theme;

    
    @NotNull(message = "Ce champ est obligatoire")
    private float prixFacon;

    
    
    @NotNull(message = "Ce champ est obligatoire")
    private float prixFini;
    

    
    @ManyToMany(fetch = FetchType.EAGER)
@JoinTable(name = "article_tailles",
      joinColumns = { @JoinColumn(name = "article_id") },
      inverseJoinColumns = { @JoinColumn(name = "taille_id") })
      
private Set<Taille> tailles = new HashSet<>();


    @NotBlank
    private String couleur;

    

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "modele_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Modele modele;


    public Article(@NotBlank(message = "Ce champ est obligatoire") String libelle, String theme,
            @NotNull(message = "Ce champ est obligatoire") float prixFacon,
            @NotNull(message = "Ce champ est obligatoire") float prixFini,
            String couleur, Modele modele) {
        this.libelle = libelle;
        this.theme = theme;
        this.prixFacon = prixFacon;
        this.prixFini = prixFini;
        this.couleur = couleur;
        this.modele = modele;
    }

    public void addTaille(Taille taille) {
        this.tailles.add(taille);
        taille.getArticles().add(this);
      }

      public Article(String value) {
        // Initialize the Machine instance using the provided String value
    }

    @Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Article)) return false;
    Article article = (Article) o;
    return id == article.id; // Comparaison basée sur l'identifiant (id) de l'article
}

@Override
public int hashCode() {
    return Objects.hash(id);
}
    

    
    
}
