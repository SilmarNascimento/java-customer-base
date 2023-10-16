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
import java.util.List;
import java.util.stream.Collectors;

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
      try {
        if(token!=null && !token.isEmpty()) {
          JWTObject tokenUserObject = JWTCreator.create(token,SecurityConfig.PREFIX, SecurityConfig.KEY);

          Client clientFound = clientRepository.findByUsername(tokenUserObject.getUsername());

          var passwordVerify = BCrypt.verifyer().verify(clientFound.getPassword().toCharArray(), tokenUserObject.getPassword());
          if(passwordVerify.verified) {
            /*UsernamePasswordAuthenticationToken userToken =
                new UsernamePasswordAuthenticationToken(
                    tokenUserObject.getUsername(),
                    tokenUserObject.getPassword(),
                    authorities);
            SecurityContextHolder.getContext().setAuthentication(userToken);*/
            request.setAttribute("idUser", clientFound.getId());
            filterChain.doFilter(request, response);
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
