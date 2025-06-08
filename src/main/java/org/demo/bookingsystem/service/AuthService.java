package org.demo.bookingsystem.service;

import org.demo.bookingsystem.dto.SignInDTO;
import org.demo.bookingsystem.dto.SignUpDTO;
import org.demo.bookingsystem.dto.UserDTO;

public interface AuthService {

    /**
     * Author: sthk
     * Created AT: June 7, 2025
     * Saves a user in the database.
     */
    UserDTO signup(SignUpDTO signUpDTO);

    /**
     * Author: sthk
     * Created AT: June 7, 2025
     * Authenticates a user and returns their details.
     */
    UserDTO signIn(SignInDTO signInDTO);

    /**
     * Author: sthk
     * Created AT: June 7, 2025
     * Eamil verification method.
     */
    void verifyEmail(String code);
}