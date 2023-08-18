package com.bezkoder.spring.login.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientCommandeCountDTO {

    private String raisonSociale;
    private int commandeCount;
}
