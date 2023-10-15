package com.silmarfnascimento.CEPSystem.security;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class JWTObject {
  private String subject; //nome do usuario
  private Date createdAt; //data de criação do token
  private Date expiresAt; // data de expiração do token
  private List<String> roles; //perfis de acesso
}
