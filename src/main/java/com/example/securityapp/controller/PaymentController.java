package com.example.securityapp.controller;

import com.example.securityapp.dto.PaymentRequestDTO;
import com.example.securityapp.model.Transaction;
import com.example.securityapp.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // O processamento de pagamento agora é feito através do OrderController
    // Este controlador pode ser removido ou adaptado para outras funcionalidades de pagamento, se necessário.
    // @PostMapping("/process")
    // @PreAuthorize("isAuthenticated()") // Apenas utilizadores autenticados podem fazer pagamentos
    // public ResponseEntity<?> processPayment(@Valid @RequestBody PaymentRequestDTO paymentRequestDTO) {
    //     try {
    //         Transaction transaction = paymentService.processPayment(paymentRequestDTO);
    //         return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    //     } catch (IllegalArgumentException e) {
    //         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    //     } catch (RuntimeException e) {
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao processar pagamento: " + e.getMessage());
    //     }
    // }
}

