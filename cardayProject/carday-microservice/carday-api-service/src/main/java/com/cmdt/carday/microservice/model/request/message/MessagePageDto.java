package com.cmdt.carday.microservice.model.request.message;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.cmdt.carrental.common.bean.messageCenter.MessageType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by zhengjun.jing on 6/7/2017.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "MessagePageDto", description = "公告消息查询信息")
public class MessagePageDto implements Serializable {

	private static final long serialVersionUID = -1755295844175968851L;

	@ApiModelProperty(value="登录用户ID",required=true)
	@NotNull(message = "userId can not be empty!")
	private Long userId;

	@ApiModelProperty("消息类型:system(系统消息), abnormal(异常消息), trip(行程消息),all(所有消息)")
	private MessageType messageType;

	@ApiModelProperty("分页，当前页码")
	@NotNull(message = "currentPage can not be empty!")
	@Min(1)
	private Integer currentPage;

	@ApiModelProperty("分页，每页条数")
	@NotNull(message = "numPerPage can not be empty!")
	@Min(1)
	private Integer numPerPage;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
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
