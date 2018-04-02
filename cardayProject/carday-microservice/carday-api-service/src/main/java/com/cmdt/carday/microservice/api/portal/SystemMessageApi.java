package com.cmdt.carday.microservice.api.portal;

import static com.cmdt.carday.microservice.common.Constants.Project_Version;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cmdt.carday.microservice.api.BaseApi;
import com.cmdt.carday.microservice.biz.service.PlatformSystemMessageService;
import com.cmdt.carday.microservice.model.request.message.MessageCreateDto;
import com.cmdt.carday.microservice.model.request.message.MessagePageAppDto;
import com.cmdt.carday.microservice.model.request.message.MessagePageDto;
import com.cmdt.carday.microservice.model.response.message.MsgCountModel;
import com.cmdt.carday.microservice.model.response.message.MsgInfoModel;
import com.cmdt.carrental.common.entity.Message;
import com.cmdt.carrental.common.model.PagModel;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;

@Api("/systemMessage")
@SwaggerDefinition(info = @Info(contact = @Contact(name = "Hao Lin", email = "andy_linhao@outlook.com"), title = "The systemMessage api for systemMessage-management", version = Project_Version))
@Validated
@RestController
@RequestMapping("/systemMessage")
public class SystemMessageApi extends BaseApi {

	@Autowired
	private PlatformSystemMessageService platformSystemMessageService;

	/**
	 * 根据消息类型获取消息列表
	 *
	 * @param messagePageDto
	 * @return
	 */
	@ApiOperation(value = "公告列表", notes = "根据消息类型获取消息列表")
	@PostMapping(path = "/message/list",consumes=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public PagModel showMessages(
			@ApiParam(value = "消息查询参数", required = true) @RequestBody @Valid @NotNull MessagePageDto messagePageDto) {
		return platformSystemMessageService.getMessageByUser(super.getUserById(messagePageDto.getUserId()),
				messagePageDto);

	}

	@ApiOperation(value = "查看具体消息", notes = "根据消息Id查询消息")
	@GetMapping(path = "/message/{messageId}")
	public Message queryMessageById(
			@ApiParam(value = "messageId", required = true) @PathVariable("messageId") Long messageId) {
		return platformSystemMessageService.queryMessageById(messageId);
	}
	
	
	/**
	 * 企业管理员添加系统公告，以及推送公告到当前orgId下的所有用户
	 *
	 * @param messageCreateDto
	 * @return
	 */
	@ApiOperation(value = "添加系统公告")
	@PostMapping(path = "/message",consumes=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Boolean addSysMessage(
			@ApiParam(value = " 创建消息参数", required = true) @RequestBody @Valid MessageCreateDto messageCreateDto) {
		platformSystemMessageService.addSysMessage(super.getUserById(messageCreateDto.getUserId()), messageCreateDto);
		return true;
	}

	/**
	 * 根据机构Id，获取最新三条系统消息
	 * 
	 * @param userId
	 * @return
	 */
	@ApiOperation(value = "最新系统公告", notes = "根据机构Id查找该机构下最新的3条公告消息")
	@GetMapping(path = "/message/{deptId}")
	public List<Message> getLatestSystemMessage(
			@ApiParam(value = "deptId", required = true) @PathVariable("deptId") Long deptId) {
		return platformSystemMessageService.getLatestSystemMessage(deptId);
	}

	/**
	 * 手机app-----消息中心
	 * 
	 * @param app
	 * @param messageDto
	 * @return
	 */
	@ApiOperation(value = "消息中心列表 ", notes = "分页显示消息中心列表")
	@PostMapping(path = "/message/app",consumes=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public MsgInfoModel loadMsgs(@ApiParam(value = "消息查询实体", required = true) @RequestBody @Valid MessagePageAppDto messageDto) {
		return platformSystemMessageService.loadMsgInfoByUser(messageDto.getUserId(), messageDto.getMessageType(),
				messageDto.getCurrentPage(), messageDto.getNumPerPage(), messageDto.getApp());
	}
	
	
	/**
	 * 手机app-----根据消息类型分别统计未读数量
	 * 
	 * @param userId
	 * @param app
	 * @return
	 */

	@ApiOperation(value = "未读数量统计 ", notes = "消息类型分组统计未读数量")
	@PostMapping(path = "/message/group",consumes=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public MsgCountModel getMsgCount(@ApiParam(value = "登录用户ID", required = true) @RequestParam("userId") Long userId,
			@ApiParam(value = "app类型", required = true, allowableValues = "CARDAY.admin,CARDAY.enduser,CARDAY.drive")@RequestParam("app") String app) {
		return platformSystemMessageService.loadMsgCountByUser(userId, app);
	}

	/**
	 * 设置消息为已读
	 * 
	 * @param userId
	 * @param msgId
	 * @return
	 */
	@ApiOperation(value = "消息置为已读 ")
	@GetMapping(path = "/message")
	public Boolean setMsgAsRead(@ApiParam(value = "登录用户ID", required = true) @RequestParam("userId") Long userId,
			@ApiParam(value = "消息Id", required = true) @RequestParam("messageId") Long msgId) {
		return platformSystemMessageService.setMsgAsRead(userId, msgId);
	}
	
	
}
