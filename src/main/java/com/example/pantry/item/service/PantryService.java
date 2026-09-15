package com.example.pantry.item.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.pantry.category.dto.CategoryResponseDTO;
import com.example.pantry.category.entity.Category;
import com.example.pantry.category.repository.CategoryRepository;
import com.example.pantry.exception.ResourceNotFoundException;
import com.example.pantry.item.dto.PantryItemRequestDTO;
import com.example.pantry.item.dto.PantryItemResponseDTO;
import com.example.pantry.item.entity.PantryItem;
import com.example.pantry.item.repository.PantryRepository;
import com.example.pantry.item.specification.PantryItemSpecification;
import com.example.pantry.storage.dto.StorageLocationResponseDTO;
import com.example.pantry.storage.entity.StorageLocation;
import com.example.pantry.storage.entity.StorageLocationType;
import com.example.pantry.storage.repository.StorageLocationRepository;
import com.example.pantry.user.entity.User;
import com.example.pantry.user.repository.UserRepository;

@Service
public class PantryService {

    private final PantryRepository pantryRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final StorageLocationRepository storageLocationRepository;
    private static final Set<String> ALLOWED_SORT_FIELDS =
        Set.of("expiryDate", "name", "quantity", "createdAt");

    public PantryService(
            PantryRepository pantryRepository,
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            StorageLocationRepository storageLocationRepository) {

        this.pantryRepository = pantryRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.storageLocationRepository = storageLocationRepository;
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

        Category category = null;

        if (itemDTO.getCategoryId() != null) {

            category = categoryRepository.findById(itemDTO.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

            item.setCategory(category);
        }
        StorageLocation storageLocation;

if (itemDTO.getStorageLocationId() != null) {

    storageLocation = storageLocationRepository
            .findById(itemDTO.getStorageLocationId())
            .orElseThrow(() -> new ResourceNotFoundException(
                    "Storage location not found"));

    if (storageLocation.getType() == StorageLocationType.CUSTOM
            && !storageLocation.getUser().getId().equals(user.getId())) {

        throw new ResourceNotFoundException(
                "Storage location not found");
    }

    item.setStorageLocation(storageLocation);
}

        // Use custom image if provided.
        // Otherwise, use the selected category's default image.
        if (itemDTO.getImageUrl() != null) {
            item.setImageUrl(itemDTO.getImageUrl());
        } else if (category != null) {
            item.setImageUrl(category.getImageUrl());
        }

        // Ownership comes from the authenticated user
        item.setUser(user);

        PantryItem savedItem = pantryRepository.save(item);

        return convertToResponseDTO(savedItem);
    }

    public List<PantryItemResponseDTO> getItems(String search,Long categoryId,Long storageLocationId,Boolean expired,LocalDate from,LocalDate to,String sortBy,String sortOrder) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Specification<PantryItem> specification =
        PantryItemSpecification.hasUser(user.getId());
        if (search != null && !search.isBlank()) {
    specification = specification.and(
            PantryItemSpecification.hasName(search)
                    .or(PantryItemSpecification.hasBarcode(search))
    );
}
        if(categoryId!=null){
                specification = specification.and(
                PantryItemSpecification.hasCategory(categoryId)
                );
        }

        if(storageLocationId!=null){
                specification = specification.and(
                PantryItemSpecification.hasStorageLocation(storageLocationId)
                );
        }
        if(expired!=null){
                specification = specification.and(
                PantryItemSpecification.hasExpired(expired)
                );
        }
        if(from!=null || to!=null){
                specification = specification.and(
                PantryItemSpecification.expiryBetween(from,to)
                );
        }
        Sort sort;

        if (sortBy != null) {

    if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
        throw new IllegalArgumentException("Invalid sort field" + sortBy);
    }

    if (sortOrder != null &&
            !sortOrder.equalsIgnoreCase("asc") &&
            !sortOrder.equalsIgnoreCase("desc")) {
        throw new IllegalArgumentException("Invalid sort order" + sortOrder);
    }

    if (sortOrder != null && sortOrder.equalsIgnoreCase("desc")) {
        sort = Sort.by(Sort.Direction.DESC, sortBy);
    } else {
        sort = Sort.by(Sort.Direction.ASC, sortBy);
    }

} else {
    sort = Sort.unsorted();
}

        List<PantryItem> items =
        pantryRepository.findAll(specification,sort);
        

        List<PantryItemResponseDTO> responseList = new ArrayList<>();

        for (PantryItem item : items) {
            responseList.add(convertToResponseDTO(item));
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
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Item not found or does not belong to the user"));

        return convertToResponseDTO(item);
    }

    public PantryItemResponseDTO updateItem(
            Long itemId,
            PantryItemRequestDTO itemDTO) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        PantryItem item = pantryRepository.findByIdAndUserId(itemId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Item not found or does not belong to the user"));

        item.setName(itemDTO.getName());
        item.setQuantity(itemDTO.getQuantity());
        item.setUnit(itemDTO.getUnit());
        item.setExpiryDate(itemDTO.getExpiryDate());
        item.setBarcode(itemDTO.getBarcode());

        Category category = null;

        if (itemDTO.getCategoryId() != null) {

            category = categoryRepository.findById(itemDTO.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

            item.setCategory(category);

        } else {
            item.setCategory(null);
        }
        StorageLocation storageLocation ;

if (itemDTO.getStorageLocationId() != null) {

    storageLocation = storageLocationRepository
            .findById(itemDTO.getStorageLocationId())
            .orElseThrow(() -> new ResourceNotFoundException(
                    "Storage location not found"));

    if (storageLocation.getType() == StorageLocationType.CUSTOM
            && !storageLocation.getUser().getId().equals(user.getId())) {

        throw new ResourceNotFoundException(
                "Storage location not found");
    }

    item.setStorageLocation(storageLocation);

} else {
    item.setStorageLocation(null);
}

        // Use custom image if provided.
        // Otherwise, use the selected category's default image.
        if (itemDTO.getImageUrl() != null) {
            item.setImageUrl(itemDTO.getImageUrl());
        } else if (category != null) {
            item.setImageUrl(category.getImageUrl());
        } else {
            item.setImageUrl(null);
        }

        PantryItem updatedItem = pantryRepository.save(item);

        return convertToResponseDTO(updatedItem);
    }

    public void deleteItem(Long itemId) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        PantryItem item = pantryRepository.findByIdAndUserId(itemId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Item not found or does not belong to the user"));

        pantryRepository.delete(item);
    }

    private PantryItemResponseDTO convertToResponseDTO(PantryItem item) {

        PantryItemResponseDTO responseDTO =
                new PantryItemResponseDTO();

        responseDTO.setId(item.getId());
        responseDTO.setName(item.getName());
        responseDTO.setQuantity(item.getQuantity());
        responseDTO.setUnit(item.getUnit());
        responseDTO.setExpiryDate(item.getExpiryDate());
        responseDTO.setBarcode(item.getBarcode());
        responseDTO.setImageUrl(item.getImageUrl());
        responseDTO.setCreatedAt(item.getCreatedAt());
        responseDTO.setUpdatedAt(item.getUpdatedAt());

        if (item.getCategory() != null) {

            CategoryResponseDTO categoryDTO =
                    new CategoryResponseDTO();

            categoryDTO.setId(item.getCategory().getId());
            categoryDTO.setName(item.getCategory().getName());
            categoryDTO.setImageUrl(item.getCategory().getImageUrl());

            responseDTO.setCategory(categoryDTO);
        }
        if (item.getStorageLocation() != null) {

    StorageLocationResponseDTO storageLocationDTO =
            new StorageLocationResponseDTO();

    storageLocationDTO.setId(item.getStorageLocation().getId());
    storageLocationDTO.setName(item.getStorageLocation().getName());
    storageLocationDTO.setType(item.getStorageLocation().getType());

    responseDTO.setStorageLocation(storageLocationDTO);
}

        return responseDTO;
    }
}