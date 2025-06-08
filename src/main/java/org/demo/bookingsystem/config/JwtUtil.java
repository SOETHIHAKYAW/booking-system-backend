package org.demo.bookingsystem.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

/**
 * Author: sthk
 * Created At: June 7, 2025
 * <p>
 * This class provides utility methods for working with JWT (JSON Web Tokens).
 * It includes methods for generating, validating, and extracting claims from JWT tokens.
 * The class uses a secret key to sign the JWT and validate its authenticity.
 */
@Component
public class JwtUtil {

    private final Key signingKey; // The secret key used for signing the JWT

    @Value("${jwt.expiration}")
    private long expiration; // The expiration time for the JWT in milliseconds

    /**
     * Author: sthk
     * Created At: June 7, 2025
     * <p>
     * Constructor that initializes the JwtUtil with a secret key.
     * The secret is used to generate a signing key for the JWT.
     *
     * @param secret the secret key for signing the JWT, injected from application properties
     */
    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); // Create a signing key from the secret
    }

    /**
     * Author: sthk
     * Created At: June 7, 2025
     * <p>
     * Generates a JWT token for the given username.
     * The token contains the username as the subject, issue time, expiration time, and is signed with the secret key.
     *
     * @param username the username to be included in the token
     * @return the generated JWT token
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // Set the subject as the username
                .setIssuedAt(new Date()) // Set the issue time to the current time
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // Set the expiration time
                .signWith(signingKey, SignatureAlgorithm.HS512) // Sign the token with the secret key
                .compact(); // Generate the compact JWT string
    }

    /**
     * Author: sthk
     * Created At: June 7, 2025
     * <p>
     * Validates the given JWT token.
     * A token is considered valid if it is not expired.
     *
     * @param token the JWT token to validate
     * @return true if the token is valid, false otherwise
     */
    public boolean validateToken(String token) {
        return !isTokenExpired(token); // Check if the token is expired
    }

    /**
     * Author: sthk
     * Created At: June 7, 2025
     * <p>
     * Extracts the username (subject) from the JWT token.
     *
     * @param token the JWT token
     * @return the username (subject) extracted from the token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject); // Extract the subject (username) claim
    }

    /**
     * Author: sthk
     * Created At: June 7, 2025
     * <p>
     * Checks if the given JWT token is expired.
     *
     * @param token the JWT token to check
     * @return true if the token is expired, false otherwise
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date()); // Check if the token's expiration date has passed
    }

    /**
     * Author: sthk
     * Created At: June 7, 2025
     * <p>
     * Extracts the expiration date from the JWT token.
     *
     * @param token the JWT token
     * @return the expiration date of the token
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration); // Extract the expiration claim
    }

    /**
     * Author: sthk
     * Created At: June 7, 2025
     * <p>
     * Extracts a specific claim from the JWT token.
     * This is a generic method that can be used to extract various claims, such as subject, expiration, etc.
     *
     * @param token          the JWT token
     * @param claimsResolver a function to extract a specific claim from the JWT
     * @param <T>            the type of the claim
     * @return the extracted claim
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parserBuilder() // Parse the JWT token
                .setSigningKey(signingKey) // Set the signing key
                .build()
                .parseClaimsJws(token) // Parse the token into claims
                .getBody(); // Get the claims body
        return claimsResolver.apply(claims); // Apply the claimsResolver function to extract the desired claim
    }
}