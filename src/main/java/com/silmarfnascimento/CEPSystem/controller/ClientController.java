package com.silmarfnascimento.CEPSystem.controller;

import com.silmarfnascimento.CEPSystem.model.Client;
import com.silmarfnascimento.CEPSystem.service.Implementation.ClientService;
import com.silmarfnascimento.CEPSystem.service.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

import static com.silmarfnascimento.CEPSystem.utils.mapHTTPStatus.mapHttpStatus;

@RestController
@RequestMapping("/users")
public class ClientController {

  @Autowired
  private ClientService clientService;

  @GetMapping
  public ResponseEntity<Object> findAll() {
    ServiceResponse serviceResponse = clientService.findAll();
    return ResponseEntity.status(mapHttpStatus(serviceResponse.getStatus())).body(serviceResponse.getData());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Object> findById(@PathVariable UUID id) {
    ServiceResponse serviceResponse = clientService.findById(id);
    if(serviceResponse.getData() != null) {
      return ResponseEntity.status(mapHttpStatus(serviceResponse.getStatus())).body(serviceResponse.getData());
    }
    return ResponseEntity.status(mapHttpStatus(serviceResponse.getStatus())).body("Cliente não encontrado");
  }

  @PostMapping
  public ResponseEntity<Object> create(@RequestBody Client client) {
    ServiceResponse serviceResponse = clientService.create(client);
    return ResponseEntity.status(mapHttpStatus(serviceResponse.getStatus())).body(serviceResponse.getData());
  }

  @PutMapping("/{id}")
  public ResponseEntity<Object> atualizar(@PathVariable UUID id, @RequestBody Client cliente) {
    ServiceResponse serviceResponse = clientService.update(id, cliente);
    return ResponseEntity.status(mapHttpStatus(serviceResponse.getStatus())).body(serviceResponse.getData());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    clientService.delete(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
