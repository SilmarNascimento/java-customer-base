package com.silmarfnascimento.CEPSystem.repository;

import com.silmarfnascimento.CEPSystem.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IClientRepository extends JpaRepository<Client, UUID> {
  Client findByUsername(String username);
}
