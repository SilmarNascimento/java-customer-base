package com.silmarfnascimento.CEPSystem.repository;

import com.silmarfnascimento.CEPSystem.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAddressRepository extends JpaRepository<Address, String> {
}
