package com.example.productcatalogservice.controller;

import com.example.productcatalogservice.dtos.ProductPriceDTO;
import com.example.productcatalogservice.dtos.ProductRequestDTO;
import com.example.productcatalogservice.dtos.ProductResponseDTO;
import com.example.productcatalogservice.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProduct(@PathVariable Long id) {
        ProductResponseDTO productResponseDTO = productService.getProductById(id);
        if (productResponseDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productResponseDTO);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponseDTO>> searchProducts(@RequestParam String keyword) {
        List<ProductResponseDTO> productResponseDTOs = productService.searchProducts(keyword);
        return ResponseEntity.ok(productResponseDTOs);
    }

    @GetMapping("/category")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByCategory(@RequestParam String category,
                                                                          @RequestParam int page,
                                                                          @RequestParam int size) {
        List<ProductResponseDTO> productResponseDTOs = productService.getProductsByCategory(category, page, size);
        return ResponseEntity.ok(productResponseDTOs);
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody ProductRequestDTO productRequestDTO) {
        ProductResponseDTO productResponseDTO = productService.saveProduct(productRequestDTO);
        return ResponseEntity.ok(productResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long id, @RequestBody ProductRequestDTO productRequestDTO) {
        ProductRequestDTO updatedProductRequestDTO = productRequestDTO;
        updatedProductRequestDTO.setId(id);
        if (productService.getProductById(id) == null){
            return ResponseEntity.notFound().build();
        }
        ProductResponseDTO productResponseDTO = productService.saveProduct(updatedProductRequestDTO);
        return ResponseEntity.ok(productResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/price/{productId}")
    public ResponseEntity<ProductPriceDTO> getProductPrice(@PathVariable Long productId) {
        System.out.println("*******************************");
        System.out.println(productService.getProductById(productId));
        System.out.println("*******************************");
        ProductPriceDTO productPriceDTO = productService.getProductPriceById(productId);
        return ResponseEntity.ok(productPriceDTO);
    }
}
