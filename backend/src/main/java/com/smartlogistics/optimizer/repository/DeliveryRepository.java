package com.smartlogistics.optimizer.repository;

import com.smartlogistics.optimizer.model.Delivery;
import com.smartlogistics.optimizer.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long>, JpaSpecificationExecutor<Delivery> {
    
    List<Delivery> findByStatus(Delivery.DeliveryStatus status);
    
    List<Delivery> findByVehicleId(String vehicleId);
    
    List<Delivery> findByOrder(Order order);
    
    @Query("SELECT d FROM Delivery d WHERE d.estimatedTime BETWEEN :startTime AND :endTime")
    List<Delivery> findByEstimatedTimeBetween(@Param("startTime") LocalDateTime startTime, 
                                            @Param("endTime") LocalDateTime endTime);
    
    @Query("SELECT d FROM Delivery d WHERE d.status = :status AND d.estimatedTime >= :fromTime")
    List<Delivery> findActiveDeliveriesFromTime(@Param("status") Delivery.DeliveryStatus status, 
                                              @Param("fromTime") LocalDateTime fromTime);
    
    @Query("SELECT d FROM Delivery d WHERE d.route LIKE %:routeSegment%")
    List<Delivery> findByRouteContaining(@Param("routeSegment") String routeSegment);
    
    long countByStatus(Delivery.DeliveryStatus status);
    
    @Query("SELECT COUNT(d) FROM Delivery d WHERE d.vehicleId = :vehicleId AND d.status IN :statuses")
    long countByVehicleIdAndStatusIn(@Param("vehicleId") String vehicleId, 
                                   @Param("statuses") List<Delivery.DeliveryStatus> statuses);
}
