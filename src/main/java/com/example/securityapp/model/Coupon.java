package com.example.securityapp.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "coupons")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiscountType discountType; // PERCENTAGE, FIXED

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal discountValue;

    private LocalDate expirationDate;

    @Column(precision = 10, scale = 2)
    private BigDecimal minOrderAmount; // Valor mínimo do pedido para aplicar o cupom

    @Column(nullable = false)
    private boolean active = true;

    public DiscountType getDiscountType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setCode(String code) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setActive(boolean b) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setExpirationDate(LocalDate now) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isActive() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getExpirationDate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getMinOrderAmount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getCode() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setDiscountValue(BigDecimal discountValue) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setMinOrderAmount(BigDecimal minOrderAmount) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setDiscountType(DiscountType discountType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public BigDecimal getDiscountValue() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public enum DiscountType {
        PERCENTAGE,
        FIXED
    }
}
