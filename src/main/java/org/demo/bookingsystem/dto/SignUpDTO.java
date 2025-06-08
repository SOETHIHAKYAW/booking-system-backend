package org.demo.bookingsystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Author: sthk
 * Created AT: June 7, 2025
 * <p>
 * This class represents the data transfer object for signing up a user.
 * It contains the username, password, and email fields with validation constraints.
 */
@Data
public class SignUpDTO {

    /**
     * The username of the user attempting to sign up
     */
    @NotBlank(message = "Username is required")
    @Size(max = 50)
    private String username;

    /**
     * The password of the user attempting to sign up
     */
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    /**
     * The email address of the user attempting to sign up
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    /**
     * The ISO country code of the user attempting to sign up
     * e.g. "SG" for Singapore, "MM" for Myanmar
     */
    @NotBlank(message = "Country code is required")
    @Size(min = 2, max = 2, message = "Country code must be exactly 2 characters long")
    @Schema(example = "SG", description = "Country code in ISO 3166-1 alpha-2 format, e.g. 'SG' for Singapore")
    private String countryCode;
}
