package com.cmdt.carrental.platform.service.model.request.message;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotNull;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageCreateDto {
	@NotNull(message="userId不能为空")
    private Long userId;   //用户Id
	
	@NotNull(message="title不能为空")
    private String title;			//消息标题
    
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
