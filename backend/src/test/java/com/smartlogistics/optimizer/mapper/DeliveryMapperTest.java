package com.smartlogistics.optimizer.mapper;

import com.smartlogistics.optimizer.controller.dto.DeliveryDtos;
import com.smartlogistics.optimizer.model.Delivery;
import com.smartlogistics.optimizer.model.Order;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

public class DeliveryMapperTest {
	private final DeliveryMapper mapper = Mappers.getMapper(DeliveryMapper.class);

	@Test
	void mapsEntityToResponse_withOrderId() {
		Order order = new Order();
		order.setId(11L);
		Delivery d = new Delivery();
		d.setOrder(order);
		d.setVehicleId("TRUCK-1");
		DeliveryDtos.Response r = mapper.toResponse(d);
		assertThat(r.orderId()).isEqualTo(11L);
	}

	@Test
	void fromCreate_setsFieldsAndOrder() {
		Order order = new Order();
		order.setId(9L);
		DeliveryDtos.CreateRequest req = new DeliveryDtos.CreateRequest("V1","R",null,null,9L);
		Delivery d = mapper.fromCreate(req, order);
		assertThat(d.getVehicleId()).isEqualTo("V1");
		assertThat(d.getOrder()).isEqualTo(order);
	}

	@Test
	void update_updatesFieldsAndOptionallyOrder() {
		Delivery d = new Delivery();
		d.setVehicleId("A");
		DeliveryDtos.UpdateRequest req = new DeliveryDtos.UpdateRequest("B","R2",null,null, null);
		Delivery out = mapper.update(d, req, null);
		assertThat(out.getVehicleId()).isEqualTo("B");
	}
}
