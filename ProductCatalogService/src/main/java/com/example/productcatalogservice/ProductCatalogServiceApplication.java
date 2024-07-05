package com.example.productcatalogservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


//@EnableCaching
@EnableElasticsearchRepositories("com.example.productcatalogservice.repository.elastic_search")
@EnableJpaRepositories("com.example.productcatalogservice.repository.jpa")
@SpringBootApplication
public class ProductCatalogServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductCatalogServiceApplication.class, args);
	}

}
