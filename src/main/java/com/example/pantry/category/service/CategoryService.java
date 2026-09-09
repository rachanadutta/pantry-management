package com.example.pantry.category.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.pantry.category.dto.CategoryResponseDTO;
import com.example.pantry.category.entity.Category;
import com.example.pantry.category.repository.CategoryRepository;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryResponseDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryResponseDTO> categoryResponseDTOs = new ArrayList<>();

        for (Category category : categories) {
            CategoryResponseDTO responseDTO = new CategoryResponseDTO();
            responseDTO.setId(category.getId());
            responseDTO.setName(category.getName());
            responseDTO.setImageUrl(category.getImageUrl());
            categoryResponseDTOs.add(responseDTO);
        }

        return categoryResponseDTOs;
    }
}
