package com.cmdt.carrental.mobile.gateway.model;

import java.util.List;

public class MsgInfoModel {
	private Integer pageSize;
	private Integer currentPage;
	private String total;
	private String unread;
	private List<MsgDataModel> msgs;
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getUnread() {
		return unread;
	}
	public void setUnread(String unread) {
		this.unread = unread;
	}
	public List<MsgDataModel> getMsgs() {
		return msgs;
	}
	public void setMsgs(List<MsgDataModel> msgs) {
		this.msgs = msgs;
	}
	
	
}
