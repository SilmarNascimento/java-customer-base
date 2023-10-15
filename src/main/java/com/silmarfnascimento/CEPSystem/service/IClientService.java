package com.silmarfnascimento.CEPSystem.service;

import com.silmarfnascimento.CEPSystem.model.Client;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IClientService {
  ServiceResponse findAll();

  ServiceResponse findById(UUID id);

  ServiceResponse create(Client client);

  ServiceResponse update(UUID id, Client client);

  void delete(UUID id);
}
