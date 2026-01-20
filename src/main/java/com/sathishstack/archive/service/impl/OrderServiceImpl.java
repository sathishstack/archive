package com.sathishstack.archive.service.impl;

import com.sathishstack.archive.constant.OrderStatus;
import com.sathishstack.archive.dto.request.CreateOrderRequest;
import com.sathishstack.archive.dto.request.OrderItemRequest;
import com.sathishstack.archive.dto.response.OrderItemResponse;
import com.sathishstack.archive.dto.response.OrderResponse;
import com.sathishstack.archive.entity.Order;
import com.sathishstack.archive.entity.OrderItem;
import com.sathishstack.archive.entity.Product;
import com.sathishstack.archive.entity.User;
import com.sathishstack.archive.repository.OrderRepository;
import com.sathishstack.archive.repository.ProductRepository;
import com.sathishstack.archive.repository.UserRepository;
import com.sathishstack.archive.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderServiceImpl(
            OrderRepository orderRepository,
            ProductRepository productRepository,
            UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public OrderResponse createOrder(CreateOrderRequest request, String idempotencyKey) {

        orderRepository.findByIdempotencyKey(idempotencyKey)
                .ifPresent(existing -> {
                    throw new IllegalStateException("Duplicate order request");
                });

        User customer = userRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        Order order = new Order();
        order.setCustomer(customer);
        order.setStatus(OrderStatus.CREATED);
        order.setIdempotencyKey(idempotencyKey);

        List<OrderItem> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found"));

            if (product.getStock() < itemRequest.getQuantity()) {
                throw new IllegalStateException("Insufficient stock");
            }

            product.setStock(product.getStock() - itemRequest.getQuantity());

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemRequest.getQuantity());
            item.setPrice(product.getPrice());

            items.add(item);
            total = total.add(product.getPrice()
                    .multiply(BigDecimal.valueOf(itemRequest.getQuantity())));
        }

        order.setItems(items);
        order.setTotalAmount(total);

        Order saved = orderRepository.save(order);

        return mapToResponse(saved);
    }

    private OrderResponse mapToResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getId());
        response.setStatus(order.getStatus());
        response.setTotalAmount(order.getTotalAmount());

        List<OrderItemResponse> itemResponses = new ArrayList<>();
        for (OrderItem item : order.getItems()) {
            OrderItemResponse r = new OrderItemResponse();
            r.setProductId(item.getProduct().getId());
            r.setQuantity(item.getQuantity());
            r.setPrice(item.getPrice());
            itemResponses.add(r);
        }

        response.setItems(itemResponses);
        return response;
    }
}