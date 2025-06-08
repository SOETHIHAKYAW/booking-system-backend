package org.demo.bookingsystem.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Author: sthk
 * Created At: June 7, 2025
 * <p>
 * SwaggerConfig is responsible for configuring Swagger/OpenAPI for the application.
 * It defines how the API documentation is structured, including security schemes and grouping.
 */
@Configuration
public class SwaggerConfig {

    /**
     * Author: sthk
     * Created At: June 7, 2025
     * <p>
     * Configures Swagger for grouping the API.
     * The group name is used to organize the API documentation.
     *
     * @return GroupedOpenApi for the public API
     */
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("booking-system-api") // Define the group name for the API documentation
                .pathsToMatch("/api/v1/**") // Match all paths that start with "/api/v1"
                .build(); // Build the GroupedOpenApi object
    }

    /**
     * Author: sthk
     * Created At: June 7, 2025
     * <p>
     * Configures the OpenAPI documentation with security settings.
     * This setup includes adding Bearer token support for secured endpoints.
     * It configures Swagger to expect JWT tokens in the "Authorization" header.
     *
     * @return OpenAPI object with security configuration
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("BookingSystem API") // Title for the API documentation
                        .version("1.0.0") // Version of the API
                        .description("API documentation for BookingSystem application")) // Description of the API
                .components(new Components()
                        .addSecuritySchemes("Bearer", // Define a security scheme named "Bearer"
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP) // Define the security scheme as HTTP
                                        .scheme("bearer") // Set the scheme to "bearer" for JWT
                                        .bearerFormat("JWT") // Specify the format of the bearer token as JWT
                                        .description("Enter your Bearer token in the format: Bearer <token>"))) // Provide a description for the Bearer token
                .addSecurityItem(new SecurityRequirement().addList("Bearer")); // Add the Bearer token requirement for API security
    }
}