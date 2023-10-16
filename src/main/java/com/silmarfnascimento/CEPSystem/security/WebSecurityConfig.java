package com.silmarfnascimento.CEPSystem.security;

import jakarta.servlet.Servlet;
import org.h2.server.web.WebServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServlet;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig{

  @Bean
  public BCryptPasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }

  private static final String[] SWAGGER_WHITELIST = {
      "/v2/api-docs",
      "/swagger-resources",
      "/swagger-resources/**",
      "/configuration/ui",
      "/configuration/security",
      "/swagger-ui.html",
      "/webjars/**"
  };

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
        .cors(cors -> cors.disable())
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterAfter(new JWTFilter(), UsernamePasswordAuthenticationFilter.class)
        .authorizeHttpRequests(authorize -> {
              authorize
                  .requestMatchers(SWAGGER_WHITELIST).permitAll()
                  .requestMatchers("/h2-console/**").permitAll()
                  .requestMatchers(HttpMethod.POST, "/login").permitAll()
                  .requestMatchers(HttpMethod.POST, "/users").permitAll()
                  .requestMatchers(HttpMethod.GET, "/users/**").hasAnyRole("USERS", "MANAGERS")
                  .requestMatchers(HttpMethod.PUT, "/users").hasAnyRole("USERS", "MANAGERS")
                  .requestMatchers(HttpMethod.DELETE, "/users").hasAnyRole("USERS", "MANAGERS")
                  .anyRequest().authenticated();
            }
        )
        .build();
  }
  /*
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }*/
  @Bean
  public ServletRegistrationBean<Servlet> h2servletRegistration() {
    ServletRegistrationBean<Servlet> registrationBean = new ServletRegistrationBean<>((Servlet) new WebServlet());
    registrationBean.addUrlMappings("/h2-console/*");
    return registrationBean;
  }
}