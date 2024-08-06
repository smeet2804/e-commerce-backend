package com.example.productcatalogservice.controller;

import com.example.productcatalogservice.dtos.ProductPriceDTO;
import com.example.productcatalogservice.dtos.ProductRequestDTO;
import com.example.productcatalogservice.dtos.ProductResponseDTO;
import com.example.productcatalogservice.services.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
//    @WithMockUser(roles = "ADMIN")
    public void getProduct_WhenProductExists_ReturnsProduct() throws Exception {
        // Arrange
        ProductResponseDTO product = new ProductResponseDTO();
        product.setId(1L);
        product.setName("Product 1");
        product.setDescription("Description of Product 1");
        product.setPrice(100.0);

        when(productService.getProductById(1L)).thenReturn(product);

        // Act and Assert
        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(product)));

        // Verify
        verify(productService).getProductById(1L);
    }

    @Test
//    @WithMockUser(roles = "ADMIN")
    public void searchProducts_WhenCalled_ReturnsListOfProducts() throws Exception {
        // Arrange
        List<ProductResponseDTO> productList = new ArrayList<>();
        ProductResponseDTO product1 = new ProductResponseDTO();
        product1.setId(1L);
        product1.setName("Product 1");
        product1.setDescription("Description of Product 1");
        product1.setPrice(100.0);

        ProductResponseDTO product2 = new ProductResponseDTO();
        product2.setId(2L);
        product2.setName("Product 2");
        product2.setDescription("Description of Product 2");
        product2.setPrice(200.0);

        productList.add(product1);
        productList.add(product2);

        when(productService.searchProducts("Product")).thenReturn(productList);

        // Act and Assert
        mockMvc.perform(get("/products/search")
                        .param("keyword", "Product"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(productList)));

        // Verify
        verify(productService).searchProducts("Product");
    }

    @Test
//    @WithMockUser(roles = "ADMIN")
    public void getProductsByCategory_WhenCalled_ReturnsListOfProducts() throws Exception {
        // Arrange
        List<ProductResponseDTO> productList = new ArrayList<>();
        ProductResponseDTO product1 = new ProductResponseDTO();
        product1.setId(1L);
        product1.setName("Product 1");
        product1.setDescription("Description of Product 1");
        product1.setPrice(100.0);

        ProductResponseDTO product2 = new ProductResponseDTO();
        product2.setId(2L);
        product2.setName("Product 2");
        product2.setDescription("Description of Product 2");
        product2.setPrice(200.0);

        productList.add(product1);
        productList.add(product2);

        when(productService.getProductsByCategory("Electronics", 0, 10)).thenReturn(productList);

        // Act and Assert
        mockMvc.perform(get("/products/category")
                        .param("category", "Electronics")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(productList)));

        // Verify
        verify(productService).getProductsByCategory("Electronics", 0, 10);
    }

    @Test
//    @WithMockUser(roles = "ADMIN")
    public void createProduct_WhenValidRequest_ReturnsCreatedProduct() throws Exception {
        // Arrange
        ProductRequestDTO productToCreate = new ProductRequestDTO();
        productToCreate.setName("Product 1");
        productToCreate.setDescription("Description of Product 1");
        productToCreate.setPrice(100.0);

        ProductResponseDTO createdProduct = new ProductResponseDTO();
        createdProduct.setId(1L);
        createdProduct.setName("Product 1");
        createdProduct.setDescription("Description of Product 1");
        createdProduct.setPrice(100.0);

        when(productService.saveProduct(any(ProductRequestDTO.class))).thenReturn(createdProduct);

        // Act and Assert
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productToCreate)))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(createdProduct)));

        // Verify
        verify(productService).saveProduct(any(ProductRequestDTO.class));
    }

    @Test
//    @WithMockUser(roles = "ADMIN")
    public void updateProduct_WhenProductExists_ReturnsUpdatedProduct() throws Exception {
        // Arrange
        ProductRequestDTO productToUpdate = new ProductRequestDTO();
        productToUpdate.setName("Updated Product");
        productToUpdate.setDescription("Updated Description");
        productToUpdate.setPrice(150.0);

        ProductResponseDTO updatedProduct = new ProductResponseDTO();
        updatedProduct.setId(1L);
        updatedProduct.setName("Updated Product");
        updatedProduct.setDescription("Updated Description");
        updatedProduct.setPrice(150.0);

        when(productService.saveProduct(any(ProductRequestDTO.class))).thenReturn(updatedProduct);

        // Act and Assert
        mockMvc.perform(put("/products/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productToUpdate)))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(updatedProduct)));

        // Verify
        verify(productService).saveProduct(any(ProductRequestDTO.class));
    }

    @Test
//    @WithMockUser(roles = "ADMIN")
    public void deleteProduct_WhenProductExists_ReturnsStatusNoContent() throws Exception {
        // Act and Assert
        mockMvc.perform(delete("/products/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        // Verify
        verify(productService).deleteProduct(1L);
    }

    @Test
//    @WithMockUser(roles = "ADMIN")
    public void getProductPrice_WhenProductExists_ReturnsProductPrice() throws Exception {
        // Arrange
        ProductPriceDTO productPrice = new ProductPriceDTO();
        productPrice.setProductId(1L);
        productPrice.setPrice(100.0);

        when(productService.getProductPriceById(1L)).thenReturn(productPrice);

        // Act and Assert
        mockMvc.perform(get("/products/price/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(productPrice)));

        // Verify
        verify(productService).getProductPriceById(1L);
    }
}
