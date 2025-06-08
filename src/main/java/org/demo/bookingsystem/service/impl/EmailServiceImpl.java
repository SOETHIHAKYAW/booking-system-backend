package org.demo.bookingsystem.service.impl;

import org.demo.bookingsystem.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Override
    public boolean sendVerifyEmail(String email, String verificationCode) {
        // Mock sending email, always returns true
        logger.info("Sending email to: {} with code: {}", email, verificationCode);
        return true;
    }

    @Override
    public boolean sendNewTempPassword(String email, String newPassword) {
        // Mock sending email, always returns true
        logger.info("Sending email to: {} with new temporary Password is : {}", email, newPassword);
        return true;
    }
}