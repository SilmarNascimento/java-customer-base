package com.silmarfnascimento.CEPSystem.service;

import com.silmarfnascimento.CEPSystem.model.Client;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IClientService {
  List<Client> findAll();

  Optional<Client> findById(UUID id);

  void create(Client client);

  void update(UUID id, Client client);

  void delete(UUID id);
}
