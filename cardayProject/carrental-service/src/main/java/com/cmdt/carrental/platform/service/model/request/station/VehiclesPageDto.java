package com.cmdt.carrental.platform.service.model.request.station;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

/**
 * Created by zhengjun.jing on 6/6/2017.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VehiclesPageDto {
    @NotNull(message="userId不能为空")
    private Long userId;
    @Digits(message="currentPage格式错误,最小:1,最大"+ Integer.MAX_VALUE, fraction = 1, integer = Integer.MAX_VALUE)
    @NotNull(message="stationId不能为空")
    private String stationId;
    @Digits(message="currentPage格式错误,最小:1,最大"+ Integer.MAX_VALUE, fraction = 1, integer = Integer.MAX_VALUE)
    @NotNull
    private Integer currentPage;
    @Digits(message="numPerPage格式错误,最小1,最大"+ Integer.MAX_VALUE, fraction = 1, integer = Integer.MAX_VALUE)
    @NotNull
    private Integer numPerPage;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
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
