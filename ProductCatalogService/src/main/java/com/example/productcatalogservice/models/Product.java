package com.example.productcatalogservice.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "products")
@Document(indexName = "products") // Ensure this is lowercase
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String description;

    @ElementCollection
    private List<String> categories;

    @ElementCollection
    private List<String> images;

    private String specifications;
    private double price;

}