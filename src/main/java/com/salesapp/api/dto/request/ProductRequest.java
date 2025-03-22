package com.salesapp.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequest {
    @NotBlank(message = "Product name is required")
    @Size(max = 100, message = "Product name cannot exceed 100 characters")
    private String productName;

    @Size(max = 255, message = "Brief description cannot exceed 255 characters")
    private String briefDescription;

    private String fullDescription;

    private String technicalSpecifications;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than zero")
    private BigDecimal price;

    private String imageURL;

    @NotNull(message = "Category ID is required")
    private Long categoryId;
}