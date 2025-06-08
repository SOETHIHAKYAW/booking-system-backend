package org.demo.bookingsystem.config;

import org.demo.bookingsystem.service.impl.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Author: sthk
 * Created At: June 7, 2025
 * <p>
 * This class configures security settings for the application.
 * It includes settings for CORS, authentication, password encoding, and session management.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter; // JWT filter for handling JWT token validation
    private final CustomUserDetailsService customUserDetailsService;


    /**
     * Author: sthk
     * Created At: June 7, 2025
     * <p>
     * Constructor to initialize JwtFilter and UserDetailsService.
     */
    public SecurityConfig(JwtFilter jwtFilter, CustomUserDetailsService customUserDetailsService) {
        this.jwtFilter = jwtFilter;
        this.customUserDetailsService = customUserDetailsService;
    }

    /**
     * Author: sthk
     * Created At: June 7, 2025
     * <p>
     * Bean for password encoding using BCrypt.
     * BCrypt is a strong hashing algorithm for securing passwords.
     *
     * @return a BCryptPasswordEncoder for password encoding
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Return an instance of BCryptPasswordEncoder
    }

    /**
     * Author: sthk
     * Created At: June 7, 2025
     * <p>
     * Configures HTTP security for the application.
     * This includes disabling CSRF, enabling CORS, defining access rules for different endpoints,
     * and setting session management to stateless (since JWTs are used for authentication).
     *
     * @param http the HttpSecurity object used to configure security
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs while configuring HTTP security
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // Disable CSRF protection (because we're using JWTs)
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Enable CORS with custom configuration
                .authorizeHttpRequests(auth -> auth
                        // Permit access to Swagger UI and API docs endpoints without authentication
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",     // explicit swagger-ui page
                                "/v3/api-docs/**",
                                "/v3/api-docs.yaml"     // sometimes requested by clients
                        ).permitAll()
                        // Permit access to authentication-related endpoints
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/api/v1/roles/**").permitAll()
                        // Require authentication for all other endpoints
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Stateless session (no server-side session)
                );

        // Add the JWT filter before the UsernamePasswordAuthenticationFilter to check the token on each request
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Security configuration for development purposes
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()) // Permit all requests
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//
//        // Don't add JWT filter since security is disabled
//        return http.build();
//    }

    /**
     * Author: sthk
     * Created At: June 7, 2025
     * <p>
     * Bean for AuthenticationManager.
     * This bean is used to authenticate users during the login process.
     *
     * @param http the HttpSecurity object used to build the AuthenticationManager
     * @return the configured AuthenticationManager
     * @throws Exception if an error occurs while configuring the AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }

    /**
     * Author: sthk
     * Created At: June 7, 2025
     * <p>
     * CORS configuration source for allowing frontend connections.
     * This method configures which origins, methods, and headers are allowed for CORS requests.
     *
     * @return the configured UrlBasedCorsConfigurationSource
     */
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000")); // Frontend URL
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Allow common HTTP methods
        config.setAllowedHeaders(List.of("*")); // Allow all headers
        config.setAllowCredentials(true); // Allow credentials (cookies, authorization headers)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // Apply the CORS configuration to all endpoints
        return source; // Return the configured CORS source
    }
}