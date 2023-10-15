package com.silmarfnascimento.CEPSystem.service.Implementation;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.silmarfnascimento.CEPSystem.dto.Login;
import com.silmarfnascimento.CEPSystem.dto.Session;
import com.silmarfnascimento.CEPSystem.model.Client;
import com.silmarfnascimento.CEPSystem.repository.IClientRepository;
import com.silmarfnascimento.CEPSystem.security.JWTCreator;
import com.silmarfnascimento.CEPSystem.security.JWTObject;
import com.silmarfnascimento.CEPSystem.security.SecurityConfig;
import com.silmarfnascimento.CEPSystem.service.ServiceResponse;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Data
@Service
public class LoginService {
  @Autowired
  private SecurityConfig securityConfig;
  @Autowired
  private IClientRepository clientRepository;

  public ServiceResponse login(Login login){
    Client clientFound = clientRepository.findByUsername(login.getUsername());
    if(clientFound != null) {
      var passwordVerify = BCrypt.verifyer().verify(clientFound.getPassword().toCharArray(), login.getPassword());
      if (!passwordVerify.verified) {
        return new ServiceResponse("OK", "Senha ou login inválidos");
      }

      Session session = new Session();
      session.setLogin(clientFound.getUsername());

      JWTObject jwtObject = new JWTObject();
      jwtObject.setUsername(clientFound.getUsername());
      jwtObject.setPassword(clientFound.getPassword());
      jwtObject.setCreatedAt(new Date(System.currentTimeMillis()));
      jwtObject.setExpiresAt((new Date(System.currentTimeMillis() + SecurityConfig.EXPIRATION)));
      jwtObject.setRoles(clientFound.getRoles());
      session.setToken(JWTCreator.create(SecurityConfig.PREFIX, SecurityConfig.KEY, jwtObject));
      return new ServiceResponse("OK", session);
    }else {
      throw new RuntimeException("Erro ao tentar fazer login");
    }
  }
}
