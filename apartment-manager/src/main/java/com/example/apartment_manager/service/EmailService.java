package com.example.apartment_manager.service;

public interface EmailService {
    void sendInvoiceEmail(String to, String subject, String content);
}
