package com.example.securityapp.repository;

import com.example.securityapp.model.DiscountRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiscountRuleRepository extends JpaRepository<DiscountRule, Long> {
    List<DiscountRule> findByActiveTrue();
}

