package com.example.productcatalogservice.services;

import com.example.productcatalogservice.dtos.ProductRequestDTO;
import com.example.productcatalogservice.dtos.ProductResponseDTO;
import com.example.productcatalogservice.mapper.ProductMapper;
import com.example.productcatalogservice.models.Product;
import com.example.productcatalogservice.repository.elastic_search.ProductElasticsearchRepository;
import com.example.productcatalogservice.repository.jpa.ProductJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService implements ProductServiceI {

    @Autowired
    private ProductElasticsearchRepository productElasticsearchRepository;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Cacheable(value = "product", key = "#id")
    public ProductResponseDTO getProductById(String id) {
        Product product = productElasticsearchRepository.findById(id).orElse(null);
        if (product == null) {
            product = productJpaRepository.findById(id).orElse(null);
            if (product != null) {
                productElasticsearchRepository.save(product);
            }
        }
        return ProductMapper.getProductResponseDTOFromProduct(product);
    }

    @Cacheable(value = "product_search", key = "#keyword")
    public List<ProductResponseDTO> searchProducts(String keyword) {
        List<Product> products = productElasticsearchRepository.findByNameContaining(keyword);
        System.out.println(products);
        return ProductMapper.getProductResponseDTOListFromProducts(products);
    }

    public List<ProductResponseDTO> getProductsByCategory(String category, int page, int size) {
        List<Product> products = productElasticsearchRepository.findByCategoriesContaining(category, PageRequest.of(page, size)).getContent();
        return ProductMapper.getProductResponseDTOListFromProducts(products);
    }

    @CachePut(value = "product", key = "#result.id")
    public ProductResponseDTO saveProduct(ProductRequestDTO productRequestDTO) {
        Product product = ProductMapper.getProductFromRequestDTO(productRequestDTO);
        Product savedProduct = productJpaRepository.save(product);
        productElasticsearchRepository.save(savedProduct);
        return ProductMapper.getProductResponseDTOFromProduct(savedProduct);
    }

    @CacheEvict(value = "product", key = "#id")
    public void deleteProduct(String id) {
        productJpaRepository.deleteById(id);
        productElasticsearchRepository.deleteById(id);
    }
}
