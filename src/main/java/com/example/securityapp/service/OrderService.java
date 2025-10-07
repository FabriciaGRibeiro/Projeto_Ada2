package com.example.securityapp.service;

import com.example.securityapp.dto.OrderCreationDTO;
import com.example.securityapp.dto.OrderItemDTO;
import com.example.securityapp.dto.PaymentRequestDTO;
import com.example.securityapp.model.Coupon;
import com.example.securityapp.model.Order;
import com.example.securityapp.model.OrderItem;
import com.example.securityapp.model.Product;
import com.example.securityapp.model.User;
import com.example.securityapp.repository.OrderItemRepository;
import com.example.securityapp.repository.OrderRepository;
import com.example.securityapp.repository.ProductRepository;
import com.example.securityapp.repository.UserRepository;

import com.example.securityapp.model.Transaction;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final EmailService emailService;
    private final CouponService couponService;
    private final PaymentService paymentService;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository,
            ProductRepository productRepository, OrderItemRepository orderItemRepository, EmailService emailService,
            CouponService couponService, PaymentService paymentService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
        this.emailService = emailService;
        this.couponService = couponService;
        this.paymentService = paymentService;
    }

    @Transactional
    public Order createOrder(OrderCreationDTO orderCreationDTO) {
        User client = userRepository.findById(extracted(orderCreationDTO.getClientId))
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado."));

        Order order = new Order();
        order.setClient(client);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.OrderStatus.OPEN);
        order.setPaymentStatus(Order.PaymentStatus.PENDING);
        order.setDeliveryStatus(Order.DeliveryStatus.PENDING);
        order.setTotalAmount(BigDecimal.ZERO);
        order.setDiscountAmount(BigDecimal.ZERO);

        return orderRepository.save(order);
    }

    private Long extracted(OrderCreationDTO orderCreationDTO) {
        return orderCreationDTO.getClientId();
    }

    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    public Order findOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado."));
    }

    /**
     * @param orderId
     * @param itemDTO
     * @return
     */
    @Transactional
    public OrderItem addItemToOrder(Long orderId, OrderItemDTO itemDTO) {
        Order order = findOrderById(orderId);
        if (order.getStatus() != Order.OrderStatus.OPEN) {
            throw new IllegalStateException("Não é possível adicionar itens a um pedido que não esteja ABERTO.");
        }

        Product product = productRepository.findById(itemDTO.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado."));

        if (!product.isActive()) {
            throw new IllegalStateException("Não é possível adicionar um produto inativo ao pedido.");
        }

        // Verificar se o item já existe no pedido
        Optional<OrderItem> existingItem = order.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(itemDTO.getProductId()))
                .findFirst();

        OrderItem orderItem;
        if (existingItem.isPresent()) {
            orderItem = existingItem.get();
            orderItem.setQuantity(orderItem.getQuantity() + itemDTO.getQuantity());
            orderItem.setUnitPrice(itemDTO.getUnitPrice()); // Atualiza o preço de venda se necessário
        } else {
            orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setUnitPrice(itemDTO.getUnitPrice());
            // Ensure getItems() returns List<OrderItem> in Order class
            order.getItems().add(orderItem);
        }

        updateOrderTotal(order);
        orderRepository.save(order);
        return orderItemRepository.save(orderItem);
    }

    private Long extracted2(OrderItemDTO itemDTO) {
        return itemDTO.getProductId();
    }

    @Transactional
    public void removeItemFromOrder(Long orderId, Long orderItemId) {
        Order order = findOrderById(orderId);
        if (order.getStatus() != Order.OrderStatus.OPEN) {
            throw new IllegalStateException("Não é possível remover itens de um pedido que não esteja ABERTO.");
        }

        OrderItem itemToRemove = order.getItems().stream()
                .filter(item -> item.getId().equals(orderItemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Item do pedido não encontrado."));

        order.getItems().remove(itemToRemove);
        orderItemRepository.delete(itemToRemove);
        updateOrderTotal(order);
        orderRepository.save(order);

    }

    @Transactional
    public OrderItem updateItemQuantity(Long orderId, Long orderItemId, Integer newQuantity) {
        if (newQuantity <= 0) {
            throw new IllegalArgumentException(
                    "A quantidade deve ser no mínimo 1. Para remover, use a função de remover item.");
        }

        Order order = findOrderById(orderId);
        if (order.getStatus() != Order.OrderStatus.OPEN) {
            throw new IllegalStateException(
                    "Não é possível alterar a quantidade de itens de um pedido que não esteja ABERTO.");
        }

        OrderItem itemToUpdate = order.getItems().stream().filter(item -> item.getId().equals(orderItemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Item do pedido não encontrado."));

        itemToUpdate.setQuantity(newQuantity);
        updateOrderTotal(order);
        orderRepository.save(order);
        return orderItemRepository.save(itemToUpdate);
    }

    @Transactional
    public Order finalizeOrder(Long orderId) {
        Order order = findOrderById(orderId);

        if (order.getItems().isEmpty()) {
            throw new IllegalStateException("O pedido deve ter ao menos um item para ser finalizado.");
        }
        if (((BigDecimal) order.getTotalAmount()).compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("O valor total do pedido deve ser maior que zero para ser finalizado.");
        }
        if (order.getStatus() != Order.OrderStatus.OPEN) {
            throw new IllegalStateException("O pedido já foi finalizado ou está em outro status.");
        }

        order.setStatus(Order.OrderStatus.PENDING_PAYMENT);
        order.setPaymentStatus(Order.PaymentStatus.PENDING);
        orderRepository.save(order);

        // Notificar o cliente via e-mail
        String subject = "Seu pedido foi finalizado e aguarda pagamento!";
        String body = String.format(
                "Olá %s,\n\nSeu pedido #%d foi finalizado com sucesso e está aguardando pagamento. O valor total é de %.2f.\n\nObrigado!",
                (order.getClient()), order.getName(), order.getId(), order.getTotalAmount());
        emailService.sendEmail(order.getClient().getEmail(), subject, body);

        return order;
    }

    // Método auxiliar para recalcular o total do pedido
    @Transactional
    public Order applyCouponToOrder(Long orderId, String couponCode) {
        Order order = findOrderById(orderId);
        if (order.getStatus() != Order.OrderStatus.OPEN) {
            throw new IllegalStateException("Não é possível aplicar cupom a um pedido que não esteja ABERTO.");
        }

        Coupon coupon = couponService.findCouponByCode(couponCode);
        if (!couponService.isValid(coupon, order.getTotalAmount())) {
            throw new IllegalArgumentException("Cupom inválido ou não aplicável a este pedido.");
        }

        // Remover qualquer desconto anterior do cupom para aplicar o novo
        order.setDiscountAmount(BigDecimal.ZERO);
        updateOrderTotal(order);

        BigDecimal discount = BigDecimal.ZERO;
        if (coupon.getDiscountType() == Coupon.DiscountType.PERCENTAGE) {
            BigDecimal discountPercentage = coupon.getDiscountValue();
            BigDecimal discount = order.getTotalAmount()
                    .multiply(discountPercentage)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            order.setDiscountAmount(discount);
        } else if (coupon.getDiscountType() == Coupon.DiscountType.FIXED) {
    order.setDiscountAmount(coupon.getDiscountValue());
        }

        order.setDiscountAmount(discount);
        updateOrderTotal(order);
        return orderRepository.save(order);
    }

    @Transactional
    public Order processOrderPayment(Long orderId, PaymentRequestDTO paymentRequestDTO) {
        Order order = findOrderById(orderId);

        if (order.getStatus() != Order.OrderStatus.PENDING_PAYMENT
                || order.getPaymentStatus() != Order.PaymentStatus.PENDING) {
            throw new IllegalStateException("O pedido não está no status 'Aguardando pagamento' para ser pago.");
        }

        // O PaymentService agora aceita o Order diretamente
        Transaction transaction = (Transaction) paymentService.processPayment(order, paymentRequestDTO);

        if ("APPROVED".equals(transaction.getStatus())) {
            order.setPaymentStatus(Order.PaymentStatus.APPROVED);
            order.setStatus(Order.OrderStatus.PAID);
            orderRepository.save(order);

            // Notificar o cliente sobre o pagamento
            String subject = "Pagamento do seu pedido foi confirmado!";
            String body = String.format(
                    "Olá %s,\n\nO pagamento do seu pedido #%d no valor de %.2f foi confirmado com sucesso!\n\nObrigado!",
                    order.getClient(), order.getName(), order.getId(), order.getTotalAmount());
            emailService.sendEmail(order.getClient().getEmail(), subject, body);
        } else {
            order.setPaymentStatus(Order.PaymentStatus.DECLINED);
            orderRepository.save(order);
            throw new RuntimeException("Pagamento recusado para o pedido #" + orderId);
        }
        return order;
    }

    @Transactional
    public Order deliverOrder(Long orderId) {
        Order order = findOrderById(orderId);

        if (order.getStatus() != Order.OrderStatus.PAID) {
            throw new IllegalStateException("O pedido não está pago para ser entregue.");
        }

        order.setDeliveryStatus(Order.DeliveryStatus.DELIVERED);
        order.setStatus(Order.OrderStatus.DELIVERED);
        orderRepository.save(order);

        // Notificar o cliente sobre a entrega
        String subject = "Seu pedido foi entregue!";
        String body = String.format("Olá %s,\n\nSeu pedido #%d foi entregue com sucesso!\n\nObrigado!",
                (order.getClient()), order.getName(), order.getId());
        emailService.sendEmail(order.getClient().getEmail(), subject, body);

        return order;
    }

    // Método auxiliar para recalcular o total do pedido
    private void updateOrderTotal(Order order) {
        BigDecimal total = order.getItems().stream()
                .map(item -> ((BigDecimal) item.getUnitPrice()).multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

    }
}
