package com.example.productcatalogservice.dtos;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class ProductResponseDTO implements Serializable {
    private String id;
    private String name;
    private String description;
    private List<String> categories;
    private List<String> images;
    private String specifications;
}