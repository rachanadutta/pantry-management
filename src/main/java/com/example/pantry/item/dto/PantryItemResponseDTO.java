package com.example.pantry.item.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.pantry.category.dto.CategoryResponseDTO;
import com.example.pantry.item.entity.Unit;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PantryItemResponseDTO {
    private Long id;
    private String name;
    private BigDecimal quantity;
    private Unit unit;
    private LocalDate expiryDate;
    private String barcode;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private CategoryResponseDTO category;
}
