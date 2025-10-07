package com.example.securityapp.service;

import com.example.securityapp.dto.CouponDTO;
import com.example.securityapp.model.Coupon;
import com.example.securityapp.repository.CouponRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class CouponService {

    private final CouponRepository couponRepository;

    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @Transactional
    public Coupon createCoupon(CouponDTO couponDTO) {
        if (couponRepository.findByCode(couponDTO.getCode()).isPresent()) {
            throw new IllegalArgumentException("Já existe um cupom com este código.");
        }
        Coupon coupon = new Coupon();
        coupon.setCode(couponDTO.getCode());
        coupon.setDiscountType(couponDTO.getDiscountType());
        coupon.setDiscountValue(couponDTO.getDiscountValue());
        coupon.setExpirationDate(couponDTO.getExpirationDate());
        coupon.setMinOrderAmount(couponDTO.getMinOrderAmount());
        coupon.setActive(couponDTO.isActive());
        return couponRepository.save(coupon);
    }

    public List<Coupon> findAllCoupons() {
        return couponRepository.findAll();
    }

    public Coupon findCouponById(Long id) {
        return couponRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cupom não encontrado com ID: " + id));
    }

    public Coupon findCouponByCode(String code) {
        return couponRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Cupom não encontrado com código: " + code));
    }

    @Transactional
    public Coupon updateCoupon(Long id, CouponDTO couponDTO) {
        Coupon existingCoupon = findCouponById(id);

        if (!existingCoupon.getCode().equals(couponDTO.getCode()) && couponRepository.findByCode(couponDTO.getCode()).isPresent()) {
            throw new IllegalArgumentException("Já existe outro cupom com este código.");
        }

        existingCoupon.setCode(couponDTO.getCode());
        existingCoupon.setDiscountType(couponDTO.getDiscountType());
        existingCoupon.setDiscountValue(couponDTO.getDiscountValue());
        existingCoupon.setExpirationDate(couponDTO.getExpirationDate());
        existingCoupon.setMinOrderAmount(couponDTO.getMinOrderAmount());
        existingCoupon.setActive(couponDTO.isActive());

        return couponRepository.save(existingCoupon);
    }

    @Transactional
    public Coupon expireCoupon(Long id) {
        Coupon existingCoupon = findCouponById(id);
        existingCoupon.setActive(false);
        existingCoupon.setExpirationDate(LocalDate.now()); // Define a data de expiração para hoje
        return couponRepository.save(existingCoupon);
    }

    public boolean isValid(Coupon coupon, Object orderAmount) {
        if (!coupon.isActive() || (coupon.getExpirationDate() != null && ((LocalDate) coupon.getExpirationDate()).isBefore(LocalDate.now()))) {
            return false; // Cupom inativo ou expirado
        }
        if (coupon.getMinOrderAmount() != null && ((BigDecimal) orderAmount).compareTo((BigDecimal) coupon.getMinOrderAmount()) < 0) {
            return false; // Valor mínimo do pedido não atingido
        }
        return true;
    }
}
