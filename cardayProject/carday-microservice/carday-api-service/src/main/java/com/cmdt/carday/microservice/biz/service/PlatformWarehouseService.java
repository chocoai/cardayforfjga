package com.cmdt.carday.microservice.biz.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cmdt.carday.microservice.model.request.warehouse.WarehouseDto;

//Service
public class PlatformWarehouseService {
	private static final Logger LOG = LoggerFactory.getLogger(PlatformWarehouseService.class);

	public Map<String,String> createWarehouse(WarehouseDto dto) throws Exception {
		LOG.debug("PlatformVehicleService.createVehicle");
		Map<String,String> map = new HashMap<>();
		map.put("status", "sucess");
		return map;
	}
}
