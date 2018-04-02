package com.cmdt.carrental.quartz.filter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cmdt.carrental.common.integration.ServiceAdapter;
import com.cmdt.carrental.common.integration.ShouqiService;
import com.cmdt.carrental.common.integration.model.ActionName;
import com.cmdt.carrental.common.model.OBDQueryDTO;
import com.cmdt.carrental.common.model.RealtimeLatestDataModel;
import com.cmdt.carrental.common.model.VehicleAlertModel;
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.quartz.service.VehicleAlertJobService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

@Component
public abstract class Filter<T> {
	
	private Logger LOG = Logger.getLogger(Filter.class);
	
	@Autowired
	protected ShouqiService shouqiService;
	
	@Autowired
	protected VehicleAlertJobService<T> vehicleAlertJobService;

	protected static final ObjectMapper MAPPER = new ObjectMapper();

	public abstract List<VehicleAlertModel> doFilter();
	
	public abstract List<VehicleAlertModel> doFilter(List<Condition<T>> conditions);
	
	/**
	 * 获取车辆当前GPS数据.
	 */
	protected Map<String,RealtimeLatestDataModel> getVehicleRealtimeData(List<OBDQueryDTO> obdList) {
		Map<String,RealtimeLatestDataModel> obdDataMap = new HashMap<String,RealtimeLatestDataModel>();

		if (obdList != null && !obdList.isEmpty()) {
			Map<String, Object> result = new ServiceAdapter(shouqiService).doService(ActionName.GETLATESTDATASBYIMEI,
					new Object[] { obdList });
			try {
				if (result != null && result.get("status").equals("success") && result.get("data") != null && StringUtils.isNotEmpty(result.get("data").toString())) {
					JsonNode jsonNode = MAPPER.readTree(result.get("data").toString());
					if ("000".equals(jsonNode.get("status").asText())) {

						ArrayNode rows = (ArrayNode) jsonNode.get("result");
						if (rows != null && rows.size() > 0) {
							for (int i = 0; i < rows.size(); i++) {
								JsonNode rowNode = rows.get(i);
								RealtimeLatestDataModel realtimeLatestDataModel = JsonUtils
										.json2Object(rowNode.toString(), RealtimeLatestDataModel.class);
								obdDataMap.put(realtimeLatestDataModel.getImei(),realtimeLatestDataModel);
							}
						}
						return obdDataMap;
					}
					return obdDataMap;
				}
				return obdDataMap;
			} catch (Exception e) {
				LOG.error("Abstract class Filter getVehicleRealtimeData failure, cause by\n", e);
				return obdDataMap;
			}
		}
		return obdDataMap;
	}
}
