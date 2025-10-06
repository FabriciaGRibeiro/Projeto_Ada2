package com.example.securityapp.controller;

import com.example.securityapp.dto.OrderCreationDTO;
import com.example.securityapp.dto.OrderItemDTO;
import com.example.securityapp.dto.PaymentRequestDTO;
import com.example.securityapp.model.Order;
import com.example.securityapp.model.OrderItem;
import com.example.securityapp.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")

public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()") // Qualquer utilizador autenticado pode criar um pedido
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderCreationDTO orderCreationDTO) {
        try {
            Order newOrder = orderService.createOrder(orderCreationDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar pedido: " + e.getMessage());
        }
    }

    @GetMapping
    @PreAuthorize("hasRole(\'ADMIN\')") // Apenas administradores podem listar todos os pedidos
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.findAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole(\'ADMIN\') or (isAuthenticated() and @orderService.findOrderById(#id).getClient().getEmail() == authentication.principal.username)") // Admin ou o próprio cliente do pedido
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        try {
            Order order = orderService.findOrderById(id);
            return ResponseEntity.ok(order);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/{orderId}/items")
    @PreAuthorize("isAuthenticated() and @orderService.findOrderById(#orderId).getClient().getEmail() == authentication.principal.username") // Apenas o cliente do pedido pode adicionar itens
    public ResponseEntity<?> addItemToOrder(@PathVariable Long orderId, @Valid @RequestBody OrderItemDTO itemDTO) {
        try {
            OrderItem orderItem = orderService.addItemToOrder(orderId, itemDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(orderItem);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao adicionar item ao pedido: " + e.getMessage());
        }
    }

    @DeleteMapping("/{orderId}/items/{orderItemId}")
    @PreAuthorize("isAuthenticated() and @orderService.findOrderById(#orderId).getClient().getEmail() == authentication.principal.username") // Apenas o cliente do pedido pode remover itens
    public ResponseEntity<?> removeItemFromOrder(@PathVariable Long orderId, @PathVariable Long orderItemId) {
        try {
            orderService.removeItemFromOrder(orderId, orderItemId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao remover item do pedido: " + e.getMessage());
        }
    }

    @PutMapping("/{orderId}/items/{orderItemId}")
    @PreAuthorize("isAuthenticated() and @orderService.findOrderById(#orderId).getClient().getEmail() == authentication.principal.username") // Apenas o cliente do pedido pode alterar a quantidade
    public ResponseEntity<?> updateItemQuantity(@PathVariable Long orderId, @PathVariable Long orderItemId, @RequestParam Integer quantity) {
        try {
            OrderItem updatedItem = orderService.updateItemQuantity(orderId, orderItemId, quantity);
            return ResponseEntity.ok(updatedItem);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar quantidade do item: " + e.getMessage());
        }
    }

    @PostMapping("/{orderId}/finalize")
    @PreAuthorize("isAuthenticated() and @orderService.findOrderById(#orderId).getClient().getEmail() == authentication.principal.username") // Apenas o cliente do pedido pode finalizar
    public ResponseEntity<?> finalizeOrder(@PathVariable Long orderId) {
        try {
            Order finalizedOrder = orderService.finalizeOrder(orderId);
            return ResponseEntity.ok(finalizedOrder);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao finalizar pedido: " + e.getMessage());
        }
    }

    @PostMapping("/{orderId}/apply-coupon")
    @PreAuthorize("isAuthenticated() and @orderService.findOrderById(#orderId).getClient().getEmail() == authentication.principal.username") // Apenas o cliente do pedido pode aplicar cupom
    public ResponseEntity<?> applyCouponToOrder(@PathVariable Long orderId, @RequestParam String couponCode) {
        try {
            Order orderWithCoupon = orderService.applyCouponToOrder(orderId, couponCode);
            return ResponseEntity.ok(orderWithCoupon);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao aplicar cupom ao pedido: " + e.getMessage());
        }
    }

    @PostMapping("/{orderId}/pay")
    @PreAuthorize("isAuthenticated() and @orderService.findOrderById(#orderId).getClient().getEmail() == authentication.principal.username") // Apenas o cliente do pedido pode pagar
    public ResponseEntity<?> processOrderPayment(@PathVariable Long orderId, @Valid @RequestBody PaymentRequestDTO paymentRequestDTO) {
        try {
    Order paidOrder = orderService.processOrderPayment(orderId, paymentRequestDTO);
    return ResponseEntity.ok(paidOrder);
} catch (RuntimeException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
} catch (Exception e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao processar pagamento do pedido: " + e.getMessage());
}

    }

    @PostMapping("/{orderId}/deliver")
    @PreAuthorize("hasRole(\'ADMIN\')") // Apenas administradores podem marcar como entregue
    public ResponseEntity<?> deliverOrder(@PathVariable Long orderId) {
        try {
            Order deliveredOrder = orderService.deliverOrder(orderId);
            return ResponseEntity.ok(deliveredOrder);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao marcar pedido como entregue: " + e.getMessage());
        }
    }
}

