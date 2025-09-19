package com.smartlogistics.optimizer.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartlogistics.optimizer.controller.dto.DeliveryDtos;
import com.smartlogistics.optimizer.controller.dto.OrderDtos;
import com.smartlogistics.optimizer.controller.dto.StockDtos;
import com.smartlogistics.optimizer.model.Delivery;
import com.smartlogistics.optimizer.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class PaginationControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long orderId1, orderId2, orderId3;
    private Long deliveryId1, deliveryId2, deliveryId3;
    private Long stockId1, stockId2, stockId3;

    @BeforeEach
    void setUp() throws Exception {
        // Create test orders
        orderId1 = createOrder("Acme Corp", "123 Market St", 10, Order.OrderStatus.PENDING);
        orderId2 = createOrder("Globex Inc", "456 Elm St", 15, Order.OrderStatus.CONFIRMED);
        orderId3 = createOrder("Acme Corp", "789 Oak Ave", 5, Order.OrderStatus.DELIVERED);

        // Create test deliveries
        deliveryId1 = createDelivery("TRUCK-42", "A-B-C", orderId1, Delivery.DeliveryStatus.PENDING);
        deliveryId2 = createDelivery("TRUCK-42", "A-D-E", orderId2, Delivery.DeliveryStatus.IN_TRANSIT);
        deliveryId3 = createDelivery("VAN-99", "A-F-G", orderId3, Delivery.DeliveryStatus.DELIVERED);

        // Create test stock items
        stockId1 = createStockItem("W1", "Widget", 100, "SKU-001", "A1");
        stockId2 = createStockItem("W1", "Gadget", 50, "SKU-002", "B2");
        stockId3 = createStockItem("W2", "Widget", 75, "SKU-003", "C3");
    }

    @Test
    void testOrderPagination() throws Exception {
        // Test basic pagination
        mockMvc.perform(get("/orders?page=0&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(2))
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.first").value(true))
                .andExpect(jsonPath("$.last").value(false))
                .andExpect(jsonPath("$.hasNext").value(true))
                .andExpect(jsonPath("$.hasPrevious").value(false));

        // Test second page
        mockMvc.perform(get("/orders?page=1&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.first").value(false))
                .andExpect(jsonPath("$.last").value(true));
    }

    @Test
    void testOrderSorting() throws Exception {
        // Test sorting by customer name ascending
        mockMvc.perform(get("/orders?sort=customerName,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].customerName").value("Acme Corp"))
                .andExpect(jsonPath("$.content[1].customerName").value("Acme Corp"))
                .andExpect(jsonPath("$.content[2].customerName").value("Globex Inc"));

        // Test sorting by quantity descending
        mockMvc.perform(get("/orders?sort=quantity,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].quantity").value(15))
                .andExpect(jsonPath("$.content[1].quantity").value(10))
                .andExpect(jsonPath("$.content[2].quantity").value(5));
    }

    @Test
    void testOrderFiltering() throws Exception {
        // Test filtering by status
        mockMvc.perform(get("/orders?status=PENDING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].status").value("PENDING"));

        // Test filtering by customer name
        mockMvc.perform(get("/orders?customerName=Acme"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].customerName").value("Acme Corp"))
                .andExpect(jsonPath("$.content[1].customerName").value("Acme Corp"));

        // Test combined filtering
        mockMvc.perform(get("/orders?status=CONFIRMED&customerName=Globex"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].customerName").value("Globex Inc"))
                .andExpect(jsonPath("$.content[0].status").value("CONFIRMED"));
    }

    @Test
    void testDeliveryPagination() throws Exception {
        // Test basic pagination
        mockMvc.perform(get("/deliveries?page=0&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.totalElements").value(3));

        // Test filtering by status
        mockMvc.perform(get("/deliveries?status=IN_TRANSIT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].status").value("IN_TRANSIT"));

        // Test filtering by vehicle ID
        mockMvc.perform(get("/deliveries?vehicleId=TRUCK-42"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].vehicleId").value("TRUCK-42"))
                .andExpect(jsonPath("$.content[1].vehicleId").value("TRUCK-42"));
    }

    @Test
    void testDeliverySorting() throws Exception {
        // Test sorting by vehicle ID
        mockMvc.perform(get("/deliveries?sort=vehicleId,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].vehicleId").value("TRUCK-42"))
                .andExpect(jsonPath("$.content[1].vehicleId").value("TRUCK-42"))
                .andExpect(jsonPath("$.content[2].vehicleId").value("VAN-99"));
    }

    @Test
    void testStockPagination() throws Exception {
        // Test basic pagination
        mockMvc.perform(get("/stock?page=0&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.totalElements").value(3));

        // Test filtering by warehouse ID
        mockMvc.perform(get("/stock?warehouseId=W1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].warehouseId").value("W1"))
                .andExpect(jsonPath("$.content[1].warehouseId").value("W1"));

        // Test filtering by product name
        mockMvc.perform(get("/stock?productName=Widget"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].productName").value("Widget"))
                .andExpect(jsonPath("$.content[1].productName").value("Widget"));

        // Test combined filtering
        mockMvc.perform(get("/stock?warehouseId=W1&productName=Gadget"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].warehouseId").value("W1"))
                .andExpect(jsonPath("$.content[0].productName").value("Gadget"));
    }

    @Test
    void testStockSorting() throws Exception {
        // Test sorting by quantity descending
        mockMvc.perform(get("/stock?sort=quantity,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].quantity").value(100))
                .andExpect(jsonPath("$.content[1].quantity").value(75))
                .andExpect(jsonPath("$.content[2].quantity").value(50));
    }

    @Test
    void testDefaultPagination() throws Exception {
        // Test default pagination parameters
        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.content.length()").value(3));
    }

    @Test
    void testInvalidPagination() throws Exception {
        // Test with invalid page size (should use default)
        mockMvc.perform(get("/orders?page=0&size=0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(0)); // Spring will use the provided value

        // Test with negative page (should use default)
        mockMvc.perform(get("/orders?page=-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").value(0));
    }

    private Long createOrder(String customerName, String address, Integer quantity, Order.OrderStatus status) throws Exception {
        OrderDtos.CreateRequest create = new OrderDtos.CreateRequest(
                customerName, address, quantity, null, status
        );

        String response = mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(create)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(response);
        return jsonNode.get("id").asLong();
    }

    private Long createDelivery(String vehicleId, String route, Long orderId, Delivery.DeliveryStatus status) throws Exception {
        DeliveryDtos.CreateRequest create = new DeliveryDtos.CreateRequest(
                vehicleId, route, null, status, orderId
        );

        String response = mockMvc.perform(post("/deliveries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(create)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(response);
        return jsonNode.get("id").asLong();
    }

    private Long createStockItem(String warehouseId, String productName, Integer quantity, String sku, String location) throws Exception {
        StockDtos.CreateRequest create = new StockDtos.CreateRequest(
                warehouseId, productName, quantity, sku, location
        );

        String response = mockMvc.perform(post("/stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(create)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(response);
        return jsonNode.get("id").asLong();
    }
}
