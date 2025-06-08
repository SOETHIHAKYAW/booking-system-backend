package org.demo.bookingsystem.service;

public interface EmailService {

    boolean sendVerifyEmail(String email, String verificationCode);

    boolean sendNewTempPassword(String email, String newPassword);
}
