package com.cmdt.carday.microservice.api.portal;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cmdt.carday.microservice.api.BaseApi;
import com.cmdt.carday.microservice.biz.service.PlatformRuleService;
import com.cmdt.carday.microservice.model.request.rule.RuleAddressAddDto;
import com.cmdt.carday.microservice.model.request.rule.RuleAddressUpdateDto;
import com.cmdt.carday.microservice.model.request.rule.RuleBdDto;
import com.cmdt.carday.microservice.model.request.rule.RuleDto;
import com.cmdt.carday.microservice.model.request.rule.RulePageDto;
import com.cmdt.carday.microservice.model.request.rule.UserUnbindRuleDto;
import com.cmdt.carrental.common.entity.RuleAddress;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.RuleEditData;
import com.cmdt.carrental.common.model.VehicleRuleDisplayModel;
import com.cmdt.carrental.common.model.VehicleRuleGetOnAndOffDisplayModel;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;

@Api("/rule")
@SwaggerDefinition(info = @Info(contact = @Contact(name = "zhang ying",
        email = "ying.zhang@cm-dt.com"),
        title = "The rule api for rule-management", 
        version = "1.0.0"))
@Validated
@RestController
@RequestMapping("/rule")
public class RuleApi extends BaseApi {
	
	@Autowired
	PlatformRuleService platformRuleService;
	
	@ApiOperation(value = "根据位置名查询用车位置列表", response = PagModel.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @PostMapping(value = "/findLocationByLocationName")
    public PagModel list(
    		@ApiParam(value = "用车位置名称查询参数", required = true) @RequestBody @Valid @NotNull RulePageDto dto){
		return platformRuleService.findLocationByLocationName(getUserById(dto.getUserId()), dto);
    }
	
	@ApiOperation(value = "新增用车位置", response = RuleAddress.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @PostMapping(value = "/createAddress")
    public RuleAddress createAddress(
    		@ApiParam(value = "用车位置新增信息", required = true) @RequestBody @Valid @NotNull RuleAddressAddDto dto){
		return platformRuleService.createAddress(getUserById(dto.getUserId()), dto);
    }
	
	@ApiOperation(value = "修改用车位置", response = Boolean.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @PostMapping(value = "/updateAddress")
    public Boolean updateAddress(
    		@ApiParam(value = "用车位置修改信息", required = true) @RequestBody @Valid @NotNull RuleAddressUpdateDto dto){
		return platformRuleService.updateAddress(getUserById(dto.getUserId()), dto);
    }
	
	@ApiOperation(value = "删除用车位置", response = Boolean.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @DeleteMapping(value = "/deleteAddress/{id}")
    public Boolean deleteLocation(
    		@ApiParam(value = "用车位置id", required = true) @PathVariable("id")Long id) {
		platformRuleService.deleteLocation(id);
		return true;
    }
	
	@ApiOperation(value = "规则列表查询", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
	@GetMapping(value = "ruleList/{userId}")
    public List<VehicleRuleDisplayModel> getRuleListByOrgId(
    		@ApiParam(value = "登录用户id", required = true) @PathVariable("userId")Long userId) {
		return platformRuleService.getRuleListByOrgId(getUserById(userId));
    }
	
	@ApiOperation(value = "删除用车规则", response = Boolean.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @DeleteMapping(value = "/deleteRule/{userId}/{ruleId}")
    public Boolean removeRule(
    		@ApiParam(value = "登录用户id", required = true) @PathVariable("userId")Long userId,
    		@ApiParam(value = "用车规则id", required = true) @PathVariable("ruleId")Long ruleId) {
		platformRuleService.removeRule(getUserById(userId), ruleId);
		return true;
    }
	
	@ApiOperation(value = "新增用车规则", response = Boolean.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @PostMapping(value = "/createRule")
    public Boolean addRule(
    		@ApiParam(value = "用车规则新增信息", required = true)@RequestBody @Valid @NotNull RuleDto dto){
		platformRuleService.addRule(getUserById(dto.getUserId()), dto);
		return true;
    }
	
	@ApiOperation(value = "根据Id查询用车规则", response = RuleEditData.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @GetMapping(value = "/findRuleById/{userId}/{ruleId}")
    public RuleEditData findRuleById(
    		@ApiParam(value = "登录用户id", required = true) @PathVariable("userId")Long userId,
    		@ApiParam(value = "用车规则id", required = true) @PathVariable("ruleId")Long ruleId) {
		return platformRuleService.findRuleById(getUserById(userId), ruleId);
    }
	
	@ApiOperation(value = "修改用车规则", response = Boolean.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @PostMapping(value = "/updateRule")
    public Boolean updateRule(
    		@ApiParam(value = "用车规则修改信息", required = true) @RequestBody @Valid @NotNull RuleDto dto){
		platformRuleService.updateRule(getUserById(dto.getUserId()), dto);
		return true;
    }
	
	@ApiOperation(value = "上车位置查询", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @GetMapping(value = "/getOnAddressListForAdd/{userId}")
    public List<VehicleRuleGetOnAndOffDisplayModel> getOnAddressListForAdd(
    		@ApiParam(value = "登录用户id", required = true) @PathVariable("userId")Long userId) {
		return platformRuleService.getOnAddressListForAdd(getUserById(userId));
    }
	
	@ApiOperation(value = "下车位置查询", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @GetMapping(value = "/getOffAddressListForAdd/{userId}")
    public List<VehicleRuleGetOnAndOffDisplayModel> getOffAddressListForAdd(
    		@ApiParam(value = "登录用户id", required = true) @PathVariable("userId")Long userId) {
		return platformRuleService.getOffAddressListForAdd(getUserById(userId));
    }
	
	@ApiOperation(value = "上车位置查询(显示所有位置,已选中的上车位置已标注)", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @GetMapping(value = "/getOnAddressListForEdit/{userId}/{ruleId}")
    public List<VehicleRuleGetOnAndOffDisplayModel> getOnAddressListForEdit(
    		@ApiParam(value = "登录用户id", required = true) @PathVariable("userId")Long userId,
    		@ApiParam(value = "用车规则id", required = true) @PathVariable("ruleId")Long ruleId) {
		return platformRuleService.getOnAddressListForEdit(getUserById(userId), ruleId);
    }
	
	@ApiOperation(value = "下车位置查询(显示所有位置,已选中的上车位置已标注)", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @GetMapping(value = "/getOffAddressListForEdit/{userId}/{ruleId}")
    public List<VehicleRuleGetOnAndOffDisplayModel> getOffAddressListForEdit(
    		@ApiParam(value = "登录用户id", required = true) @PathVariable("userId")Long userId,
    		@ApiParam(value = "用车规则id", required = true) @PathVariable("ruleId")Long ruleId) {
		return platformRuleService.getOffAddressListForEdit(getUserById(userId), ruleId);
    }
	
	@ApiOperation(value = "规则列表查询(用户已绑定规则)", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @GetMapping(value = "/ruleBindList/{userId}")
    public List<VehicleRuleDisplayModel> ruleBindingList(
    		@ApiParam(value = "登录用户id", required = true) @PathVariable("userId")Long userId) {
		return platformRuleService.ruleBindingList(getUserById(userId));
    }
	
	@ApiOperation(value = "规则列表查询(用户未绑定规则)", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @GetMapping(value = "/ruleUnbindList/{userId}/{bindRuleUserId}")
    public List<VehicleRuleDisplayModel> ruleNotBindingList(
    		@ApiParam(value = "登录用户id", required = true) @PathVariable("userId")Long userId,
    		@ApiParam(value = "绑定规则用户id", required = true) @PathVariable("bindRuleUserId")Long bindRuleUserId) {
		return platformRuleService.ruleNotBindingList(getUserById(userId), bindRuleUserId);
    }
	
	@ApiOperation(value = "用户解绑规则", response = Boolean.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @PostMapping(value = "/userUnbindRule")
    public Boolean userUnbindRule(
    		@ApiParam(value = "用户解绑规则", required = true) @RequestBody @Valid @NotNull  UserUnbindRuleDto dto) {
		platformRuleService.userUnbindRule(getUserById(dto.getUserId()), dto);
		return true;
    }
	
	@ApiOperation(value = "用户绑定规则", response = Boolean.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @PostMapping(value = "/userBindRule")
    public Boolean userBindRule(
    		@ApiParam(value = "用户绑定规则参数", required = true) @RequestBody @Valid @NotNull  RuleBdDto dto) {
		platformRuleService.userBindRule(getUserById(dto.getUserId()), dto);
		return true;
    }
	
	@ApiOperation(value = "用车余额校验", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @GetMapping(value = "/balanceCheck/{userId}")
    public String balanceCheck(
    		@ApiParam(value = "登录用户id", required = true) @PathVariable("userId")Long userId) {
		return platformRuleService.balanceCheck(getUserById(userId));
    }
}
