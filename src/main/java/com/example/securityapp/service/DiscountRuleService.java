package com.example.securityapp.service;

import com.example.securityapp.dto.DiscountRuleDTO;
import com.example.securityapp.model.DiscountRule;
import com.example.securityapp.repository.DiscountRuleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DiscountRuleService {

    private final DiscountRuleRepository discountRuleRepository;

    public DiscountRuleService(DiscountRuleRepository discountRuleRepository) {
        this.discountRuleRepository = discountRuleRepository;
    }

    @Transactional
    public DiscountRule createDiscountRule(DiscountRuleDTO discountRuleDTO) {
        DiscountRule rule = new DiscountRule();
        rule.setName(discountRuleDTO.getName());
        rule.setDescription(discountRuleDTO.getDescription());
        rule.setRuleType(discountRuleDTO.getRuleType());
        rule.setConditions(discountRuleDTO.getConditions());
        rule.setDiscountAction(discountRuleDTO.getDiscountAction());
        rule.setActive(discountRuleDTO.isActive());
        return discountRuleRepository.save(rule);
    }

    public List<DiscountRule> findAllDiscountRules() {
        return discountRuleRepository.findAll();
    }

    public List<DiscountRule> findActiveDiscountRules() {
        return discountRuleRepository.findByActiveTrue();
    }

    public DiscountRule findDiscountRuleById(Long id) {
        return discountRuleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Regra de desconto não encontrada com ID: " + id));
    }

    @Transactional
    public DiscountRule updateDiscountRule(Long id, DiscountRuleDTO discountRuleDTO) {
        DiscountRule existingRule = findDiscountRuleById(id);

        existingRule.setName(discountRuleDTO.getName());
        existingRule.setDescription(discountRuleDTO.getDescription());
        existingRule.setRuleType(discountRuleDTO.getRuleType());
        existingRule.setConditions(discountRuleDTO.getConditions());
        existingRule.setDiscountAction(discountRuleDTO.getDiscountAction());
        existingRule.setActive(discountRuleDTO.isActive());

        return discountRuleRepository.save(existingRule);
    }

    @Transactional
    public DiscountRule deactivateDiscountRule(Long id) {
        DiscountRule existingRule = findDiscountRuleById(id);
        existingRule.setActive(false);
        return discountRuleRepository.save(existingRule);
    }
}

