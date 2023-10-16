package com.silmarfnascimento.CEPSystem.security;

import io.jsonwebtoken.*;

public class JWTCreator {
  public static final String HEADER_AUTHORIZATION = "Authorization";

  public static String create(String prefix,String key, JWTObject jwtObject) {
    System.out.println(jwtObject.toString());
    System.out.println(prefix);
    System.out.println(key);
    String token = Jwts.builder()
        .setSubject(jwtObject.getUsername())
        .claim("password",jwtObject.getPassword())
        .setIssuedAt(jwtObject.getCreatedAt())
        .setExpiration(jwtObject.getExpiresAt())
        .signWith(SignatureAlgorithm.HS256, key)
        .compact();
    return prefix + " " + token;
  }
  public static JWTObject create(String authToken,String prefix,String key)
      throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException {
    String token = authToken.replace(prefix, "").trim();
    System.out.println(token);
    Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();

    JWTObject object = new JWTObject();
    object.setUsername(claims.getSubject());
    object.setPassword(claims.get("password", String.class));
    object.setExpiresAt(claims.getExpiration());
    object.setCreatedAt(claims.getIssuedAt());
    return object;

  }
}
