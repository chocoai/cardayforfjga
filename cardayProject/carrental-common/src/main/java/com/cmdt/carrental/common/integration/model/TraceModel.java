package com.cmdt.carrental.common.integration.model;

import java.util.List;

public class TraceModel {
	private Integer count;
	private List<TrackModel> list;
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public List<TrackModel> getList() {
		return list;
	}
	public void setList(List<TrackModel> list) {
		this.list = list;
	}
	@Override
	public String toString() {
		return "TraceModel [count=" + count + ", list=" + list + "]";
	}
	
}