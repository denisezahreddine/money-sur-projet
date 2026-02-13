package com.bschool.moneysur.service;

public interface EmailService {
    void sendVerificationEmail(String to, String token);
}