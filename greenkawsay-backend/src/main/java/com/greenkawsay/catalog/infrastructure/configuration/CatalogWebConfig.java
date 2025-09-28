package com.greenkawsay.catalog.infrastructure.configuration;

import com.greenkawsay.catalog.infrastructure.adapters.in.web.mappers.ProductMapper;
import com.greenkawsay.catalog.infrastructure.adapters.in.web.mappers.CategoryMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web Configuration for Catalog Context
 * Configures CORS, serialization, and web layer components
 */
@Configuration
public class CatalogWebConfig {

    @Bean
    public ProductMapper productMapper() {
        return ProductMapper.INSTANCE;
    }

    @Bean
    public CategoryMapper categoryMapper() {
        return CategoryMapper.INSTANCE;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/catalog/**")
                        .allowedOrigins("http://localhost:4200", "https://greenkawsay.com")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true)
                        .maxAge(3600);
                
                registry.addMapping("/api/categories/**")
                        .allowedOrigins("http://localhost:4200", "https://greenkawsay.com")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true)
                        .maxAge(3600);
                
                registry.addMapping("/api/products/**")
                        .allowedOrigins("http://localhost:4200", "https://greenkawsay.com")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true)
                        .maxAge(3600);
            }
        };
    }
}