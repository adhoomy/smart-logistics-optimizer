package com.smartlogistics.optimizer.repository;

import com.smartlogistics.optimizer.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    
    List<Order> findByStatus(Order.OrderStatus status);
    
    List<Order> findByCustomerNameContainingIgnoreCase(String customerName);
    
    List<Order> findByDeliveryDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT o FROM Order o WHERE o.destinationAddress LIKE %:address%")
    List<Order> findByDestinationAddressContaining(@Param("address") String address);
    
    @Query("SELECT o FROM Order o WHERE o.status = :status AND o.deliveryDate >= :fromDate")
    List<Order> findPendingOrdersFromDate(@Param("status") Order.OrderStatus status, 
                                        @Param("fromDate") LocalDateTime fromDate);
    
    long countByStatus(Order.OrderStatus status);
}
