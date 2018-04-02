package com.cmdt.carrental.common.model;

import java.util.List;


public class PagModel{
	
	 //记录总数
	  private int totalRows; 
	 //总页数
	  private int totalPages; 
	 //当前页码
	  private int currentPage;
	  //一页显示的记录数
	  private int numPerPage; 
	  //结果集存放List
	  private List resultList;
	  
	public int getTotalRows() {
		return totalRows;
	}
	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getNumPerPage() {
		return numPerPage;
	}
	public void setNumPerPage(int numPerPage) {
		this.numPerPage = numPerPage;
	}
	public List getResultList() {
		return resultList;
	}
	public void setResultList(List resultList) {
		this.resultList = resultList;
	}
	  
}
