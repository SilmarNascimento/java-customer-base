package com.silmarfnascimento.CEPSystem.security;

import io.jsonwebtoken.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class JWTCreator {
  public static final String HEADER_AUTHORIZATION = "Authorization";
  public static final String ROLES_AUTHORITIES = "authorities";

  public static String create(String prefix,String key, JWTObject jwtObject) {
    String token = Jwts.builder()
        .setSubject(jwtObject.getUsername())
        .claim("password",jwtObject.getPassword())
        .setIssuedAt(jwtObject.getCreatedAt())
        .setExpiration(jwtObject.getExpiresAt())
        .claim(ROLES_AUTHORITIES, checkRoles(jwtObject.getRoles()))
        .signWith(SignatureAlgorithm.HS512, key)
        .compact();
    return prefix + " " + token;
  }
  public static JWTObject create(String authToken,String prefix,String key)
      throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException {
    JWTObject object = new JWTObject();
    String token = authToken.replace(prefix, "");
    System.out.println(token);
    Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();

    object.setUsername(claims.getSubject());
    object.setPassword(claims.get("password", String.class));
    object.setExpiresAt(claims.getExpiration());
    object.setCreatedAt(claims.getIssuedAt());
    object.setRoles(claims.get(ROLES_AUTHORITIES, List.class));
    return object;

  }
  private static List<String> checkRoles(List<String> roles) {
    return roles.stream().map(s -> "ROLE_".concat(s.replaceAll("ROLE_",""))).collect(Collectors.toList());
  }
}
