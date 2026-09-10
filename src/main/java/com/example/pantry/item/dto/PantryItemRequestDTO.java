package com.example.pantry.item.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.example.pantry.item.entity.Unit;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PantryItemRequestDTO {
    @NotBlank(message = "Item name is required")
    private String name;

    @NotNull(message = "Item quantity is required")
@DecimalMin(value = "0.0", inclusive = false, message = "Quantity must be greater than 0")
private BigDecimal quantity;

    @NotNull(message = "Item unit is required")
    private Unit unit;

    private LocalDate expiryDate;
    private String barcode;
    private String imageUrl;
    private Long categoryId;
    private Long storageLocationId;
}
