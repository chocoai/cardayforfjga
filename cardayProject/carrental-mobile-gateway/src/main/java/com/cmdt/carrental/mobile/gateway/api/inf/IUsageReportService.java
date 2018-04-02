package com.cmdt.carrental.mobile.gateway.api.inf;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.cmdt.carrental.mobile.gateway.model.UsageReportVehiclePropertyQueryDto;
import com.cmdt.carrental.mobile.gateway.model.request.usage.UsageDepartmentReportDto;


@Produces(MediaType.APPLICATION_JSON)
public interface IUsageReportService {
	
	
	/**
	 * 部门统计图(柱形图展示)
	 */
	public Response getDepartmentColumnarChartByDayRange(@Valid @NotNull UsageDepartmentReportDto dto);
	
	/**
	 * 用车情况(list展示)
	 * @param dto
	 * @return
	 */
	public Response getVehiclePropertyDataByDayRangeAndDeptId(@Valid @NotNull UsageReportVehiclePropertyQueryDto dto);
	
	/**
	 * 整体走势图(曲线图展示)
	 */
	public  Response getTendencyChartByDayRange(@Valid @NotNull UsageDepartmentReportDto dto);

}
