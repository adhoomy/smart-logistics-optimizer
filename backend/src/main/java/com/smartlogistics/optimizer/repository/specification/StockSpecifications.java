package com.smartlogistics.optimizer.repository.specification;

import com.smartlogistics.optimizer.model.StockItem;
import org.springframework.data.jpa.domain.Specification;

public class StockSpecifications {

    public static Specification<StockItem> hasWarehouseId(String warehouseId) {
        return (root, query, criteriaBuilder) -> 
            warehouseId != null && !warehouseId.trim().isEmpty() 
                ? criteriaBuilder.equal(root.get("warehouseId"), warehouseId)
                : null;
    }

    public static Specification<StockItem> hasProductNameContaining(String productName) {
        return (root, query, criteriaBuilder) -> 
            productName != null && !productName.trim().isEmpty() 
                ? criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("productName")), 
                    "%" + productName.toLowerCase() + "%"
                  )
                : null;
    }

    public static Specification<StockItem> hasSku(String sku) {
        return (root, query, criteriaBuilder) -> 
            sku != null && !sku.trim().isEmpty() 
                ? criteriaBuilder.equal(root.get("sku"), sku)
                : null;
    }

    public static Specification<StockItem> hasLocation(String location) {
        return (root, query, criteriaBuilder) -> 
            location != null && !location.trim().isEmpty() 
                ? criteriaBuilder.equal(root.get("location"), location)
                : null;
    }

    public static Specification<StockItem> hasQuantityGreaterThanOrEqual(Integer minQuantity) {
        return (root, query, criteriaBuilder) -> 
            minQuantity != null ? criteriaBuilder.greaterThanOrEqualTo(root.get("quantity"), minQuantity) : null;
    }

    public static Specification<StockItem> hasQuantityLessThanOrEqual(Integer maxQuantity) {
        return (root, query, criteriaBuilder) -> 
            maxQuantity != null ? criteriaBuilder.lessThanOrEqualTo(root.get("quantity"), maxQuantity) : null;
    }
}
