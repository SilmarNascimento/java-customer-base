package com.silmarfnascimento.CEPSystem.controller;

import com.silmarfnascimento.CEPSystem.dto.Login;
import com.silmarfnascimento.CEPSystem.service.Implementation.LoginService;
import com.silmarfnascimento.CEPSystem.service.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.silmarfnascimento.CEPSystem.utils.mapHTTPStatus.mapHttpStatus;

@RestController
@RequestMapping("auth")
public class LoginController {
  @Autowired
  private LoginService loginService;

  @PostMapping
  public ResponseEntity<Object> login(@RequestBody Login login){
    ServiceResponse serviceResponse = loginService.login(login);
    if(serviceResponse.getData() != null) {
      return ResponseEntity.status(mapHttpStatus(serviceResponse.getStatus())).body(serviceResponse.getData());
    }
    return ResponseEntity.status(mapHttpStatus(serviceResponse.getStatus())).body("Cliente não encontrado");
  }
}
