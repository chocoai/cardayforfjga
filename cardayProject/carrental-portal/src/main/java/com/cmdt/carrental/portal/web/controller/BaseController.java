package com.cmdt.carrental.portal.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.integration.CarDayService;
import com.cmdt.carrental.common.integration.ServiceAdapter;
import com.cmdt.carrental.common.integration.model.ActionName;
import com.cmdt.carrental.common.model.ResponseModel;
import com.cmdt.carrental.common.model.ResponseTreeModel;
import com.cmdt.carrental.common.model.StateResponseModel;
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.common.util.TypeUtils;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class BaseController {

	private static final Logger LOG = LoggerFactory.getLogger(BaseController.class);
	private static final String ENCODING = "UTF-8";

	@Autowired
	private CarDayService carDayService;
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> doRequest(ActionName action,Object[] params){
		Map<String, Object> map = new HashMap<>();
		String resp_json = new ServiceAdapter(carDayService).doCarDayService(action, params);
		Map<String, Object> jsonMap = JsonUtils.json2Object(resp_json, Map.class);
		Object result=null;
		String errorMsg="";
		if("000".equals(jsonMap.get("status"))){
			result=jsonMap.get("result");
			map.put("status", "success");
			map.put("data", result);
		}else{
			errorMsg=TypeUtils.obj2String(jsonMap.get("messages"));
			map.put("status", "failure");
			map.put("data", errorMsg);
		}
		return map;
	}
	
	protected String formatFailureResponse(ResponseModel<?> resp, String status, String failureMsg) {
		ObjectMapper om = new ObjectMapper();
		try {
			if (status != null) {
				resp.setStatus(status);
			}
			resp.setFailureMsg(failureMsg);
			resp.setSuccess(false);
			final byte[] data = om.writeValueAsBytes(resp);
			String respStr = new String(data, ENCODING);
			LOG.debug("Response: " + respStr);
			return respStr;
		} catch (Exception e) {
			LOG.error("Failed to convert the marker objects into JSON format!", e);
			return formatFailureResponse(new StateResponseModel(), ResponseModel.SERVICE_FAILURE,
					ResponseModel.JSON_FORMAT_FAILURE + ": " + e.getMessage());
		}
	}

	protected String formatFailureResponse(ResponseTreeModel<?> resp, String status, String failureMsg) {
		ObjectMapper om = new ObjectMapper();
		try {
			if (status != null) {
				resp.setStatus(status);
			}
			resp.setFailureMsg(failureMsg);
			resp.setSuccess(false);
			final byte[] data = om.writeValueAsBytes(resp);
			String respStr = new String(data, ENCODING);
			LOG.debug("Response: " + respStr);
			return respStr;
		} catch (Exception e) {
			LOG.error("Failed to convert the marker objects into JSON format!", e);
			return formatFailureResponse(new StateResponseModel(), ResponseModel.SERVICE_FAILURE,
					ResponseModel.JSON_FORMAT_FAILURE + ": " + e.getMessage());
		}
	}

	protected String formatResponse(ResponseModel<?> resp, Integer totalPage, String status) {
		ObjectMapper om = new ObjectMapper();
		try {
			if (totalPage != null) {
				resp.setTotal(totalPage);
			}
			if (status != null) {
				resp.setStatus(status);
			}
			resp.setSuccess(true);
			final byte[] data = om.writeValueAsBytes(resp);
			String respStr = new String(data, ENCODING);
			LOG.debug("Response: " + respStr);
			return respStr;
		} catch (Exception e) {
			LOG.error("Failed to convert the marker objects into JSON format!", e);
			return formatFailureResponse(new StateResponseModel(), ResponseModel.SERVICE_FAILURE,
					ResponseModel.JSON_FORMAT_FAILURE + ": " + e.getMessage());
		}
	}

	protected String formatResponse(ResponseTreeModel<?> resp, Integer totalPage, String status) {
		ObjectMapper om = new ObjectMapper();
		try {
			if (totalPage != null) {
				resp.setTotal(totalPage);
			}
			if (status != null) {
				resp.setStatus(status);
			}
			resp.setSuccess(true);
			final byte[] data = om.writeValueAsBytes(resp);
			String respStr = new String(data, ENCODING);
			LOG.debug("Response: " + respStr);
			return respStr;
		} catch (Exception e) {
			LOG.error("Failed to convert the marker objects into JSON format!", e);
			return formatFailureResponse(new StateResponseModel(), ResponseModel.SERVICE_FAILURE,
					ResponseModel.JSON_FORMAT_FAILURE + ": " + e.getMessage());
		}
	}

	protected String formatStatResponse(Object obj) {
		ObjectMapper om = new ObjectMapper();
		try {
			final byte[] data = om.writeValueAsBytes(obj);
			String respStr = new String(data, ENCODING);
			LOG.debug("StatResponse: " + respStr);
			return respStr;
		} catch (Exception e) {
			LOG.error("Failed to convert the marker objects into JSON format!", e);
			return formatFailureResponse(new StateResponseModel(), ResponseModel.SERVICE_FAILURE,
					ResponseModel.JSON_FORMAT_FAILURE + ": " + e.getMessage());
		}
	}

	protected Subject getSubject() {
		return SecurityUtils.getSubject();
	}

	protected User getCurrentUser() {
		Object principal = getSubject().getPrincipal();
		if (principal instanceof User) {
			LOG.debug("principal exist.");
			User user = (User) principal;
			LOG.info("UserModel in session: " + user.toString());
			return user;
		}
		LOG.debug("principal does not exist.");
		return new User();
	}


	protected <T> T transJson(String json, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);
		return mapper.readValue(json, clazz);
	}
	

	public <T> List<T> getObjectsFromJson(String in, Class<T> clsT) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		JsonParser parser = mapper.getJsonFactory().createJsonParser(in);
		JsonNode nodes = parser.readValueAsTree();
		List<T> list = new ArrayList<T>(nodes.size());
		for (JsonNode node : nodes) {
			//list.add(mapper.readValue(node, clsT));
		}
		return list;
	}

	protected String failure(Exception e) {
		return formatFailureResponse(new StateResponseModel(), ResponseModel.SERVICE_FAILURE, e.getMessage());
	}

	protected String failure(String msg) {
		return formatFailureResponse(new StateResponseModel(), ResponseModel.SERVICE_FAILURE, msg);
	}

	protected String failure(String errorCode, String msg) {
		return formatFailureResponse(new StateResponseModel(), errorCode, msg);
	}

	protected String success() {
		return formatResponse(new StateResponseModel(), null, ResponseModel.SERVICE_SUCCESS);
	}

	protected String success(ResponseModel<?> resp) {
		resp.setSuccess(true);
		return formatResponse(resp, null, ResponseModel.SERVICE_SUCCESS);
	}

	protected String success(String status) {
		return formatResponse(new StateResponseModel(), null, status);
	}

}
