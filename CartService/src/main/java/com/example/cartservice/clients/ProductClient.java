package com.example.cartservice.clients;

import com.example.cartservice.config.FeignConfig;
import com.example.cartservice.dtos.ProductPriceDTO;
import com.example.cartservice.models.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ProductCatalogService", configuration = FeignConfig.class)
public interface ProductClient {

    @GetMapping("/product/price/{productId}")
    ProductPriceDTO getProductPrice(@PathVariable String productId);

    @GetMapping("/products/{productId}")
    Product getProductDetails(@PathVariable String productId);
}