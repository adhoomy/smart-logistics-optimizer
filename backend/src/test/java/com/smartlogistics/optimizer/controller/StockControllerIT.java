package com.smartlogistics.optimizer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartlogistics.optimizer.controller.dto.StockDtos;
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
class StockControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create_get_update_delete_stock() throws Exception {
        StockDtos.CreateRequest create = new StockDtos.CreateRequest(
                "W1",
                "Widget",
                100,
                "SKU-1",
                "A1"
        );

        String created = mockMvc.perform(post("/stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(create)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sku").value("SKU-1"))
                .andReturn().getResponse().getContentAsString();
        Long id = objectMapper.readTree(created).get("id").asLong();

        mockMvc.perform(get("/stock/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.warehouseId").value("W1"));

        StockDtos.UpdateRequest update = new StockDtos.UpdateRequest(
                "W2",
                "Widget",
                80,
                "SKU-1",
                "B2"
        );

        mockMvc.perform(put("/stock/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.warehouseId").value("W2"));

        mockMvc.perform(delete("/stock/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/stock/" + id))
                .andExpect(status().isNotFound());
    }
}


