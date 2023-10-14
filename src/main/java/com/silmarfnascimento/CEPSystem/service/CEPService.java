package com.silmarfnascimento.CEPSystem.service;

import com.silmarfnascimento.CEPSystem.model.Address;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "viacep", url = "https://viacep.com.br/ws")
public interface CEPService {

  @GetMapping("/{cep}/json/")
  Address consultarCep(@PathVariable("cep") String cep);
}
