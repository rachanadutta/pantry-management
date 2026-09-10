package com.example.pantry.storage.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pantry.storage.dto.StorageLocationRequestDTO;
import com.example.pantry.storage.dto.StorageLocationResponseDTO;
import com.example.pantry.storage.service.StorageLocationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/storage-locations")
public class StorageLocationController {

    private final StorageLocationService storageLocationService;

    public StorageLocationController(StorageLocationService storageLocationService) {
        this.storageLocationService = storageLocationService;
    }

    @GetMapping
    public List<StorageLocationResponseDTO> getAllStorageLocations() {
        return storageLocationService.getAllStorageLocations();
    }

    @PostMapping
    public StorageLocationResponseDTO createStorageLocation(
            @Valid @RequestBody StorageLocationRequestDTO requestDTO) {

        return storageLocationService.createStorageLocation(requestDTO);
    }
}