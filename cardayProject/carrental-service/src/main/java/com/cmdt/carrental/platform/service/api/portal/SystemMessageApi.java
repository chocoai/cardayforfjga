package com.cmdt.carrental.platform.service.api.portal;

import com.cmdt.carrental.common.entity.Message;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.service.MessageService;
import com.cmdt.carrental.platform.service.api.BaseApi;
import com.cmdt.carrental.platform.service.biz.service.PlatformSystemMessageService;
import com.cmdt.carrental.platform.service.model.request.message.MessageCreateDto;
import com.cmdt.carrental.platform.service.model.request.message.MessagePageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
public class SystemMessageApi extends BaseApi {

    private static final Logger LOG = LoggerFactory.getLogger(SystemMessageApi.class);

    @Autowired
    private MessageService messageService;
    
    @Autowired
    private PlatformSystemMessageService platformSystemMessageService;


    /**
     * 根据消息类型获取消息列表
     *
     * @param messagePageDto
     * @return
     */
    @POST
    @Path("/list")
    @Consumes(MediaType.APPLICATION_JSON)  
    public PagModel showMessages(@Valid @NotNull MessagePageDto messagePageDto) {
        LOG.info("Enter SystemMessageApi showMessages");
        return platformSystemMessageService.getMessageByUser(super.getUserById(messagePageDto.getUserId()),messagePageDto);
       
    }

    /**
     * 企业管理员添加系统公告，以及推送公告到当前orgId下的所有用户
     *
     * @param messageCreateDto
     * @return
     */
    @POST
    @Path("/addSysMessage")
    @Consumes(MediaType.APPLICATION_JSON)  
    public void addSysMessage(MessageCreateDto messageCreateDto) {
        LOG.info("Enter SystemMessageApi addSysMessage");
        platformSystemMessageService.addSysMessage(super.getUserById(messageCreateDto.getUserId()),messageCreateDto);
    }


    /**
     * 根据用户Id信息，获取最新消息列表
     * @param userId
     * @return
     */
    @GET
    @Path("/{userId}/getLatestSystemMessage")
    public List<Message>  getLatestSystemMessage(@PathParam("userId")  @NotNull Long userId) {
        LOG.info("Enter SystemMessageApi getLatestSystemMessage");
        return messageService.getLatestSystemMessage(super.getUserById(userId).getOrganizationId());
    }
}
