package com.example.securityapp.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "discount_rules")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RuleType ruleType; // SIMPLE, COMPOUND

    @Lob // Para armazenar JSON ou texto longo
    @Column(nullable = false)
    private String conditions; // JSON string defining conditions (e.g., {"minItems": 3, "category": "Electronics"})

    @Lob // Para armazenar JSON ou texto longo
    @Column(nullable = false)
    private String discountAction; // JSON string defining action (e.g., {"type": "PERCENTAGE", "value": 10})

    @Column(nullable = false)
    private boolean active = true;

    public enum RuleType {
        SIMPLE,
        COMPOUND
    }
}

