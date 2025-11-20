package az.devlab.aiquestiongenerator.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    private Key getSigningKey() {
        byte[] keyBytes = jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(CustomUserDetails userDetails) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtProperties.getAccessTokenExpirationMs());

        return Jwts.builder()
                .setSubject(userDetails.getId().toString())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .claim("username", userDetails.getUsername())
                .claim("email", userDetails.getEmail())
                .claim("roles", userDetails.getAuthorities())
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateRefreshToken(CustomUserDetails userDetails) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtProperties.getRefreshTokenExpirationMs());

        return Jwts.builder()
                .setSubject(userDetails.getId().toString())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = parseClaims(token).getBody();
        String subject = claims.getSubject();
        return Long.parseLong(subject);
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    private Jws<Claims> parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
    }
}
