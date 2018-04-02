package com.cmdt.carrental.portal.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.model.Area;
import com.cmdt.carrental.common.service.AreaService;
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.common.util.TypeUtils;
import com.cmdt.carrental.portal.web.bind.annotation.CurrentUser;

@Controller
@RequestMapping("/area")
public class AreaController {
	@Autowired
	private AreaService areaService;
	
	private static final Logger LOG = LoggerFactory.getLogger(AreaController.class);
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/queryAreaInfo", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> queryAreaInfo(@CurrentUser User loginUser, String json) {
		LOG.info("Inside AreaApi.queryAreaInfo.");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			Integer parentId = 100000;
			if(StringUtils.isNotEmpty(json)) {
				Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
				parentId = TypeUtils.obj2Integer(jsonMap.get("parentId"));
			}
			List<Area> list=areaService.findByParentId(parentId);
			map.put("data", list);
			map.put("status", "success");
		} catch (Exception e) {
			LOG.error("Area Controller queryAreaInfo by parentId error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}
}



	