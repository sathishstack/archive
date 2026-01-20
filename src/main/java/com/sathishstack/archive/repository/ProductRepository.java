package com.sathishstack.archive.repository;

import com.sathishstack.archive.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {
}

