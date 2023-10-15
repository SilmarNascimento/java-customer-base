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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JWTFilter extends OncePerRequestFilter {
  @Autowired
  private IClientRepository clientRepository;
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String token =  request.getHeader(JWTCreator.HEADER_AUTHORIZATION);
    try {
      if(token!=null && !token.isEmpty()) {
        JWTObject tokenUserObject = JWTCreator.create(token,SecurityConfig.PREFIX, SecurityConfig.KEY);

        Client clientFound = clientRepository.findByUsername(tokenUserObject.getUsername());
        List<SimpleGrantedAuthority> authorities = authorities(tokenUserObject.getRoles());

        var passwordVerify = BCrypt.verifyer().verify(clientFound.getPassword().toCharArray(), tokenUserObject.getPassword());
        if(passwordVerify.verified) {
          UsernamePasswordAuthenticationToken userToken =
              new UsernamePasswordAuthenticationToken(
                  tokenUserObject.getUsername(),
                  tokenUserObject.getPassword(),
                  authorities);
          SecurityContextHolder.getContext().setAuthentication(userToken);
        }

        SecurityContextHolder.clearContext();
      }else {
        SecurityContextHolder.clearContext();
      }
      filterChain.doFilter(request, response);
    }catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException e) {
      e.printStackTrace();
      response.setStatus(HttpStatus.FORBIDDEN.value());
    }
  }
  private List<SimpleGrantedAuthority> authorities(List<String> roles){
    return roles.stream().map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());
  }
}
