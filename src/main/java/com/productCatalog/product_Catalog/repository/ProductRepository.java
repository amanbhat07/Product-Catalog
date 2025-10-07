package com.productCatalog.product_Catalog.repository;

import com.productCatalog.product_Catalog.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    // Pagination
    Page<ProductEntity> findByPriceBetween(Double minPrice, Double maxPrice, Pageable pageable);

    Page<ProductEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
