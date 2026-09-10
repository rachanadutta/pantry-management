package com.example.pantry.storage.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pantry.storage.entity.StorageLocation;
import com.example.pantry.storage.entity.StorageLocationType;
import com.example.pantry.user.entity.User;
public interface StorageLocationRepository extends JpaRepository<StorageLocation, Long> {
    List<StorageLocation> findByTypeOrUser(StorageLocationType type, User user);
    boolean existsByNameAndUser(String name, User user);
    boolean existsByNameAndType(String name, StorageLocationType type);
}
