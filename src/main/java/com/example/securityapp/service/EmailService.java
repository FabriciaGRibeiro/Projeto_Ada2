package com.example.securityapp.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public void sendEmail(Object to, String subject, String body) {
        System.out.println("Simulando envio de e-mail para: " + to);
        System.out.println("Assunto: " + subject);
        System.out.println("Corpo: " + body);
        System.out.println("--------------------------------------");
        // Numa aplicação real, usaria JavaMailSender para enviar o e-mail
    }
}

