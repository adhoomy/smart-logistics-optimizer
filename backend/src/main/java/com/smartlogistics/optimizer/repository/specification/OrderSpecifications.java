package com.smartlogistics.optimizer.repository.specification;

import com.smartlogistics.optimizer.model.Order;
import org.springframework.data.jpa.domain.Specification;

public class OrderSpecifications {

    public static Specification<Order> hasStatus(Order.OrderStatus status) {
        return (root, query, criteriaBuilder) -> 
            status != null ? criteriaBuilder.equal(root.get("status"), status) : null;
    }

    public static Specification<Order> hasCustomerNameContaining(String customerName) {
        return (root, query, criteriaBuilder) -> 
            customerName != null && !customerName.trim().isEmpty() 
                ? criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("customerName")), 
                    "%" + customerName.toLowerCase() + "%"
                  )
                : null;
    }

    public static Specification<Order> hasDestinationAddressContaining(String address) {
        return (root, query, criteriaBuilder) -> 
            address != null && !address.trim().isEmpty() 
                ? criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("destinationAddress")), 
                    "%" + address.toLowerCase() + "%"
                  )
                : null;
    }

    public static Specification<Order> hasQuantityGreaterThanOrEqual(Integer minQuantity) {
        return (root, query, criteriaBuilder) -> 
            minQuantity != null ? criteriaBuilder.greaterThanOrEqualTo(root.get("quantity"), minQuantity) : null;
    }

    public static Specification<Order> hasQuantityLessThanOrEqual(Integer maxQuantity) {
        return (root, query, criteriaBuilder) -> 
            maxQuantity != null ? criteriaBuilder.lessThanOrEqualTo(root.get("quantity"), maxQuantity) : null;
    }
}
