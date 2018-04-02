package com.cmdt.carrental.common.dao;

import java.util.List;

import com.cmdt.carrental.common.model.Area;

public interface AreaDao {
	public List<Area> findByParentId(Integer parentId);

}
