package com.example.securityapp.controller;

import com.example.securityapp.dto.CouponDTO;
import com.example.securityapp.model.Coupon;
import com.example.securityapp.service.CouponService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coupons")
@PreAuthorize("hasRole(\'ADMIN\')") // Todos os endpoints de cupom exigem role ADMIN
public class CouponController {

    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping
    public ResponseEntity<Coupon> createCoupon(@Valid @RequestBody CouponDTO couponDTO) {
        try {
            Coupon newCoupon = couponService.createCoupon(couponDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(newCoupon);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<Coupon>> getAllCoupons() {
        List<Coupon> coupons = couponService.findAllCoupons();
        return ResponseEntity.ok(coupons);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Coupon> getCouponById(@PathVariable Long id) {
        try {
            Coupon coupon = couponService.findCouponById(id);
            return ResponseEntity.ok(coupon);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCoupon(@PathVariable Long id, @Valid @RequestBody CouponDTO couponDTO) {
        try {
            Coupon updatedCoupon = couponService.updateCoupon(id, couponDTO);
            return ResponseEntity.ok(updatedCoupon);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar cupom: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/expire")
    public ResponseEntity<?> expireCoupon(@PathVariable Long id) {
        try {
            Coupon expiredCoupon = couponService.expireCoupon(id);
            return ResponseEntity.ok(expiredCoupon);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao expirar cupom: " + e.getMessage());
        }
    }
}

