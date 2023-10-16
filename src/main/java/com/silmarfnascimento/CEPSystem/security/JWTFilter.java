package com.silmarfnascimento.CEPSystem.security;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.silmarfnascimento.CEPSystem.model.Client;
import com.silmarfnascimento.CEPSystem.repository.IClientRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {
  @Autowired
  private IClientRepository clientRepository;
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    var servletPath = request.getServletPath();
    if (servletPath.startsWith("/users") && request.getMethod().equals("POST")) {
      filterChain.doFilter(request, response);
    } else if(servletPath.startsWith("/auth/login") && request.getMethod().equals("POST")) {
      filterChain.doFilter(request, response);
    } else {
      String token =  request.getHeader(JWTCreator.HEADER_AUTHORIZATION);
      System.out.println(token);
      try {
        if(token!=null && !token.isEmpty()) {
          JWTObject tokenUserObject = JWTCreator.create(token,SecurityConfig.PREFIX, SecurityConfig.KEY);
          System.out.println(tokenUserObject.toString());

          Client clientFound = clientRepository.findByUsername(tokenUserObject.getUsername());
          if(clientFound == null) {
            response.sendError(404, "usuário não encontrado");
            return;
          }
          if(clientFound.getPassword().equals(tokenUserObject.getPassword())) {
            request.setAttribute("idUser", clientFound.getId());
            filterChain.doFilter(request, response);
            return;
          }
          response.sendError(403, "usuário não autorizado");
        }else {
          response.sendError(403, "Usuário não encontrado");
        }
        filterChain.doFilter(request, response);
      }catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException e) {
        System.out.println("Error processing JWT: " + e.getMessage());
        response.setStatus(HttpStatus.FORBIDDEN.value());
      }
    }
  }
  /* private List<SimpleGrantedAuthority> authorities(List<String> roles){
    return roles.stream().map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());
  }*/
}
