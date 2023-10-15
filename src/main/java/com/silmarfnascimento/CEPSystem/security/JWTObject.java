package com.silmarfnascimento.CEPSystem.security;

import lombok.Data;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Data
public class JWTObject {
  private String username;
  private String password;
  private Date createdAt;
  private Date expiresAt;
  private List<String> roles;

  public void setRoles(List<String> roles) {
    this.roles = roles;
  }

  public void setRoles(String... roles){
    this.roles = Arrays.asList(roles);
  }
}
