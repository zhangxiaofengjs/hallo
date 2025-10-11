package com.hallo.fw.config;

import java.util.Date;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * 
 * @author zhangxiaofeng
 * @create 2025年10月11日 13:58:50
 */
public class JwtUtil {
  private static final SecretKey KEY = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
  private static final long EXPIRATION = 86400000 * 7; // 7天

  public static String createToken(String uid) {
    return Jwts.builder()
        .setSubject(uid)
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
        .signWith(KEY)
        .compact();
  }

  public static Claims parseToken(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(KEY)
        .build()
        .parseClaimsJws(token)
        .getBody();
  }
}
