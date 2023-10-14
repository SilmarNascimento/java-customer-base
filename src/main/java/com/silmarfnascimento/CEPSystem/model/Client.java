package com.silmarfnascimento.CEPSystem.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class Client {
  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;
  private String nome;
  @ManyToOne
  private Address endereco;
}
