package org.demo.bookingsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Author: sthk
 * Created AT: June 7, 2025
 * <p>
 * This class represents the data transfer object for signing in.
 * It contains the username and password fields with validation constraints.
 */
@Data
public class SignInDTO {

    /**
     * The username of the user attempting to sign in
     */
    @NotBlank
    @Size(max = 50)
    private String username;

    /**
     * The password of the user attempting to sign in
     */
    @NotBlank
    @Size(min = 6, max = 100)
    private String password;
}
