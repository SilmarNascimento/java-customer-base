package com.silmarfnascimento.CEPSystem.service.Implementation;

import com.silmarfnascimento.CEPSystem.model.Address;
import com.silmarfnascimento.CEPSystem.model.Client;
import com.silmarfnascimento.CEPSystem.repository.IAddressRepository;
import com.silmarfnascimento.CEPSystem.repository.IClientRepository;
import com.silmarfnascimento.CEPSystem.service.ICEPService;
import com.silmarfnascimento.CEPSystem.service.IClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
public class ClienteService implements IClientService {
  @Autowired
  private IClientRepository clientRepository;
  @Autowired
  private IAddressRepository addressRepository;
  @Autowired
  private ICEPService cepService;

  @Override
  public List<Client> findAll() {
    return clientRepository.findAll();
  }

  @Override
  public Optional<Client> findById(UUID id) {
    return clientRepository.findById(id);
  }

  @Override
  public void create(Client client) {
    saveClientAddress(client);
  }

  @Override
  public void update(UUID id, Client client) {
    // Buscar Cliente por ID, caso exista:
    Optional<Client> clientFound = clientRepository.findById(id);
    if (clientFound.isPresent()) {
      saveClientAddress(client);
    }
  }

  @Override
  public void delete(UUID id) {
    clientRepository.deleteById(id);
  }

  private void saveClientAddress(Client client) {
    String cep = client.getAddress().getCep();
    Address address = addressRepository.findById(cep).orElseGet(() -> {
      Address newAddress = cepService.verifyCEP(cep);
      addressRepository.save(newAddress);
      return newAddress;
    });
    client.setAddress(address);
    clientRepository.save(client);
  }
}
