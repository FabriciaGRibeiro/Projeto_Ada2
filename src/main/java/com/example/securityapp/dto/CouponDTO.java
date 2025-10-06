package com.example.securityapp.dto;

import com.example.securityapp.model.Coupon;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CouponDTO {

    @NotBlank(message = "O código do cupom é obrigatório")
    private String code;

    @NotNull(message = "O tipo de desconto é obrigatório")
    private Coupon.DiscountType discountType;

    @NotNull(message = "O valor do desconto é obrigatório")
    @DecimalMin(value = "0.01", message = "O valor do desconto deve ser maior que zero")
    private BigDecimal discountValue;

    private LocalDate expirationDate;

    @DecimalMin(value = "0.00", message = "O valor mínimo do pedido não pode ser negativo")
    private BigDecimal minOrderAmount = BigDecimal.ZERO;

    private boolean active = true;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Coupon.DiscountType getDiscountType() {
        return discountType;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public LocalDate getExpirationDate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public BigDecimal getMinOrderAmount() {
        return minOrderAmount;
    }

    public void setMinOrderAmount(BigDecimal minOrderAmount) {
        this.minOrderAmount = minOrderAmount;
    }

    public boolean isActive() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
