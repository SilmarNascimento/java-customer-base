package com.silmarfnascimento.CEPSystem.security;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Configuration
@Component
public class SecurityConfig {
  @Value("${security.config.prefix}")
  public static String PREFIX = "Bearer";

  @Value("${security.config.key}")
  public static String KEY = "SECRET_KEY";

  @Value("${security.config.expiration}")
  public static Long EXPIRATION = 3600000L;
}
