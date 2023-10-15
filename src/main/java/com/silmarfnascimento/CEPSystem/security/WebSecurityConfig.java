package com.silmarfnascimento.CEPSystem.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

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

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(new JWTFilter(), UsernamePasswordAuthenticationFilter.class)
        .authorizeHttpRequests(authorize -> {
              authorize
                  .requestMatchers("/h2-console/**").permitAll()
                  .requestMatchers(HttpMethod.POST, "/login").permitAll()
                  .requestMatchers(HttpMethod.POST, "/users").permitAll()
                  .requestMatchers(HttpMethod.GET, "/users/**").hasAnyRole("USERS", "MANAGERS")
                  .requestMatchers(HttpMethod.PUT, "/users").hasAnyRole("USERS", "MANAGERS")
                  .requestMatchers(HttpMethod.DELETE, "/users").hasAnyRole("USERS", "MANAGERS")
                  .anyRequest().authenticated();
            }
        );
  }
}