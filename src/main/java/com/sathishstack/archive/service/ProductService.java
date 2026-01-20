package com.sathishstack.archive.service;


import com.sathishstack.archive.dto.request.CreateProductRequest;
import com.sathishstack.archive.dto.response.ProductResponse;

public interface ProductService {
    ProductResponse create(CreateProductRequest request);
}

