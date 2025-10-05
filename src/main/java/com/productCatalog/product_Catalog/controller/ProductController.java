package com.productCatalog.product_Catalog.controller;

import com.productCatalog.product_Catalog.dto.RequestDto;
import com.productCatalog.product_Catalog.dto.ResponseDto;
import com.productCatalog.product_Catalog.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // Create
    @PostMapping
    public ResponseDto create(@Valid @RequestBody RequestDto requestDto) {
        return productService.createProduct(requestDto);
    }

    // Read by id
    @GetMapping("/{id}")
    public ResponseDto getById(@PathVariable Long id) {
        return productService.getById(id);
    }

    // Get all with pagination & sorting (uses Spring's Pageable)
    // Default: page=0, size=5, sort by id asc
    @GetMapping
    public Page<ResponseDto> getAll(
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return productService.getAll(pageable);
    }

    // Update
    @PutMapping("/{id}")
    public ResponseDto update(@PathVariable Long id, @Valid @RequestBody RequestDto requestDto) {
        return productService.update(id, requestDto);
    }

    // Delete
    @DeleteMapping("/{id}")
    public Map<String, String> delete(@PathVariable Long id) {
        productService.delete(id);
        return Map.of("status", "deleted", "id", id.toString());
    }

    @GetMapping("/search")
    public Page<ResponseDto> search(
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String name,
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        if (minPrice != null && maxPrice != null) {
            return productService.searchByPrice(minPrice, maxPrice, pageable);
        } else if (name != null && !name.isBlank()) {
            return productService.searchByName(name, pageable);
        } else {
            throw new IllegalArgumentException("Provide minPrice & maxPrice OR name parameter for search");
        }
    }
}
