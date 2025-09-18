package com.smartlogistics.optimizer.mapper;

import com.smartlogistics.optimizer.controller.dto.OrderDtos;
import com.smartlogistics.optimizer.model.Order;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderMapperTest {
	private final OrderMapper mapper = Mappers.getMapper(OrderMapper.class);

	@Test
	void mapsEntityToResponse() {
		Order o = new Order();
		o.setId(42L);
		o.setCustomerName("Acme");
		o.setDestinationAddress("123");
		o.setQuantity(5);
		o.setDeliveryDate(LocalDateTime.of(2025,1,1,10,0));
		o.setCreatedAt(LocalDateTime.of(2025,1,1,9,0));
		OrderDtos.Response r = mapper.toResponse(o);
		assertThat(r.id()).isEqualTo(42L);
		assertThat(r.customerName()).isEqualTo("Acme");
	}

	@Test
	void mapsCreateRequestToEntity() {
		OrderDtos.CreateRequest req = new OrderDtos.CreateRequest("Globex","456",7,null,null);
		Order o = mapper.fromCreate(req);
		assertThat(o.getCustomerName()).isEqualTo("Globex");
		assertThat(o.getQuantity()).isEqualTo(7);
	}

	@Test
	void updatesEntityFromRequest() {
		Order existing = new Order();
		existing.setCustomerName("Old");
		existing.setQuantity(1);
		OrderDtos.UpdateRequest req = new OrderDtos.UpdateRequest("New","Addr",3,null,null);
		Order updated = mapper.update(existing, req);
		assertThat(updated.getCustomerName()).isEqualTo("New");
		assertThat(updated.getQuantity()).isEqualTo(3);
	}
}
