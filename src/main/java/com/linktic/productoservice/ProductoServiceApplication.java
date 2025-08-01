package com.linktic.productoservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ProductoServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductoServiceApplication.class, args);
    }
}