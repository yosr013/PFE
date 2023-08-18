package com.bezkoder.spring.login.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bezkoder.spring.login.dto.ClientCommandeCountDTO;
import com.bezkoder.spring.login.models.Client;
import com.bezkoder.spring.login.repository.ClientRepository;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public List<ClientCommandeCountDTO> getClientCommandeCount() {
        List<Client> clients = clientRepository.findAll();

        List<ClientCommandeCountDTO> clientCommandeCountList = new ArrayList<>();
        for (Client client : clients) {
            ClientCommandeCountDTO dto = new ClientCommandeCountDTO();
            dto.setRaisonSociale(client.getRaisonSociale());
            dto.setCommandeCount(client.getCommandes().size());
            clientCommandeCountList.add(dto);
        }

        return clientCommandeCountList;
    }
    
}
