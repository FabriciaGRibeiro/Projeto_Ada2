package com.example.securityapp.controller;

import com.example.securityapp.dto.DiscountRuleDTO;
import com.example.securityapp.model.DiscountRule;
import com.example.securityapp.service.DiscountRuleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/discount-rules")
@PreAuthorize("hasRole(\'ADMIN\')") // Todos os endpoints de regras de desconto exigem role ADMIN
public class DiscountRuleController {

    private final DiscountRuleService discountRuleService;

    public DiscountRuleController(DiscountRuleService discountRuleService) {
        this.discountRuleService = discountRuleService;
    }

    @PostMapping
    public ResponseEntity<DiscountRule> createDiscountRule(@Valid @RequestBody DiscountRuleDTO discountRuleDTO) {
        DiscountRule newRule = discountRuleService.createDiscountRule(discountRuleDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newRule);
    }

    @GetMapping
    public ResponseEntity<List<DiscountRule>> getAllDiscountRules() {
        List<DiscountRule> rules = discountRuleService.findAllDiscountRules();
        return ResponseEntity.ok(rules);
    }

    @GetMapping("/active")
    public ResponseEntity<List<DiscountRule>> getActiveDiscountRules() {
        List<DiscountRule> rules = discountRuleService.findActiveDiscountRules();
        return ResponseEntity.ok(rules);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiscountRule> getDiscountRuleById(@PathVariable Long id) {
        try {
            DiscountRule rule = discountRuleService.findDiscountRuleById(id);
            return ResponseEntity.ok(rule);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDiscountRule(@PathVariable Long id, @Valid @RequestBody DiscountRuleDTO discountRuleDTO) {
        try {
            DiscountRule updatedRule = discountRuleService.updateDiscountRule(id, discountRuleDTO);
            return ResponseEntity.ok(updatedRule);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar regra de desconto: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivateDiscountRule(@PathVariable Long id) {
        try {
            DiscountRule deactivatedRule = discountRuleService.deactivateDiscountRule(id);
            return ResponseEntity.ok(deactivatedRule);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao desativar regra de desconto: " + e.getMessage());
        }
    }
}

