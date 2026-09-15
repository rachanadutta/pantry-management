package com.example.pantry.item.specification;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.example.pantry.item.entity.PantryItem;

public class PantryItemSpecification {
    public static Specification<PantryItem> hasUser(Long userId) {

        return (root, query, builder) ->
                builder.equal(
                        root.get("user").get("id"),
                        userId
                );
    }
    public static Specification<PantryItem> hasName(String name){
        return (root,query,builder)->
        builder.like(
            builder.lower(root.get("name")),
            "%" + name.toLowerCase() + "%"
        );
    }
    public static Specification<PantryItem> hasCategory(Long categoryId){
        return (root,query,builder)->
        builder.equal(
            root.get("category").get("id"),
            categoryId
        );
    }
    public static Specification<PantryItem> hasStorageLocation(
        Long storageLocationId) {

    return (root, query, builder) ->
            builder.equal(
                    root.get("storageLocation").get("id"),
                    storageLocationId
            );
}
public static Specification<PantryItem> hasExpired(boolean expired) {
    
    return (root, query, builder) -> {
        
        if (expired) {
            return builder.lessThan(
                    root.get("expiryDate"),
                    LocalDate.now()
            );
        } else {
            return builder.greaterThanOrEqualTo(
                    root.get("expiryDate"),
                    LocalDate.now()
            );
        }
    };
}
public static Specification<PantryItem> expiryBetween(
        LocalDate from,
        LocalDate to) {

    return (root, query, builder) -> {

        if (from != null && to != null) {
            return builder.and(
                    builder.greaterThanOrEqualTo(
                            root.get("expiryDate"), from),
                    builder.lessThanOrEqualTo(
                            root.get("expiryDate"), to)
            );
        }

        if (from != null) {
            return builder.greaterThanOrEqualTo(
                    root.get("expiryDate"), from);
        }

        return builder.lessThanOrEqualTo(
                root.get("expiryDate"), to);
    };
}
public static Specification<PantryItem> hasBarcode(String barcode) {
    return (root, query, builder) ->
            builder.equal(
                    root.get("barcode"),
                    barcode
            );
}
}
