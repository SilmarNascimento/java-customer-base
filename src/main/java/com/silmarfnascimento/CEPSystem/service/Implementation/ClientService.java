package com.silmarfnascimento.CEPSystem.service.Implementation;

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
    return new ServiceResponse("SUCCESS", clients);
  }

  @Override
  public ServiceResponse findById(UUID id) {
    Optional<Client> client = clientRepository.findById(id);
    return new ServiceResponse("SUCCESS", client);
  }

  @Override
  public ServiceResponse create(Client client) {
    Client createdClient = saveClientAddress(client);
    return new ServiceResponse("CREATED", createdClient);
  }

  @Override
  public ServiceResponse update(UUID id, Client client) {
    Optional<Client> clientFound = clientRepository.findById(id);
    if (clientFound.isEmpty()) {
      return new ServiceResponse("NOT_FOUND", "Client not Found");
    }
    Utils.copyNonNullProperties(client, clientFound);
    Client updatedClient = saveClientAddress(clientFound.get());
    return new ServiceResponse("SUCCESS", updatedClient);
  }

  @Override
  public void delete(UUID id) {
    clientRepository.deleteById(id);
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
