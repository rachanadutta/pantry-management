package com.example.pantry.storage.dto;

import com.example.pantry.storage.entity.StorageLocationType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
public class StorageLocationResponseDTO {
    private Long id;
    private String name;
    private StorageLocationType type;
}
