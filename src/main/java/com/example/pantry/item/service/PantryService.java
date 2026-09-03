package com.example.pantry.item.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.pantry.exception.ResourceNotFoundException;
import com.example.pantry.item.dto.PantryItemRequestDTO;
import com.example.pantry.item.dto.PantryItemResponseDTO;
import com.example.pantry.item.entity.PantryItem;
import com.example.pantry.item.repository.PantryRepository;
import com.example.pantry.user.entity.User;
import com.example.pantry.user.repository.UserRepository;

@Service

public class PantryService {
    private final PantryRepository pantryRepository;
    private final UserRepository userRepository;
    

    public PantryService(PantryRepository pantryRepository, UserRepository userRepository) {
        this.pantryRepository = pantryRepository;
        this.userRepository = userRepository;
    }

    public PantryItemResponseDTO addItem(PantryItemRequestDTO itemDTO) {

    Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

    String email = authentication.getName();

    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    PantryItem item = new PantryItem();

    item.setName(itemDTO.getName());
    item.setQuantity(itemDTO.getQuantity());
    item.setUnit(itemDTO.getUnit());
    item.setExpiryDate(itemDTO.getExpiryDate());
    item.setBarcode(itemDTO.getBarcode());
    item.setImageUrl(itemDTO.getImageUrl());

    // Ownership comes from the authenticated user
    item.setUser(user);

    PantryItem savedItem = pantryRepository.save(item);

    PantryItemResponseDTO responseDTO = new PantryItemResponseDTO();

    responseDTO.setId(savedItem.getId());
    responseDTO.setName(savedItem.getName());
    responseDTO.setQuantity(savedItem.getQuantity());
    responseDTO.setUnit(savedItem.getUnit());
    responseDTO.setExpiryDate(savedItem.getExpiryDate());
    responseDTO.setBarcode(savedItem.getBarcode());
    responseDTO.setImageUrl(savedItem.getImageUrl());
    responseDTO.setCreatedAt(savedItem.getCreatedAt());
    responseDTO.setUpdatedAt(savedItem.getUpdatedAt());

    return responseDTO;
}

public List<PantryItemResponseDTO> getItems(){
    
    Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

    String email = authentication.getName();

    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

   
    List<PantryItem> items = pantryRepository.findByUserId(user.getId());
    List<PantryItemResponseDTO> responseList=new ArrayList<>();
    for(PantryItem item:items){
        PantryItemResponseDTO responseDTO = new PantryItemResponseDTO();

        responseDTO.setId(item.getId());
        responseDTO.setName(item.getName());
        responseDTO.setQuantity(item.getQuantity());
        responseDTO.setUnit(item.getUnit());
        responseDTO.setExpiryDate(item.getExpiryDate());
        responseDTO.setBarcode(item.getBarcode());
        responseDTO.setImageUrl(item.getImageUrl());
        responseDTO.setCreatedAt(item.getCreatedAt());
        responseDTO.setUpdatedAt(item.getUpdatedAt());

        responseList.add(responseDTO);
    }
    return responseList;

}
public PantryItemResponseDTO getItemById(Long itemId) {
    Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

    String email = authentication.getName();

    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    PantryItem item = pantryRepository.findByIdAndUserId(itemId, user.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Item not found or does not belong to the user"));

    PantryItemResponseDTO responseDTO = new PantryItemResponseDTO();

    responseDTO.setId(item.getId());
    responseDTO.setName(item.getName());
    responseDTO.setQuantity(item.getQuantity());
    responseDTO.setUnit(item.getUnit());
    responseDTO.setExpiryDate(item.getExpiryDate());
    responseDTO.setBarcode(item.getBarcode());
    responseDTO.setImageUrl(item.getImageUrl());
    responseDTO.setCreatedAt(item.getCreatedAt());
    responseDTO.setUpdatedAt(item.getUpdatedAt());

    return responseDTO;
   

}
public PantryItemResponseDTO updateItem(Long itemId, PantryItemRequestDTO itemDTO) {
    Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

    String email = authentication.getName();

    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    PantryItem item = pantryRepository.findByIdAndUserId(itemId, user.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Item not found or does not belong to the user"));

    item.setName(itemDTO.getName());
    item.setQuantity(itemDTO.getQuantity());
    item.setUnit(itemDTO.getUnit());
    item.setExpiryDate(itemDTO.getExpiryDate());
    item.setBarcode(itemDTO.getBarcode());
    item.setImageUrl(itemDTO.getImageUrl());

    PantryItem updatedItem = pantryRepository.save(item);

    PantryItemResponseDTO responseDTO = new PantryItemResponseDTO();

    responseDTO.setId(updatedItem.getId());
    responseDTO.setName(updatedItem.getName());
    responseDTO.setQuantity(updatedItem.getQuantity());
    responseDTO.setUnit(updatedItem.getUnit());
    responseDTO.setExpiryDate(updatedItem.getExpiryDate());
    responseDTO.setBarcode(updatedItem.getBarcode());
    responseDTO.setImageUrl(updatedItem.getImageUrl());
    responseDTO.setCreatedAt(updatedItem.getCreatedAt());
    responseDTO.setUpdatedAt(updatedItem.getUpdatedAt());

    return responseDTO;
}
public void deleteItem(Long itemId) {
    Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

    String email = authentication.getName();

    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    PantryItem item = pantryRepository.findByIdAndUserId(itemId, user.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Item not found or does not belong to the user"));

    pantryRepository.delete(item);
}
}