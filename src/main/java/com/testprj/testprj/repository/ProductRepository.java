package com.testprj.testprj.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.testprj.testprj.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
