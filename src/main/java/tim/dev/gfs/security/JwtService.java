package tim.dev.gfs.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    /*
     * Move these values to application.properties later.
     */
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    /**
     * Creates the signing key from the configured secret.
     */
    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generates a JWT for a successfully authenticated user.
     */
    public String generateToken(CustomUserDetails user) {

        return Jwts.builder()

                // Subject = username
                .subject(user.getUsername())

                // Token issued now
                .issuedAt(new Date())

                // Expiration
                .expiration(new Date(
                        System.currentTimeMillis() + expiration))

                // Sign token
                .signWith(getSigningKey())

                .compact();
    }

    /**
     * Extract all claims.
     */
    public Claims extractClaims(String token) {

        return Jwts.parser()

                .verifyWith(getSigningKey())

                .build()

                .parseSignedClaims(token)

                .getPayload();
    }

    /**
     * Returns username stored in token.
     */
    public String extractUsername(String token) {

        return extractClaims(token).getSubject();
    }

    /**
     * Checks if token is expired.
     */
    public boolean isTokenExpired(String token) {

        return extractClaims(token)

                .getExpiration()

                .before(new Date());
    }

    /**
     * Validates token.
     */
    public boolean isTokenValid(
            String token,
            CustomUserDetails user) {

        return user.getUsername().equals(extractUsername(token))
                && !isTokenExpired(token);
    }
}