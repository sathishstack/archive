package com.sathishstack.archive.service;


import com.sathishstack.archive.dto.request.CreateOrderRequest;
import com.sathishstack.archive.dto.response.OrderResponse;

public interface OrderService {
    OrderResponse createOrder(CreateOrderRequest request, String idempotencyKey);
}

