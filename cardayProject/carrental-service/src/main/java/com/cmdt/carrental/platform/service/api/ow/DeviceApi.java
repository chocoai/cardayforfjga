package com.cmdt.carrental.platform.service.api.ow;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.platform.service.api.BaseApi;
import com.cmdt.carrental.platform.service.biz.service.PlatformDeviceService;
import com.cmdt.carrental.platform.service.common.Constants;
import com.cmdt.carrental.platform.service.exception.ServerException;
import com.cmdt.carrental.platform.service.model.request.device.DeviceCreateDto;
import com.cmdt.carrental.platform.service.model.request.device.DeviceLicenseActiveDto;
import com.cmdt.carrental.platform.service.model.request.device.DeviceLicenseBindDto;
import com.cmdt.carrental.platform.service.model.request.device.DeviceLicenseReactiveDto;
import com.cmdt.carrental.platform.service.model.request.device.DeviceLicenseSuspendDto;
import com.cmdt.carrental.platform.service.model.request.device.DeviceListDto;
import com.cmdt.carrental.platform.service.model.request.device.DeviceUpdateDto;
import com.cmdt.carrental.platform.service.model.request.device.DeviceLicenseTerminatedDto;
import com.cmdt.carrental.platform.service.model.request.device.DeviceLicenseUnbinddDto;

@Produces(MediaType.APPLICATION_JSON)
public class DeviceApi extends BaseApi{
	private static final Logger LOG = LoggerFactory.getLogger(DeviceApi.class);
	
	@Autowired
	PlatformDeviceService platformDeviceService;
	
	/**
     * [系统管理员]查询设备列表
     */
	@POST
	@Path("/list")
    public PagModel deviceList(DeviceListDto deviceListDto) {
    	try{
    		return platformDeviceService.deviceList(getUserById(deviceListDto.getUserId()), deviceListDto);
    	}catch(Exception e) {
    		LOG.error("DeviceController deviceList error!", e);
    		throw new ServerException(Constants.API_MESSAGE_FAILURE);
    	}
	}
	
	/**
     * [系统管理员]添加设备
     */
	@POST
	@Path("/create")
    public String create(DeviceCreateDto deviceCreateDto) {
    	try{
    		return platformDeviceService.create(getUserById(deviceCreateDto.getUerId()), deviceCreateDto);
    	}catch(Exception e) {
    		LOG.error("DeviceController create error!", e);
    		throw new ServerException(Constants.API_MESSAGE_FAILURE);
    	}
	}
	
	/**
     * [系统管理员]更新设备
     */
	@POST
	@Path("/update")
    public String update(DeviceUpdateDto deviceUpdateDto) {
    	try{
    		return platformDeviceService.update(getUserById(deviceUpdateDto.getUerId()), deviceUpdateDto);
    	}catch(Exception e) {
    		LOG.error("DeviceController update error!", e);
    		throw new ServerException(Constants.API_MESSAGE_FAILURE);
    	}
	}
	
	/**
     * [系统管理员]License绑定
     * @param 
     * @return
     */
	@POST
	@Path("/licenseBind")
    public String licenseBind(DeviceLicenseBindDto deviceLicenseBindDto) {
    	try{
    		return platformDeviceService.licenseBind(deviceLicenseBindDto);
    	}catch(Exception e) {
    		LOG.error("DeviceController update error!", e);
    		throw new ServerException(Constants.API_MESSAGE_FAILURE);
    	}
	}
	
	/**
     * [系统管理员]License激活
     * @param 
     * @return
     */
	@POST
	@Path("/licenseActive")
    public String licenseActive(DeviceLicenseActiveDto deviceLicenseActiveDto) {
    	try{
    		return platformDeviceService.licenseActive(deviceLicenseActiveDto);
    	}catch(Exception e) {
    		LOG.error("licenseActive update error!", e);
    		throw new ServerException(Constants.API_MESSAGE_FAILURE);
    	}
	}
	
	/**
     * [系统管理员]License挂起
     * @param 
     * @return
     */
	@POST
	@Path("/licenseSuspend")
    public String licenseSuspend(DeviceLicenseSuspendDto deviceLicenseSuspendDto) {
    	try{
    		return platformDeviceService.licenseSuspend(deviceLicenseSuspendDto);
    	}catch(Exception e) {
    		LOG.error("licenseSuspend update error!", e);
    		throw new ServerException(Constants.API_MESSAGE_FAILURE);
    	}
	}
	
	/**
     * [系统管理员]License重新激活
     * @param 
     * @return
     */
	@POST
	@Path("/licenseReactive")
    public String licenseReactive(DeviceLicenseReactiveDto deviceLicenseReactiveDto) {
    	try{
    		return platformDeviceService.licenseReactive(deviceLicenseReactiveDto);
    	}catch(Exception e) {
    		LOG.error("licenseReactive update error!", e);
    		throw new ServerException(Constants.API_MESSAGE_FAILURE);
    	}
	}
	
	/**
     * [系统管理员]License终止
     * @param 
     * @return
     */
	@POST
	@Path("/licenseTerminated")
    public String licenseTerminated(DeviceLicenseTerminatedDto devicelicenseTerminatedDto) {
    	try{
    		return platformDeviceService.licenseTerminated(devicelicenseTerminatedDto);
    	}catch(Exception e) {
    		LOG.error("licenseReactive update error!", e);
    		throw new ServerException(Constants.API_MESSAGE_FAILURE);
    	}
	}
	
	/**
     * [系统管理员]License解绑
     * @param 
     * @return
     */
	@POST
	@Path("/licenseUnbind")
    public String licenseUnbind(DeviceLicenseUnbinddDto deviceLicenseUnbinddDto) {
    	try{
    		return platformDeviceService.licenseUnbind(deviceLicenseUnbinddDto);
    	}catch(Exception e) {
    		LOG.error("licenseUnbind update error!", e);
    		throw new ServerException(Constants.API_MESSAGE_FAILURE);
    	}
	}
}
