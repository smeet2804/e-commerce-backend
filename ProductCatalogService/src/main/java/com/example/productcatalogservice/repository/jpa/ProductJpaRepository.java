package com.example.productcatalogservice.repository.jpa;


import com.example.productcatalogservice.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<Product, Long> {
}