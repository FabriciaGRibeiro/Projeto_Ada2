package com.example.securityapp.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User client;

    @Column(nullable = false)
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status; // OPEN, PENDING_PAYMENT, PAID, DELIVERED, CANCELLED

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus; // PENDING, APPROVED, DECLINED

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus deliveryStatus; // PENDING, IN_TRANSIT, DELIVERED

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal discountAmount;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    public List<OrderItem> getItems() {
    return items;

    public void setClient(User client) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setOrderDate(LocalDateTime now) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setStatus(OrderStatus orderStatus) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setDiscountAmount(BigDecimal ZERO) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setTotalAmount(BigDecimal ZERO) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public OrderStatus getStatus() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List getItems() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getTotalAmount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getClient() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public PaymentStatus getPaymentStatus() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public BigDecimal getDiscountAmount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public enum OrderStatus {
        OPEN,
        PENDING_PAYMENT,
        PAID,
        DELIVERED,
        CANCELLED
    }

    public enum PaymentStatus {
        PENDING,
        APPROVED,
        DECLINED
    }

    public enum DeliveryStatus {
        PENDING,
        IN_TRANSIT,
        DELIVERED
    }
}
