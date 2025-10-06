package com.example.securityapp.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String paymentMethod;

    @Column(nullable = false)
    private String status; // Ex: PENDING, APPROVED, DECLINED, REFUNDED

    private String transactionIdGateway; // ID da transação no gateway de pagamento

    @Column(nullable = false)
    private LocalDateTime transactionDate;

    // Getters e Setters são gerados pelo Lombok (@Data)

    public void setUser(User user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setStatus(String status) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setTransactionIdGateway(String transactionIdGateway) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setTransactionDate(LocalDateTime now) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setAmount(Object totalAmount) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'setAmount'");
    }

    public void setPaymentMethod(Object paymentMethod2) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'setPaymentMethod'");
    }
}

