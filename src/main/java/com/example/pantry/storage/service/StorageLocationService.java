package com.example.pantry.storage.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.pantry.exception.ResourceConflictException;
import com.example.pantry.exception.ResourceNotFoundException;
import com.example.pantry.storage.dto.StorageLocationRequestDTO;
import com.example.pantry.storage.dto.StorageLocationResponseDTO;
import com.example.pantry.storage.entity.StorageLocation;
import com.example.pantry.storage.entity.StorageLocationType;
import com.example.pantry.storage.repository.StorageLocationRepository;
import com.example.pantry.user.entity.User;
import com.example.pantry.user.repository.UserRepository;

@Service 
public class StorageLocationService {
    private final StorageLocationRepository storageLocationRepository;
    private final UserRepository userRepository;
    public StorageLocationService(StorageLocationRepository storageLocationRepository, UserRepository userRepository) {
        this.storageLocationRepository = storageLocationRepository;
        this.userRepository = userRepository;
    }

    public List<StorageLocationResponseDTO> getAllStorageLocations(){
        Authentication authentication =
        SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        List<StorageLocation> storageLocations= storageLocationRepository.findByTypeOrUser(StorageLocationType.PREDEFINED, user);
        List<StorageLocationResponseDTO> responseDTOs= new ArrayList<>();
        for(StorageLocation location:storageLocations){
            StorageLocationResponseDTO response= new StorageLocationResponseDTO();
            response.setId(location.getId());
            response.setName(location.getName());
            response.setType(location.getType());

            responseDTOs.add(response);
        }
        return responseDTOs;
    }
    public StorageLocationResponseDTO createStorageLocation(StorageLocationRequestDTO requestDTO){
        Authentication authentication =
        SecurityContextHolder.getContext().getAuthentication();
        String email= authentication.getName();
        User user= userRepository.findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (storageLocationRepository.existsByNameAndUser(requestDTO.getName(), user)) {
    throw new ResourceConflictException(
        "Storage location already exists");
}

if (storageLocationRepository.existsByNameAndType(
        requestDTO.getName(),
        StorageLocationType.PREDEFINED)) {
    throw new ResourceConflictException(
        "Storage location already exists as a predefined location");
}
        StorageLocation location= new StorageLocation();
        location.setName(requestDTO.getName());
        location.setType(StorageLocationType.CUSTOM);
        location.setUser(user);
        StorageLocation savedLocation= storageLocationRepository.save(location);
        StorageLocationResponseDTO responseDTO= new StorageLocationResponseDTO();
        responseDTO.setId(savedLocation.getId());
        responseDTO.setName(savedLocation.getName());
        responseDTO.setType(savedLocation.getType());
        return responseDTO;
    }
}
