package com.cmdt.carrental.platform.service.api.ow;



import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.UserModel;
import com.cmdt.carrental.platform.service.api.BaseApi;
import com.cmdt.carrental.platform.service.biz.service.PlatformUserService;
import com.cmdt.carrental.platform.service.model.request.user.UserChangePasswDto;
import com.cmdt.carrental.platform.service.model.request.user.UserCreateDto;
import com.cmdt.carrental.platform.service.model.request.user.UserDeleteDto;
import com.cmdt.carrental.platform.service.model.request.user.UserInfoDto;
import com.cmdt.carrental.platform.service.model.request.user.UserListDto;
import com.cmdt.carrental.platform.service.model.request.user.UserPageDto;
import com.cmdt.carrental.platform.service.model.request.user.UserUpdateDto;

@Produces(MediaType.APPLICATION_JSON)
public class UserApi extends BaseApi{
	
	private static final Logger LOG = LoggerFactory.getLogger(UserApi.class);
	
    @Autowired
    private PlatformUserService  platformUserService;
    
    
    /**
     * 根据realname 查询用户
     * @param userPageDto
     * @return
     */
    @POST
    @Path("/findAllAdmin")
    @Consumes(MediaType.APPLICATION_JSON)  
    public  PagModel findAllAdmin(@Valid @NotNull UserPageDto userPageDto){
    	
    	LOG.info("Enter UserApi findAllAdmin");
        return platformUserService.findAllAdminByRealName(userPageDto);
    }
    
    /**
     * 添加用户
     * @param userInfoDto
     * @return
     */
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)  
    public void create(@Valid @NotNull UserCreateDto userCreateDto){
        LOG.info("Enter UserApi create");
		platformUserService.createUser(userCreateDto);
    }
    
    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    @GET
    @Path("/{id}/findUserById")
    public UserModel findUserById(@PathParam("id") @NotNull Long id){
        LOG.info("Enter UserApi findUserById");
        return platformUserService.findUserModel(id);
		
    }
    
    /**
     * 修改用户信息
     * @param userInfoDto
     * @return
     */
    @POST
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)  
    public void update(@Valid @NotNull UserUpdateDto userUpdateDto){
        LOG.info("Enter UserApi update");
        platformUserService.update(userUpdateDto);
       
    }
    
    /**
     * 删除用户信息
     * @param userListDto
     * @return
     */
    @POST
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)  
    public void delete(@Valid @NotNull UserDeleteDto userDeleteDto){
        LOG.info("Enter UserApi delete");
        platformUserService.delete(super.getUserById(userDeleteDto.getUserId()),userDeleteDto);
        
    }
    
    /**
     * 修改密码
     * @param userChangePasswDto
     * @return
     */
    @POST
    @Path("/changePassword")
    @Consumes(MediaType.APPLICATION_JSON)  
    public void changePassword(@Valid @NotNull UserChangePasswDto userChangePasswDto){
        LOG.info("Enter UserApi changePassword");
        platformUserService.changePassword(userChangePasswDto);
        
    }
    
    /**
     * 加载当前user
     * param:userId
     * @return
     */
    @GET
    @Path("/loadCurrentUser/{userId}")
    public String loadCurrentUser(@PathParam("userId") @NotNull Long userId){
        LOG.info("Enter UserApi loadCurrentUser");
        return platformUserService.loadCurrentUser(super.getUserById(userId));
    }
    
    /**
     * 修改个人基本信息
     * @param userListDto
     * @return
     */
    @POST
    @Path("/changeUserInfo")
    @Consumes(MediaType.APPLICATION_JSON)  
    public void changeUserInfo(@Valid @NotNull UserListDto userListDto){
        LOG.info("Enter UserApi changeUserInfo");
        platformUserService.changeUserInfo(super.getUserById(userListDto.getUserId()),userListDto);
    }
    
    /**
     * 系统业务管理员，运营业务管理员修改企业管理员信息，
     * 每个企业只有一个管理员
     * @param userListDto
     * @return
     */
    @POST
    @Path("/modifyEnterAdmin")
    @Consumes(MediaType.APPLICATION_JSON)  
    public void modifyEnterAdmin(@Valid @NotNull UserInfoDto userInfoDto) {
        LOG.info("Enter UserApi modifyEnterAdmin");
        platformUserService.modifyEnterAdmin(userInfoDto);
    }
    
    /**
     * 查询企业下的用户
     * @param entId
     * @return
     */
    @GET
    @Path("/findEnterAdmin/{entId}")
    public User findEnterAdmin(@PathParam("entId") @NotNull Long entId){
        LOG.info("Enter UserApi findEnterAdmin");
        return platformUserService.findEnterAdmin(entId);
    }
}
