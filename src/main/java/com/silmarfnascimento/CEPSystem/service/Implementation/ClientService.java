package com.silmarfnascimento.CEPSystem.service.Implementation;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.silmarfnascimento.CEPSystem.model.Address;
import com.silmarfnascimento.CEPSystem.model.Client;
import com.silmarfnascimento.CEPSystem.repository.IAddressRepository;
import com.silmarfnascimento.CEPSystem.repository.IClientRepository;
import com.silmarfnascimento.CEPSystem.service.ICEPService;
import com.silmarfnascimento.CEPSystem.service.IClientService;
import com.silmarfnascimento.CEPSystem.service.ServiceResponse;
import com.silmarfnascimento.CEPSystem.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
public class ClientService implements IClientService {
  @Autowired
  private IClientRepository clientRepository;
  @Autowired
  private IAddressRepository addressRepository;
  @Autowired
  private ICEPService cepService;

  @Override
  public ServiceResponse findAll() {
    List<Client> clients = clientRepository.findAll();
    return new ServiceResponse("OK", clients);
  }

  @Override
  public ServiceResponse findById(UUID id) {
    Optional<Client> client = clientRepository.findById(id);
    return new ServiceResponse("OK", client);
  }

  @Override
  public ServiceResponse create(Client client) {
    Client userFound = this.clientRepository.findByUsername(client.getUsername());
    if (userFound != null) {
      return new ServiceResponse("BAD_REQUEST", "Usuário já existente!");
    }
    hashClientPassword(client);
    Client createdClient = saveClientAddress(client);
    return new ServiceResponse("CREATED", createdClient);
  }

  @Override
  public ServiceResponse update(UUID id, Client client) {
    Optional<Client> clientFound = clientRepository.findById(id);
    if (clientFound.isEmpty()) {
      return new ServiceResponse("NOT_FOUND", "Client not Found");
    }

    var passwordVerify = BCrypt.verifyer().verify(client.getPassword().toCharArray(), clientFound.get().getPassword());
    if(!passwordVerify.verified) {
      hashClientPassword(client);
    }

    Utils.copyNonNullProperties(client, clientFound.get());
    Client updatedClient = saveClientAddress(clientFound.get());
    return new ServiceResponse("OK", updatedClient);
  }

  @Override
  public void delete(UUID id) {
    clientRepository.deleteById(id);
  }

  private static void hashClientPassword(Client client) {
    String passwordHashed = BCrypt.withDefaults()
        .hashToString(12, client.getPassword().toCharArray());
    client.setPassword(passwordHashed);
  }

  private Client saveClientAddress(Client client) {
    String cep = client.getAddress().getCep();
    Address address = addressRepository.findById(cep).orElseGet(() -> {
      Address newAddress = cepService.verifyCEP(cep);
      addressRepository.save(newAddress);
      return newAddress;
    });
    client.setAddress(address);
    clientRepository.save(client);
    return client;
  }
}
