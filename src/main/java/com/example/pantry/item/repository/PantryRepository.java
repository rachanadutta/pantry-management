package com.example.pantry.item.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pantry.item.entity.PantryItem;

public interface PantryRepository extends JpaRepository<PantryItem, Long> {
    List<PantryItem> findByUserId(Long userId);
    Optional<PantryItem> findByIdAndUserId(Long itemId, Long userId);
    
}
