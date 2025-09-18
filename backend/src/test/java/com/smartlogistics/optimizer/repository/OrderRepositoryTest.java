package com.smartlogistics.optimizer.repository;

import com.smartlogistics.optimizer.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class OrderRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OrderRepository orderRepository;

    private Order testOrder;

    @BeforeEach
    void setUp() {
        testOrder = new Order();
        testOrder.setCustomerName("Test Customer");
        testOrder.setDestinationAddress("123 Test St, Test City, TC 12345");
        testOrder.setQuantity(5);
        testOrder.setDeliveryDate(LocalDateTime.now().plusDays(1));
        testOrder.setStatus(Order.OrderStatus.PENDING);
        
        entityManager.persistAndFlush(testOrder);
    }

    @Test
    void testSaveAndFindById() {
        // Given
        Order newOrder = new Order();
        newOrder.setCustomerName("New Customer");
        newOrder.setDestinationAddress("456 New Ave, New City, NC 67890");
        newOrder.setQuantity(3);
        newOrder.setStatus(Order.OrderStatus.CONFIRMED);

        // When
        Order savedOrder = orderRepository.save(newOrder);
        Order foundOrder = orderRepository.findById(savedOrder.getId()).orElse(null);

        // Then
        assertThat(foundOrder).isNotNull();
        assertThat(foundOrder.getCustomerName()).isEqualTo("New Customer");
        assertThat(foundOrder.getStatus()).isEqualTo(Order.OrderStatus.CONFIRMED);
    }

    @Test
    void testFindByStatus() {
        // When
        List<Order> pendingOrders = orderRepository.findByStatus(Order.OrderStatus.PENDING);

        // Then
        assertThat(pendingOrders).hasSize(1);
        assertThat(pendingOrders.get(0).getCustomerName()).isEqualTo("Test Customer");
    }

    @Test
    void testFindByCustomerNameContaining() {
        // When
        List<Order> orders = orderRepository.findByCustomerNameContainingIgnoreCase("test");

        // Then
        assertThat(orders).hasSize(1);
        assertThat(orders.get(0).getCustomerName()).isEqualTo("Test Customer");
    }

    @Test
    void testFindByDestinationAddressContaining() {
        // When
        List<Order> orders = orderRepository.findByDestinationAddressContaining("Test St");

        // Then
        assertThat(orders).hasSize(1);
        assertThat(orders.get(0).getDestinationAddress()).contains("Test St");
    }

    @Test
    void testCountByStatus() {
        // When
        long pendingCount = orderRepository.countByStatus(Order.OrderStatus.PENDING);
        long confirmedCount = orderRepository.countByStatus(Order.OrderStatus.CONFIRMED);

        // Then
        assertThat(pendingCount).isEqualTo(1);
        assertThat(confirmedCount).isEqualTo(0);
    }
}
