package com.example.pantry.item.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.pantry.category.entity.Category;
import com.example.pantry.storage.entity.StorageLocation;
import com.example.pantry.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "pantry_items")
public class PantryItem {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private BigDecimal  quantity;

    @Column(length=100)
    private String barcode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Unit unit;

    @Column(name="expiry_date")
    private LocalDate expiryDate;

    @Column(name="image_url",length=500)
    private String imageUrl;

    @Column(name = "calendar_event_id", length = 255)
private String calendarEventId;

@Column(name = "created_at", nullable = false)
private LocalDateTime createdAt;

@Column(name = "updated_at", nullable = false)
private LocalDateTime updatedAt;

@ManyToOne
@JoinColumn(name = "category_id")
private Category category;

@ManyToOne
@JoinColumn(name = "storage_location_id")
private StorageLocation storageLocation;



@PrePersist
protected void onCreate() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
}

@PreUpdate
protected void onUpdate() {
    updatedAt = LocalDateTime.now();
}

@ManyToOne
@JoinColumn(name = "user_id", nullable = false)
private User user;
}
