package com.cmdt.carrental.common.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.cmdt.carrental.common.model.PageJsonModel;
import com.cmdt.carrental.common.model.PagModel;

public class Pagination {

	public static final int NUMBERS_PER_PAGE = 10;
	// 一页显示的记录数
	private int numPerPage;
	// 记录总数
	private int totalRows;
	// 总页数
	private int totalPages;
	// 当前页码
	private int currentPage;
	// 起始行数
	private int startIndex;
	// 结束行数
	private int lastIndex;
	// 结果集存放List
	@SuppressWarnings("rawtypes")
	private List resultList;
	// JdbcTemplate jTemplate
	private JdbcTemplate jdbcTemplate;

	/**
	 * 每页显示10条记录的构造函数,使用该函数必须先给Pagination设置currentPage，jTemplate初值
	 * 
	 * @param sql
	 *            Oracle语句
	 */
	@SuppressWarnings({ "rawtypes" })
	public Pagination(String sql, Class clazz) {
		if (jdbcTemplate == null) {
			throw new IllegalArgumentException("JdbcTemplate is null,please initial it first. ");
		} else if (sql.equals("")) {
			throw new IllegalArgumentException("Sql is empty,please initial it first. ");
		}
		new Pagination(sql, currentPage, NUMBERS_PER_PAGE, clazz, jdbcTemplate);
	}

	/**
	 * 分页构造函数
	 * 
	 * @param sql
	 *            根据传入的sql语句得到一些基本分页信息
	 * @param currentPage
	 *            当前页
	 * @param numPerPage
	 *            每页记录数
	 * @param jTemplate
	 *            JdbcTemplate实例
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Pagination(String sql, int currentPage, int numPerPage, Class clazz, JdbcTemplate jdbcTemplate) {
		if (jdbcTemplate == null) {
			throw new IllegalArgumentException("JdbcTemplate is null,please initial it first. ");
		} else if (sql == null || sql.equals("")) {
			throw new IllegalArgumentException("Sql is empty,please initial it first. ");
		}
		// 设置每页显示记录数
		setNumPerPage(numPerPage);
		// 设置要显示的页数
		setCurrentPage(currentPage);
		// 计算总记录数
		StringBuffer totalSQL = new StringBuffer(" SELECT count(*) FROM ( ");
		totalSQL.append(sql);
		totalSQL.append(" ) totalTable ");
		// 给JdbcTemplate赋值
		setJdbcTemplate(jdbcTemplate);
		// 总记录数
		setTotalRows(getJdbcTemplate().queryForObject(totalSQL.toString(), Integer.class));
		// 计算总页数
		setTotalPages();
		// 计算起始行数
		setStartIndex();
		// 计算结束行数
		setLastIndex();

		// 装入结果集
		setResultList(getJdbcTemplate().query(getPOSTGRESQLPageSQL(sql, startIndex, numPerPage),
				new BeanPropertyRowMapper(clazz)));
	}

	/**
	 * 分页构造函数
	 * 
	 * @param sql
	 *            根据传入的sql语句得到一些基本分页信息
	 * @param currentPage
	 *            当前页
	 * @param numPerPage
	 *            每页记录数
	 * @param jTemplate
	 *            JdbcTemplate实例
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Pagination(String sql, int currentPage, int numPerPage, Class clazz, JdbcTemplate jdbcTemplate,
			Object... args) {
		if (jdbcTemplate == null) {
			throw new IllegalArgumentException("JdbcTemplate is null,please initial it first. ");
		} else if (sql == null || sql.equals("")) {
			throw new IllegalArgumentException("Sql is empty,please initial it first. ");
		}
		// 设置每页显示记录数
		setNumPerPage(numPerPage);
		// 设置要显示的页数
		setCurrentPage(currentPage);
		// 计算总记录数
		StringBuffer totalSQL = new StringBuffer(" SELECT count(*) FROM ( ");
		totalSQL.append(sql);
		totalSQL.append(" ) totalTable ");
		// 给JdbcTemplate赋值
		setJdbcTemplate(jdbcTemplate);
		// 总记录数
		setTotalRows(getJdbcTemplate().queryForObject(totalSQL.toString(), Integer.class, args));
		// 计算总页数
		setTotalPages();
		// 计算起始行数
		setStartIndex();
		// 计算结束行数
		setLastIndex();

		// 装入结果集
		setResultList(getJdbcTemplate().query(getPOSTGRESQLPageSQL(sql, startIndex, numPerPage),
				new BeanPropertyRowMapper(clazz), args));
	}
	
	
	/**
	 * Pagination for split SQL
	 * @param countsql
	 * @param querysql
	 * @param currentPage
	 * @param numPerPage
	 * @param clazz
	 * @param jdbcTemplate
	 * @param args
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Pagination(String countsql, String querysql, int currentPage, int numPerPage, Class clazz, JdbcTemplate jdbcTemplate,
			Object... args) {
		if (jdbcTemplate == null) {
			throw new IllegalArgumentException("JdbcTemplate is null,please initial it first. ");
		} else if (querysql == null || querysql.equals("")) {
			throw new IllegalArgumentException("Sql is empty,please initial it first. ");
		}
		// 设置每页显示记录数
		setNumPerPage(numPerPage);
		// 设置要显示的页数
		setCurrentPage(currentPage);
		
		// 计算总记录数
		if(StringUtils.isBlank(countsql)){
			countsql = querysql;
		}
		StringBuffer totalSQL = new StringBuffer(" SELECT count(*) FROM ( ");
		totalSQL.append(countsql);
		totalSQL.append(" ) totalTable ");
		
		// 给JdbcTemplate赋值
		setJdbcTemplate(jdbcTemplate);
		// 总记录数
		setTotalRows(getJdbcTemplate().queryForObject(totalSQL.toString(), Integer.class, args));
		// 计算总页数
		setTotalPages();
		// 计算起始行数
		setStartIndex();
		// 计算结束行数
		setLastIndex();

		// 装入结果集
		setResultList(getJdbcTemplate().query(getPOSTGRESQLPageSQL(querysql, startIndex, numPerPage),
				new BeanPropertyRowMapper(clazz), args));
	}
	
	

	/**
	 * 构建基于Json Node
	 * 
	 * @param jsonNode
	 * @param currentPage
	 * @param numPerPage
	 * @param clazz
	 */
	public Pagination(List<PageJsonModel> resultList, String currentPage, String numPerPage) {

		// 设置每页显示记录数
		setNumPerPage(new Integer(numPerPage));
		// 设置要显示的页数
		setCurrentPage(new Integer(currentPage));
		// 总记录数
		setTotalRows(resultList.size());
		// 计算总页数
		setTotalPages();
		// 计算起始行数
		setStartIndex();
		// 计算结束行数
		setLastIndex();
		// 装入结果集
		setResultList(getJsonPage(resultList, this.startIndex, new Integer(numPerPage)));

	}

	/**
	 * 获取分页Json
	 * 
	 * @param resultList
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	public List<PageJsonModel> getJsonPage(List<PageJsonModel> resultList, Integer startIndex, Integer pageSize) {
		List<PageJsonModel> jsonPage = new ArrayList<PageJsonModel>();
		int endIndex = startIndex + pageSize;
		endIndex = (endIndex <= resultList.size()) ? endIndex : resultList.size();
		for (; startIndex < endIndex; startIndex++) {
			jsonPage.add(resultList.get(startIndex));
		}
		return jsonPage;
	}

	/**
	 * 构造postgresql数据分页SQL
	 * 
	 * @param queryString
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	public String getPOSTGRESQLPageSQL(String queryString, Integer startIndex, Integer pageSize) {
		String result = "";
		if (null != startIndex && null != pageSize) {
			result = queryString + " limit " + pageSize + " offset " + startIndex;
		} else if (null != startIndex && null == pageSize) {
			result = queryString + " limit " + pageSize + " offset 0";
		} else {
			result = queryString;
		}
		return result;
	}

	/**
	 * 构造MySQL数据分页SQL
	 * 
	 * @param queryString
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	public String getMySQLPageSQL(String queryString, Integer startIndex, Integer pageSize) {
		String result = "";
		if (null != startIndex && null != pageSize) {
			result = queryString + " limit " + startIndex + "," + pageSize;
		} else if (null != startIndex && null == pageSize) {
			result = queryString + " limit " + startIndex;
		} else {
			result = queryString;
		}
		return result;
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

	@SuppressWarnings("rawtypes")
	public List getResultList() {
		return resultList;
	}

	@SuppressWarnings("rawtypes")
	public void setResultList(List resultList) {
		this.resultList = resultList;
	}

	public int getTotalPages() {
		return totalPages;
	}

	// 计算总页数
	public void setTotalPages() {
		if (totalRows % numPerPage == 0) {
			this.totalPages = totalRows / numPerPage;
		} else {
			this.totalPages = (totalRows / numPerPage) + 1;
		}
	}

	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex() {
		this.startIndex = (currentPage - 1) * numPerPage;
	}

	public int getLastIndex() {
		return lastIndex;
	}

	// 计算结束时候的索引
	public void setLastIndex() {
//		System.out.println("totalRows=" + totalRows);///////////
//		System.out.println("numPerPage=" + numPerPage);///////////
		if (totalRows < numPerPage) {
			this.lastIndex = totalRows;
		} else if ((totalRows % numPerPage == 0) || (totalRows % numPerPage != 0 && currentPage < totalPages)) {
			this.lastIndex = currentPage * numPerPage;
		} else if (totalRows % numPerPage != 0 && currentPage == totalPages) {// 最后一页
			this.lastIndex = totalRows;
		}
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public PagModel getResult() {
		PagModel pagModel = new PagModel();
		BeanUtils.copyProperties(this, pagModel);
		return pagModel;
	}
}
