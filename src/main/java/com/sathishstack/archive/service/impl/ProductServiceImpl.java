package com.sathishstack.archive.service.impl;


import com.sathishstack.archive.dto.request.CreateProductRequest;
import com.sathishstack.archive.dto.response.ProductResponse;
import com.sathishstack.archive.entity.Product;
import com.sathishstack.archive.mapper.ProductMapper;
import com.sathishstack.archive.repository.ProductRepository;
import com.sathishstack.archive.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    public ProductServiceImpl(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public ProductResponse create(CreateProductRequest request) {
        Product product = ProductMapper.toEntity(request);
        Product saved = repository.save(product);
        return ProductMapper.toResponse(saved);
    }
}

