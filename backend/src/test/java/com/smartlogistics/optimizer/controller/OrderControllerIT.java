package com.smartlogistics.optimizer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartlogistics.optimizer.controller.dto.OrderDtos;
import com.smartlogistics.optimizer.model.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OrderControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create_get_update_delete_order() throws Exception {
        OrderDtos.CreateRequest create = new OrderDtos.CreateRequest(
                "Acme",
                "123 St",
                5,
                null,
                null
        );

        String created = mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(create)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").exists())
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(created).get("id").asLong();

        mockMvc.perform(get("/orders/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("Acme"));

        OrderDtos.UpdateRequest update = new OrderDtos.UpdateRequest(
                "Globex",
                "456 Ave",
                7,
                null,
                null
        );

        mockMvc.perform(put("/orders/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("Globex"));

        mockMvc.perform(delete("/orders/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/orders/" + id))
                .andExpect(status().isNotFound());
    }
}


