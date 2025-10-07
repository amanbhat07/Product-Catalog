package com.productCatalog.product_Catalog.service;

import com.productCatalog.product_Catalog.dto.RequestDto;
import com.productCatalog.product_Catalog.dto.ResponseDto;
import com.productCatalog.product_Catalog.entity.ProductEntity;
import com.productCatalog.product_Catalog.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private ResponseDto toDto(ProductEntity p) {
        return ResponseDto.builder()
                .id(p.getId())
                .name(p.getName())
                .description(p.getDescription())
                .price(p.getPrice())
                .stockQuantity(p.getStockQuantity())
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .createdBy(p.getCreatedBy())
                .updatedBy(p.getUpdatedBy())
                .build();
    }

    private void apply(RequestDto r, ProductEntity p, boolean creating) {
        p.setName(r.getName());
        p.setDescription(r.getDescription());
        p.setPrice(r.getPrice());
        p.setStockQuantity(r.getStockQuantity());
        if (creating) {
            p.setCreatedBy(r.getCreatedBy());
        }
        p.setUpdatedBy(r.getUpdatedBy());
    }

    public ResponseDto createProduct(RequestDto req) {
        ProductEntity p = ProductEntity.builder().build();
        apply(req, p, true);
        ProductEntity saved = productRepository.save(p);
        return toDto(saved);
    }

    public ResponseDto getById(Long id) {
        ProductEntity p = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        return toDto(p);
    }

    public Page<ResponseDto> getAll(Pageable pageable) {
        return productRepository.findAll(pageable).map(this::toDto);
    }

    public ResponseDto update(Long id, RequestDto req) {
        ProductEntity p = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        apply(req, p, false);
        ProductEntity updated = productRepository.save(p);
        return toDto(updated);
    }

    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        productRepository.deleteById(id);
    }

    public Page<ResponseDto> searchByPrice(Double minPrice, Double maxPrice, Pageable pageable) {
        return productRepository.findByPriceBetween(minPrice, maxPrice, pageable).map(this::toDto);
    }

    public Page<ResponseDto> searchByName(String name, Pageable pageable) {
        return productRepository.findByNameContainingIgnoreCase(name, pageable).map(this::toDto);
    }

}
