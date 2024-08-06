package com.example.cartservice.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class Product {

    private String id;
    private String name;
    private String description;
    private List<String> categories;
    private List<String> images;
    private String specifications;
    private double price;
}