package com.example.securityapp.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice; // Preço de venda do produto no momento da adição ao pedido

    public void setOrder(Order order) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setProduct(Product product) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setQuantity(Integer newQuantity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Integer getQuantity() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getProduct() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

