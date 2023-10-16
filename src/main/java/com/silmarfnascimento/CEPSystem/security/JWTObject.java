package com.silmarfnascimento.CEPSystem.security;

import lombok.Data;

import java.util.Date;

@Data
public class JWTObject {
  private String username;
  private String password;
  private Date createdAt;
  private Date expiresAt;
}
