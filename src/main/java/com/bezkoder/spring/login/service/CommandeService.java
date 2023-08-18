package com.bezkoder.spring.login.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.bezkoder.spring.login.repository.CommandeRepository;

@Service
public class CommandeService {

    @Autowired
    private CommandeRepository commandeRepository;

    public String getMostOrderedArticleLabel() {
        List<String> labels = commandeRepository.findMostOrderedArticleLabel();

        if (labels.isEmpty()) {
            // Gérer le cas où il n'y a pas de commande
            return "";
        }

        // Récupérer le libellé de l'article le plus commandé (premier élément de la liste)
        return labels.get(0);
    }

    public int getMostOrderedArticleCount() {
    Pageable pageable = PageRequest.of(0, 1); // Limite les résultats à un seul résultat
    List<Long> articleIds = commandeRepository.findMostOrderedArticleIds(pageable);
    
    if (!articleIds.isEmpty()) {
        Long articleId = articleIds.get(0);
        return commandeRepository.findMostOrderedArticleCount(articleId);
    } else {
        return 0;
    }
}

    
}
