package com.example.securityapp.service;

import com.example.securityapp.dto.PaymentRequestDTO;
import com.example.securityapp.model.Order;
import com.example.securityapp.model.Transaction;
import com.example.securityapp.model.User;
import com.example.securityapp.repository.TransactionRepository;
import com.example.securityapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class PaymentService{

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final PaymentGatewayService paymentGatewayService;

    public PaymentService(TransactionRepository transactionRepository, UserRepository userRepository, PaymentGatewayService paymentGatewayService) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.paymentGatewayService = paymentGatewayService;
    }

    @Transactional
    public Transaction processPayment(Order order, PaymentRequestDTO requestDTO) {
        User user = (User) order.getClient();

        if (((BigDecimal) order.getTotalAmount()).compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor do pagamento deve ser positivo.");
        }

        String transactionIdGateway = null;
        String status = "PENDING";

        try {
            switch (((String) requestDTO.getPaymentMethod()).toUpperCase()) {
                case "CREDIT_CARD":

                    transactionIdGateway = paymentGatewayService.processCreditCardPayment(requestDTO);
                    break;
                case "PIX":
                    transactionIdGateway = paymentGatewayService.processPixPayment(requestDTO);
                    break;
                case "DEBIT":
                    transactionIdGateway = paymentGatewayService.processDebitPayment(requestDTO);
                    break;
                default:
                    throw new IllegalArgumentException("Método de pagamento não suportado: " + requestDTO.getPaymentMethod());
            }
            status = "APPROVED";
        } catch (RuntimeException e) {
            status = "DECLINED";
            System.err.println("Erro ao processar pagamento: " + e.getMessage());
            throw new RuntimeException("Falha no processamento do pagamento: " + e.getMessage());
        }

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setAmount(order.getTotalAmount()); // Usa o total do pedido
        transaction.setPaymentMethod(requestDTO.getPaymentMethod());
        transaction.setStatus(status);
        transaction.setTransactionIdGateway(transactionIdGateway);
        transaction.setTransactionDate(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }
}
