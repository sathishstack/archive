package com.sathishstack.archive.mapper;

import com.sathishstack.archive.dto.request.CreateProductRequest;
import com.sathishstack.archive.dto.response.ProductResponse;
import com.sathishstack.archive.entity.Product;

public class ProductMapper {

    public static Product toEntity(CreateProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        return product;
    }

    public static ProductResponse toResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setPrice(product.getPrice());
        response.setStock(product.getStock());
        return response;
    }
}

