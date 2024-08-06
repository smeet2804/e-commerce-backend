package com.example.productcatalogservice.services;

import com.example.productcatalogservice.dtos.ProductPriceDTO;
import com.example.productcatalogservice.dtos.ProductRequestDTO;
import com.example.productcatalogservice.dtos.ProductResponseDTO;

import java.util.List;

public interface ProductServiceI {

    ProductResponseDTO getProductById(Long id);

    List<ProductResponseDTO> searchProducts(String keyword);

    List<ProductResponseDTO> getProductsByCategory(String category, int page, int size);

    ProductResponseDTO saveProduct(ProductRequestDTO productRequestDTO);

    void deleteProduct(Long id);

    ProductPriceDTO getProductPriceById(Long productId);
}
