package com.smartlogistics.optimizer.repository.specification;

import com.smartlogistics.optimizer.model.Delivery;
import org.springframework.data.jpa.domain.Specification;

public class DeliverySpecifications {

    public static Specification<Delivery> hasStatus(Delivery.DeliveryStatus status) {
        return (root, query, criteriaBuilder) -> 
            status != null ? criteriaBuilder.equal(root.get("status"), status) : null;
    }

    public static Specification<Delivery> hasVehicleId(String vehicleId) {
        return (root, query, criteriaBuilder) -> 
            vehicleId != null && !vehicleId.trim().isEmpty() 
                ? criteriaBuilder.equal(root.get("vehicleId"), vehicleId)
                : null;
    }

    public static Specification<Delivery> hasRouteContaining(String route) {
        return (root, query, criteriaBuilder) -> 
            route != null && !route.trim().isEmpty() 
                ? criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("route")), 
                    "%" + route.toLowerCase() + "%"
                  )
                : null;
    }

    public static Specification<Delivery> hasOrderId(Long orderId) {
        return (root, query, criteriaBuilder) -> 
            orderId != null ? criteriaBuilder.equal(root.get("order").get("id"), orderId) : null;
    }
}
