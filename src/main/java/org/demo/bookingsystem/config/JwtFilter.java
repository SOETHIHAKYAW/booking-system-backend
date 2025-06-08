/**
 * Author: sthk
 * Created At: June 7, 2025
 * <p>
 * This class is a filter for handling JWT authentication in the application.
 * It extracts the JWT token from the request, validates it, and sets the authentication in the security context.
 * The filter is applied to every request to ensure proper authentication.
 */

package org.demo.bookingsystem.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Author: sthk
 * Created At: June 7, 2025
 * <p>
 * JwtFilter is responsible for intercepting HTTP requests to extract and validate JWT tokens.
 * If the token is valid, it sets the authentication details into the security context for the request.
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil; // Utility class for JWT operations such as validation and username extraction

    /**
     * Author: sthk
     * Created At: June 7, 2025
     * <p>
     * This method is executed for every incoming HTTP request.
     * It extracts the JWT token from the request, validates it, and sets the authentication if the token is valid.
     *
     * @param request     the HTTP request
     * @param response    the HTTP response
     * @param filterChain the filter chain for further processing of the request
     * @throws ServletException if an error occurs during the filter process
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = extractToken(request); // Extract the token from the request
        if (token != null && jwtUtil.validateToken(token)) { // Validate the token
            String username = jwtUtil.extractUsername(token); // Extract the username from the token
            // If the token is valid, set the authentication context
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(username, null, null));
        }
        filterChain.doFilter(request, response); // Continue the filter chain
    }

    /**
     * Author: sthk
     * Created At: June 7, 2025
     * <p>
     * Extracts the JWT token from the Authorization header of the HTTP request.
     * The token is expected to have the "Bearer " prefix.
     *
     * @param request the HTTP request
     * @return the extracted token or null if not found
     */
    private String extractToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization"); // Get the Authorization header
        if (token != null && token.startsWith("Bearer ")) { // Check if the header starts with "Bearer "
            return token.substring(7); // Return the token without the "Bearer " prefix
        }
        return null; // Return null if the token is not found
    }
}