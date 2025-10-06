package com.example.securityapp.service;

import com.example.securityapp.dto.PaymentRequestDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

// Esta é uma simulação de um serviço de gateway de pagamento externo.
// Numa aplicação real, esta interface seria implementada por um cliente HTTP
// que se comunica com a API de um gateway como Stripe, PagSeguro, Adyen, etc.
@Service
public class PaymentGatewayService {

    public String processCreditCardPayment(PaymentRequestDTO request) {
        // Simula a comunicação com um gateway de cartão de crédito
        // Numa implementação real, enviaria o token do cartão e outros dados para o gateway
        // e receberia um ID de transação e status.
        System.out.println("Processando pagamento com cartão de crédito no valor de " + request.getAmount());
        if (Math.random() > 0.1) { // 90% de chance de sucesso
            return "TRANS_CC_" + System.currentTimeMillis(); // Retorna um ID de transação simulado
        } else {
            throw new RuntimeException("Pagamento com cartão de crédito recusado.");
        }
    }

    public String processPixPayment(PaymentRequestDTO request) {
        // Simula a comunicação com um gateway PIX
        System.out.println("Processando pagamento PIX no valor de " + request.getAmount());
        if (Math.random() > 0.05) { // 95% de chance de sucesso
            return "TRANS_PIX_" + System.currentTimeMillis(); // Retorna um ID de transação simulado
        } else {
            throw new RuntimeException("Pagamento PIX falhou.");
        }
    }

    public String processDebitPayment(PaymentRequestDTO request) {
        // Simula a comunicação com um gateway de débito
        System.out.println("Processando pagamento de débito no valor de " + request.getAmount());
        if (Math.random() > 0.15) { // 85% de chance de sucesso
            return "TRANS_DEBIT_" + System.currentTimeMillis(); // Retorna um ID de transação simulado
        } else {
            throw new RuntimeException("Pagamento de débito recusado.");
        }
    }
}

