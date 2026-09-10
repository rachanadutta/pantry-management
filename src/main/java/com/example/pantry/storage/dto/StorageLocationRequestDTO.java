package com.example.pantry.storage.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
public class StorageLocationRequestDTO {
    @NotBlank (message = "Storage location name is required")
    @Size (max = 100, message = "Storage location name must not exceed 100 characters")
    private String name;
}
