package com.example.securityapp.dto;

import com.example.securityapp.model.DiscountRule;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DiscountRuleDTO {

    @NotBlank(message = "O nome da regra é obrigatório")
    private String name;

    private String description;

    @NotNull(message = "O tipo de regra é obrigatório")
    private DiscountRule.RuleType ruleType;

    @NotBlank(message = "As condições da regra são obrigatórias (formato JSON)")
    private String conditions;

    @NotBlank(message = "A ação de desconto é obrigatória (formato JSON)")
    private String discountAction;

    private boolean active = true;
}

