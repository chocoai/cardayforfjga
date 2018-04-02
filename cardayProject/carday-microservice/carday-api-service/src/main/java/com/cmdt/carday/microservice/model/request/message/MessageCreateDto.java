package com.cmdt.carday.microservice.model.request.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import javax.validation.constraints.NotNull;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "MessageCreateDto", description = "创建公告消息")
public class MessageCreateDto implements Serializable{
	
	private static final long serialVersionUID = -6429191794175965092L;
	
	@ApiModelProperty(value="登录用户ID",required=true)
	@NotNull(message="userId不能为空")
    private Long userId;   //用户Id
	
	@ApiModelProperty(value="消息主题",required=true)
	@NotNull(message="title不能为空")
    private String title;			//消息标题
    
	@ApiModelProperty(value="消息内容",required=true)
	@NotNull(message="content不能为空")
    private String content;    		//消息内容

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
    
}
