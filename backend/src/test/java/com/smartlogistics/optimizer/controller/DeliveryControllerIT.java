package com.smartlogistics.optimizer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartlogistics.optimizer.controller.dto.DeliveryDtos;
import com.smartlogistics.optimizer.controller.dto.OrderDtos;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class DeliveryControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create_get_update_delete_delivery() throws Exception {
        OrderDtos.CreateRequest orderReq = new OrderDtos.CreateRequest(
                "Acme",
                "123 St",
                3,
                null,
                null
        );

        String orderCreated = mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Long orderId = objectMapper.readTree(orderCreated).get("id").asLong();

        DeliveryDtos.CreateRequest create = new DeliveryDtos.CreateRequest(
                "TRUCK-1",
                "A-B-C",
                null,
                null,
                orderId
        );

        String created = mockMvc.perform(post("/deliveries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(create)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.vehicleId").value("TRUCK-1"))
                .andReturn().getResponse().getContentAsString();
        Long id = objectMapper.readTree(created).get("id").asLong();

        mockMvc.perform(get("/deliveries/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(orderId));

        DeliveryDtos.UpdateRequest update = new DeliveryDtos.UpdateRequest(
                "TRUCK-2",
                "A-C",
                null,
                null,
                orderId
        );

        mockMvc.perform(put("/deliveries/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vehicleId").value("TRUCK-2"));

        mockMvc.perform(delete("/deliveries/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/deliveries/" + id))
                .andExpect(status().isNotFound());
    }
}


