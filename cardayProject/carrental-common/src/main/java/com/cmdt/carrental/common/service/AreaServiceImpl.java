package com.cmdt.carrental.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carrental.common.dao.AreaDao;
import com.cmdt.carrental.common.model.Area;

@Service
public class AreaServiceImpl implements AreaService{
	@Autowired
	private AreaDao area;

	@Override
	public List<Area> findByParentId(Integer parentId) {
			return area.findByParentId(parentId);
	}

}
