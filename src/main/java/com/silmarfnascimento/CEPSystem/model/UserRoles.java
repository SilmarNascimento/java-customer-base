package com.silmarfnascimento.CEPSystem.model;

public enum UserRoles {
  ADMIN("admin"),
  USER("user");

  private String role;

  UserRoles(String Role) {
    this.role = role;
  }

  public String getRole() {
    return this.role;
  }
}
