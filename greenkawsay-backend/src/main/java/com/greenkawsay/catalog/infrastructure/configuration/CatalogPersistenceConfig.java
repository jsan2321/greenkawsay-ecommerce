package com.greenkawsay.catalog.infrastructure.configuration;

import com.greenkawsay.catalog.infrastructure.adapters.out.persistence.ProductPersistenceAdapter;
import com.greenkawsay.catalog.infrastructure.adapters.out.persistence.CategoryPersistenceAdapter;
import com.greenkawsay.catalog.infrastructure.adapters.out.persistence.mappers.ProductPersistenceMapper;
import com.greenkawsay.catalog.infrastructure.adapters.out.persistence.mappers.CategoryPersistenceMapper;
import com.greenkawsay.catalog.infrastructure.adapters.out.persistence.repositories.ProductJpaRepository;
import com.greenkawsay.catalog.infrastructure.adapters.out.persistence.repositories.CategoryJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Persistence Configuration for Catalog Context
 * Configures JPA repositories and persistence adapters
 */
@Configuration
public class CatalogPersistenceConfig {

    @Bean
    public ProductPersistenceMapper productPersistenceMapper() {
        return ProductPersistenceMapper.INSTANCE;
    }

    @Bean
    public CategoryPersistenceMapper categoryPersistenceMapper() {
        return CategoryPersistenceMapper.INSTANCE;
    }

    @Bean
    public ProductPersistenceAdapter productPersistenceAdapter(
            ProductJpaRepository productJpaRepository,
            ProductPersistenceMapper productPersistenceMapper) {
        return new ProductPersistenceAdapter(productJpaRepository, productPersistenceMapper);
    }

    @Bean
    public CategoryPersistenceAdapter categoryPersistenceAdapter(
            CategoryJpaRepository categoryJpaRepository,
            CategoryPersistenceMapper categoryPersistenceMapper) {
        return new CategoryPersistenceAdapter(categoryJpaRepository, categoryPersistenceMapper);
    }
}