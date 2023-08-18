package com.bezkoder.spring.login.models;

//import java.util.HashSet;
import java.util.List;
//import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Chaine {

    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    @NotBlank(message ="Ce champ est obligatoire")
    private String code;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER ,mappedBy = "chaine")
    private List<Machine> machines;


    

    
}
