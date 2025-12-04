package com.project.BidIT.Compontents;

import com.project.BidIT.entity.Admin;
import com.project.BidIT.entity.DeliveryMan;
import com.project.BidIT.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET = "MzJieXRlc2xvbmdzZWNyZXRrZXlmb3Jqd3QxMjM0NTY=";

    // Generate JWT token
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) // 24h
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    public String generateTokenAdmin(Admin admin) {
        return Jwts.builder()
                .setSubject(admin.getEmail())
                .claim("id", admin.getAdminId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) // 24h
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    public String generateTokenDeliveryMan(DeliveryMan dm) {
        return Jwts.builder()
                .setSubject(dm.getdEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) // 24h
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    // Extract email (username) from JWT
    public String extractEmail(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    // Validate token (checks expiration)
    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    // Optional: validate token against user
    public boolean isTokenValid(String token, UserDetails userDetails) {
        String email = extractEmail(token);
        return email.equals(userDetails.getUsername());
    }
    public boolean isTokenValidAdmin(String token, Admin admin) {
        String email = extractEmail(token);
        return email != null && email.equals(admin.getEmail());
    }
    public boolean isTokenValidDeliveryMan(String token, DeliveryMan dm) {
        String email = extractEmail(token);
        return email != null && email.equals(dm.getdEmail());
    }
}
