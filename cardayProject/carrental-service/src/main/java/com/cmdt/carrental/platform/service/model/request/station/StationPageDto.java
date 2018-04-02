package com.cmdt.carrental.platform.service.model.request.station;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by zhengjun.jing on 6/6/2017.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StationPageDto {
    @NotNull(message="userId不能为空")
    private Long userId;
    
    @NotNull(message="stationName不能为空")
    private String stationName;
    
    @Digits(message="currentPage格式错误,最小:1,最大:"+ Integer.MAX_VALUE, fraction = 1, integer = Integer.MAX_VALUE)
    @NotNull(message="currentPage不能为空")
    private Integer currentPage;
    
    @Digits(message="numPerPage格式错误,最小1,最大:"+ Integer.MAX_VALUE, fraction = 1, integer = Integer.MAX_VALUE)
    @NotNull(message="numPerPage不能为空")
    private Integer numPerPage;
    
    private Long organizationId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getNumPerPage() {
        return numPerPage;
    }

    public void setNumPerPage(Integer numPerPage) {
        this.numPerPage = numPerPage;
    }

    
    public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	@Override
    public String toString() {
        return "StationPageDto{" +
                "userId=" + userId +
                ", stationName='" + stationName + '\'' +
                ", currentPage=" + currentPage +
                ", numPerPage=" + numPerPage +
                '}';
    }
}
