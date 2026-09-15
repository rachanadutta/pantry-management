package com.example.pantry.item.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.pantry.item.dto.PantryItemRequestDTO;
import com.example.pantry.item.dto.PantryItemResponseDTO;
import com.example.pantry.item.service.PantryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/items")
public class PantryController {
    private final PantryService pantryService;


    public PantryController(PantryService pantryService) {
        this.pantryService = pantryService;

    }

    @PostMapping
    public PantryItemResponseDTO addItem(@Valid @RequestBody PantryItemRequestDTO itemDTO) {
        return pantryService.addItem(itemDTO);
    }

    @GetMapping
    public List<PantryItemResponseDTO> getAllItems(@RequestParam(required=false) String search,@RequestParam (required=false) Long categoryId, @RequestParam (required=false) Long storageLocationId, @RequestParam (required=false) Boolean expired,@RequestParam (required=false) LocalDate from,@RequestParam (required=false) LocalDate to,@RequestParam (required=false) String sort,@RequestParam (required=false) String order) {
        return pantryService.getItems(search,categoryId,storageLocationId,expired,from,to,sort,order);
    }
    
    @GetMapping("/{itemId}")
    public PantryItemResponseDTO getItemById(@PathVariable Long itemId) {
        return pantryService.getItemById(itemId);
    }

    @PutMapping("/{itemId}")
    public PantryItemResponseDTO updateItem(@PathVariable Long itemId, @Valid @RequestBody PantryItemRequestDTO itemDTO) {
        return pantryService.updateItem(itemId, itemDTO);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable Long itemId) {
        pantryService.deleteItem(itemId);
    }
}
