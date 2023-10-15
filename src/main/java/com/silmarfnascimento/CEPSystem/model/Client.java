package com.silmarfnascimento.CEPSystem.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class Client {
  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;

  private String name;

  @Column(unique = true)
  private String username;

  private String email;

  private String password;

  @ManyToOne
  private Address address;

  @CreationTimestamp
  private LocalDateTime createdAt;
}
