package com.example.productcatalogservice.repository.elastic_search;

import com.example.productcatalogservice.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


import java.util.List;

public interface ProductElasticsearchRepository extends ElasticsearchRepository<Product, String>{
    List<Product> findByNameContaining(String name);
    Page<Product> findByCategoriesContaining(String category, Pageable pageable);

}
