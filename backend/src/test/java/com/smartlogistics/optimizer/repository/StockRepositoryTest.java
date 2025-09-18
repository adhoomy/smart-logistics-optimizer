package com.smartlogistics.optimizer.repository;

import com.smartlogistics.optimizer.model.StockItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class StockRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private StockRepository stockRepository;

    private StockItem testStockItem;

    @BeforeEach
    void setUp() {
        testStockItem = new StockItem();
        testStockItem.setWarehouseId("WH-001");
        testStockItem.setProductName("Test Product");
        testStockItem.setQuantity(100);
        testStockItem.setSku("TEST-001");
        testStockItem.setLocation("A1-B2-C3");
        
        entityManager.persistAndFlush(testStockItem);
    }

    @Test
    void testSaveAndFindById() {
        // Given
        StockItem newStockItem = new StockItem();
        newStockItem.setWarehouseId("WH-002");
        newStockItem.setProductName("New Product");
        newStockItem.setQuantity(50);
        newStockItem.setSku("NEW-001");
        newStockItem.setLocation("B1-C2-D3");

        // When
        StockItem savedStockItem = stockRepository.save(newStockItem);
        StockItem foundStockItem = stockRepository.findById(savedStockItem.getId()).orElse(null);

        // Then
        assertThat(foundStockItem).isNotNull();
        assertThat(foundStockItem.getProductName()).isEqualTo("New Product");
        assertThat(foundStockItem.getWarehouseId()).isEqualTo("WH-002");
    }

    @Test
    void testFindByWarehouseId() {
        // When
        List<StockItem> items = stockRepository.findByWarehouseId("WH-001");

        // Then
        assertThat(items).hasSize(1);
        assertThat(items.get(0).getProductName()).isEqualTo("Test Product");
    }

    @Test
    void testFindBySku() {
        // When
        Optional<StockItem> foundItem = stockRepository.findBySku("TEST-001");

        // Then
        assertThat(foundItem).isPresent();
        assertThat(foundItem.get().getProductName()).isEqualTo("Test Product");
    }

    @Test
    void testFindLowStockItems() {
        // Given
        StockItem lowStockItem = new StockItem();
        lowStockItem.setWarehouseId("WH-001");
        lowStockItem.setProductName("Low Stock Product");
        lowStockItem.setQuantity(5);
        lowStockItem.setSku("LOW-001");
        entityManager.persistAndFlush(lowStockItem);

        // When
        List<StockItem> lowStockItems = stockRepository.findLowStockItems(10);

        // Then
        assertThat(lowStockItems).hasSize(1);
        assertThat(lowStockItems.get(0).getProductName()).isEqualTo("Low Stock Product");
    }

    @Test
    void testExistsBySku() {
        // When
        boolean exists = stockRepository.existsBySku("TEST-001");
        boolean notExists = stockRepository.existsBySku("NON-EXISTENT");

        // Then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    void testFindByProductNameContaining() {
        // When
        List<StockItem> items = stockRepository.findByProductNameContainingIgnoreCase("test");

        // Then
        assertThat(items).hasSize(1);
        assertThat(items.get(0).getProductName()).isEqualTo("Test Product");
    }
}
