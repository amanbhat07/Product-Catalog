package com.productCatalog.product_Catalog.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {

    @NotBlank(message = "Product name cannot be blank")
    private String name;

    private String description;

    @NotNull
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private Double price;

    @NotNull
    @Min(value = 0, message = "Stock quantity cannot be negative")
    private Integer stockQuantity;

    private String createdBy;
    private String updatedBy;
}