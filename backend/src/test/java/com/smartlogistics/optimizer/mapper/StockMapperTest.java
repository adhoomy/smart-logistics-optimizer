package com.smartlogistics.optimizer.mapper;

import com.smartlogistics.optimizer.controller.dto.StockDtos;
import com.smartlogistics.optimizer.model.StockItem;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

public class StockMapperTest {
	private final StockMapper mapper = Mappers.getMapper(StockMapper.class);

	@Test
	void mapsEntityToResponse() {
		StockItem s = new StockItem();
		s.setId(5L);
		s.setWarehouseId("W1");
		StockDtos.Response r = mapper.toResponse(s);
		assertThat(r.id()).isEqualTo(5L);
		assertThat(r.warehouseId()).isEqualTo("W1");
	}

	@Test
	void fromCreate_setsFields() {
		StockDtos.CreateRequest req = new StockDtos.CreateRequest("W2","Widget",10,"SKU","A1");
		StockItem s = mapper.fromCreate(req);
		assertThat(s.getProductName()).isEqualTo("Widget");
		assertThat(s.getQuantity()).isEqualTo(10);
	}

	@Test
	void update_updatesFields() {
		StockItem s = new StockItem();
		StockDtos.UpdateRequest req = new StockDtos.UpdateRequest("W3","Gadget",2,"SKU2","B2");
		StockItem out = mapper.update(s, req);
		assertThat(out.getWarehouseId()).isEqualTo("W3");
		assertThat(out.getProductName()).isEqualTo("Gadget");
	}
}
