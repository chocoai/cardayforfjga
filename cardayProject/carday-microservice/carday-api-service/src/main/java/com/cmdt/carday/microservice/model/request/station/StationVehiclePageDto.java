package com.cmdt.carday.microservice.model.request.station;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by zhengjun.jing on 6/6/2017.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StationVehiclePageDto {
	
	@ApiModelProperty(value="登录用户ID",required=true)
    @NotNull(message="userId不能为空")
    private Long userId;
    
	@ApiModelProperty(value="站点ID",required=true)
    @NotNull(message="stationId不能为空")
    private Long stationId;
    
    @ApiModelProperty(value="分页，当前页码",required=true)
    @NotNull(message="currentPage不能为空")
    @Min(value=1)
    private Integer currentPage;
    
    @ApiModelProperty(value="分页，每页条数",required=true)
    @NotNull(message="numPerPage不能为空")
    @Min(value=1)
    private Integer numPerPage;
    
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
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

}
