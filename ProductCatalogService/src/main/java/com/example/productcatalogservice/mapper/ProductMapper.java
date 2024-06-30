package com.example.productcatalogservice.mapper;

import com.example.productcatalogservice.dtos.ProductRequestDTO;
import com.example.productcatalogservice.dtos.ProductResponseDTO;
import com.example.productcatalogservice.models.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductMapper {

    public static Product getProductFromRequestDTO(ProductRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setCategories(dto.getCategories());
        product.setImages(dto.getImages());
        product.setSpecifications(dto.getSpecifications());
        return product;
    }

    public static ProductResponseDTO getProductResponseDTOFromProduct(Product product) {
        if (product == null) {
            return null;
        }

        ProductResponseDTO responseDTO = new ProductResponseDTO();
        responseDTO.setId(product.getId());
        responseDTO.setName(product.getName());
        responseDTO.setDescription(product.getDescription());
        responseDTO.setCategories(product.getCategories());
        responseDTO.setImages(product.getImages());
        responseDTO.setSpecifications(product.getSpecifications());
        return responseDTO;
    }

    public static List<ProductResponseDTO> getProductResponseDTOListFromProducts(List<Product> products) {
        List<ProductResponseDTO> responseDTOS = new ArrayList<>();
        for (Product product : products) {
            responseDTOS.add(getProductResponseDTOFromProduct(product));
        }
        return responseDTOS;
    }
}
