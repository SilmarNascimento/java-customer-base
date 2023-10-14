package com.silmarfnascimento.CEPSystem.service;

import com.silmarfnascimento.CEPSystem.model.Client;

import java.util.List;
import java.util.UUID;

public interface ClientService {
  List<Client> findAll();

  Client findById(UUID id);

  void create(Client client);

  void update(UUID id, Client client);

  void delete(UUID id);
}
