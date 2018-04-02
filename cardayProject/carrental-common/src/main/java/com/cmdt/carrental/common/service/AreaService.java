package com.cmdt.carrental.common.service;

import java.util.List;

import com.cmdt.carrental.common.model.Area;

public interface AreaService {

	List<Area> findByParentId(Integer parentId);

}
