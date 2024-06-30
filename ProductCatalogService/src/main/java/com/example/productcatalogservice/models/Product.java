package com.example.productcatalogservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name = "products")
@Document(indexName = "products") // Ensure this is lowercase
public class Product implements Serializable {
    @Id
    private String id;
    private String name;
    private String description;
    private List<String> categories;
    private List<String> images;
    private String specifications;
}