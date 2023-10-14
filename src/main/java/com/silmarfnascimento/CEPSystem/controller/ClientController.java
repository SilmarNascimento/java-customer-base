package com.silmarfnascimento.CEPSystem.controller;

import com.silmarfnascimento.CEPSystem.model.Client;
import com.silmarfnascimento.CEPSystem.service.Implementation.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class ClientController {

  @Autowired
  private ClientService clientService;

  @GetMapping
  public ResponseEntity<List<Client>> findAll() {
    return ResponseEntity.status(HttpStatus.OK).body(clientService.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Object> findById(@PathVariable UUID id) {
    Optional<Client> clientFound = clientService.findById(id);
    if(clientFound.isPresent()) {
      return ResponseEntity.status(HttpStatus.OK).body(clientService.findById(id));
    }
    return ResponseEntity.status(HttpStatus.OK).body("Cliente não encontrado");
  }

  @PostMapping
  public ResponseEntity<Client> create(@RequestBody Client client) {
    clientService.create(client);
    return ResponseEntity.status(HttpStatus.CREATED).body(client);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Client> atualizar(@PathVariable UUID id, @RequestBody Client cliente) {
    clientService.update(id, cliente);
    return ResponseEntity.status(HttpStatus.OK).body(cliente);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    clientService.delete(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
