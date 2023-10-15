package com.silmarfnascimento.CEPSystem.utils;

import org.springframework.http.HttpStatus;

public class mapHTTPStatus {
  public static HttpStatus mapHttpStatus(String status) {
    return switch (status) {
      case "OK" -> HttpStatus.OK;
      case "CREATED" -> HttpStatus.CREATED;
      case "NOT_FOUND" -> HttpStatus.NOT_FOUND;
      case "BAD_REQUEST" -> HttpStatus.BAD_REQUEST;
      default -> HttpStatus.INTERNAL_SERVER_ERROR;
    };
  }
}
