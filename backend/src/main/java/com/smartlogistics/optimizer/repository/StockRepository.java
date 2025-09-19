package com.smartlogistics.optimizer.repository;

import com.smartlogistics.optimizer.model.StockItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<StockItem, Long>, JpaSpecificationExecutor<StockItem> {
    
    List<StockItem> findByWarehouseId(String warehouseId);
    
    List<StockItem> findByProductNameContainingIgnoreCase(String productName);
    
    Optional<StockItem> findBySku(String sku);
    
    List<StockItem> findByLocation(String location);
    
    @Query("SELECT s FROM StockItem s WHERE s.quantity < :threshold")
    List<StockItem> findLowStockItems(@Param("threshold") Integer threshold);
    
    @Query("SELECT s FROM StockItem s WHERE s.warehouseId = :warehouseId AND s.quantity < :threshold")
    List<StockItem> findLowStockItemsByWarehouse(@Param("warehouseId") String warehouseId, 
                                                @Param("threshold") Integer threshold);
    
    @Query("SELECT s FROM StockItem s WHERE s.productName LIKE %:name% AND s.warehouseId = :warehouseId")
    List<StockItem> findByProductNameAndWarehouse(@Param("name") String productName, 
                                                 @Param("warehouseId") String warehouseId);
    
    @Query("SELECT SUM(s.quantity) FROM StockItem s WHERE s.warehouseId = :warehouseId")
    Long getTotalQuantityByWarehouse(@Param("warehouseId") String warehouseId);
    
    @Query("SELECT COUNT(s) FROM StockItem s WHERE s.warehouseId = :warehouseId AND s.quantity > 0")
    long countAvailableItemsByWarehouse(@Param("warehouseId") String warehouseId);
    
    boolean existsBySku(String sku);
}
