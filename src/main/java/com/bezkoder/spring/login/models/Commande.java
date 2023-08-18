package com.bezkoder.spring.login.models;

import java.sql.Date;
//import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
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

//import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
//import com.fasterxml.jackson.databind.annotation.JsonSerialize;

//import com.fasterxml.jackson.annotation.JsonIgnore;

//import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor

public class Commande {

    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

    @NotBlank(message="Ce champ est obligatoire")
    private String ref;

    @NotNull(message = "date obligatoire")
    private Date dateEntree;

    @NotNull(message = "date obligatoire")
    private Date dateLivraison;

    @NotNull
    private int qte;


    private String saison;

    @JsonDeserialize(using =ArticleDeserializer.class)
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;


    @JsonDeserialize(using = ClientDeserializer.class)
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Client client;

    @Column(name = "etat", columnDefinition = "integer default 0")
    private int etat;

    public Commande(@NotNull(message = "date obligatoire") Date dateEntree,
            @NotNull(message = "date obligatoire") Date dateLivraison, int qte, String saison, Article article,String ref,
            Client client) {
        this.ref=ref;
        this.dateEntree = dateEntree;
        this.dateLivraison = dateLivraison;
        this.qte = qte;
        this.saison = saison;
        this.article = article;
        this.client = client;
    }

    public Commande(String value) {
        // Initialize the Machine instance using the provided String value
    }

 



    
    
}
