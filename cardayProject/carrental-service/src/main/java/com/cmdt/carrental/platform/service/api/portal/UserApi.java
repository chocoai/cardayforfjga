package com.cmdt.carrental.platform.service.api.portal;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import com.cmdt.carrental.platform.service.common.WsResponse;
import com.cmdt.carrental.platform.service.model.request.user.*;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmdt.carrental.common.model.UserModel;
import com.cmdt.carrental.platform.service.api.BaseApi;
import com.cmdt.carrental.platform.service.biz.service.PlatformUserService;
import com.cmdt.carrental.platform.service.model.response.user.UserLoginRetDto;


@Produces(MediaType.APPLICATION_JSON)
public class UserApi extends BaseApi{
	
	private static final Logger LOG = LoggerFactory.getLogger(UserApi.class);
	
    @Autowired
    private PlatformUserService  platformUserService;
    
    /**
     * [超级管理员]查询所有租户
     * [租户管理员]查询企业管理员
     * @return
     */
    @POST
    @Path("/list")
    @Consumes(MediaType.APPLICATION_JSON)  
    public  List<UserModel> list(@Valid @NotNull UserListDto userListDto){
    	LOG.info("Enter UserApi list");
    	return platformUserService.listUser(super.getUserById(userListDto.getUserId()),userListDto);
    	
    }
    
    /**
     * 查询当前节点的企业管理员和部门管理员
     * param:orgId
     * @return
     */
    @GET
    @Path("/{orgId}/listOrgAdminListByOrgId")
    public List<UserModel> listOrgAdminListByOrgId(@PathParam("orgId")  @NotNull Long organizationId) {
        LOG.info("Enter UserApi listOrgAdminListByOrgId");
    	return  platformUserService.listOrgAdminListByOrgId(organizationId); 
        
    }
    
    
    /**
     * 根据登录名查询用户
     * @param name
     * @return
     */
    @GET
    @Path("/{name}/findByName")
    public UserModel findByName(@PathParam("name") @NotNull String name) {
    	LOG.info("Enter UserApi findByName");
    	return  platformUserService.findUserModelByName(name);
    }
    
    /**
     * 添加用户
     * @param userCreateDto
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
     * @param userUpdateDto
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
     * @param userDeleteDto
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
    public String loadCurrentUser(@PathParam("userId") @NotNull Long userId) throws Exception{
        LOG.info("Enter UserApi loadCurrentUser");
        return platformUserService.loadCurrentUser(super.getUserById(userId));
    }
    
    /**
     * [企业管理员,部门管理员]查询下级部门的部门管理员，员工，不包含司机
     * param:userListDto
     * @return
     */
    @POST
    @Path("/listDirectUserListByOrgId")
    @Consumes(MediaType.APPLICATION_JSON)  
    public List<UserModel> listDirectUserListByOrgId(@Valid @NotNull UserListDto userListDto){
        LOG.info("Enter UserApi listDirectUserListByOrgId");
        return platformUserService.listDirectUserListByOrgId(super.getUserById(userListDto.getUserId()),userListDto);
    }
    
    /**
     * [企业管理员]查询企业根节点用户，不包含企业管理员自己与司机
     * param:userId
     * @return
     */
    @GET
    @Path("/listEnterpriseRootNodeUserList/{userId}")
    public List<UserModel> listEnterpriseRootNodeUserList(@PathParam("userId") @NotNull Long userId) {
        LOG.info("Enter UserApi listEnterpriseRootNodeUserList");
        return platformUserService.listEnterpriseRootNodeUserList(super.getUserById(userId));
    }
    
    /**
     * [企业管理员]将部门管理员或员工移除到企业根节点
     * [部门管理员]将下级部门管理员或员工移除到企业根节点
     * param:userListDto
     * @return
     */
    @POST
    @Path("/removeUserToEnterpriseRootNode")
    @Consumes(MediaType.APPLICATION_JSON)  
    public void removeUserToEnterpriseRootNode(@Valid @NotNull UserListDto userListDto){
        LOG.info("Enter UserApi removeUserToEnterpriseRootNode");
        platformUserService.removeUserToEnterpriseRootNode(super.getUserById(userListDto.getUserId()),userListDto);
    }
    
    /**
     * [企业管理员]将部门管理员或员工更换组织
     * [部门管理员]将下级部门管理员或员工更换组织
     * param:userListDto
     * @return
     */
    @POST
    @Path("/batchChangeUserOrganization")
    @Consumes(MediaType.APPLICATION_JSON)  
    public void batchChangeUserOrganization(@Valid @NotNull UserListDto userListDto) throws Exception{
        LOG.info("Enter UserApi batchChangeUserOrganization");
        platformUserService.batchChangeUserOrganization(super.getUserById(userListDto.getUserId()),userListDto);
    }
    
    /**
     * 修改个人基本信息
     * @param userListDto
     * @return
     */
    @POST
    @Path("/changeUserInfo")
    @Consumes(MediaType.APPLICATION_JSON)  
    public void changeUserInfo(@Valid @NotNull UserListDto userListDto) throws Exception{
        LOG.info("Enter UserApi changeUserInfo");
        platformUserService.changeUserInfo(super.getUserById(userListDto.getUserId()),userListDto);
    }
    
    @POST
    @Path("/login")
	public UserLoginRetDto login(UserLoginDto dto){
    	return platformUserService.login(dto);
	}
    
    
    @POST
    @Path("/sendVerificationCode")
    @Consumes(MediaType.APPLICATION_JSON)
    public WsResponse sendVerificationCode( @NotNull VerificationCodeRequestDto request) {
        LOG.info("Enter UserApi sendVerificationCode");
        boolean result = platformUserService.sendVertificationCode(request);

        if (result) {
            return WsResponse.success(true);
        } else {
            return WsResponse.success(false);
        }

    }
    
    @POST
    @Path("/checkVerificationCode")
    @Consumes(MediaType.APPLICATION_JSON) 
    public WsResponse checkVerificationCode(@Valid @NotNull VerificationCodeDto dto){
    	Pair<Boolean, String> result =  platformUserService.checkVertificationCode(dto.getPhoneNumber(), dto.getCode());

        if (result.getLeft()) {
            return WsResponse.success();
        } else {
            return WsResponse.failure(result.getRight());
        }
    }
    
    @POST
    @Path("/resetPassword")
    @Consumes(MediaType.APPLICATION_JSON) 
    public WsResponse resetPassword(@Valid @NotNull ResetPasswordDto dto){
    	platformUserService.resetPassword(dto.getUsername(), dto.getPassword());

    	return WsResponse.success();
    }

    /**
     *
     * @param phone
     * @return
     */
    @GET
    @Path("/phone-validate")
//    @Consumes(MediaType.APPLICATION_JSON)
    public WsResponse validatePhone(@Valid @NotNull @QueryParam("phone") String phone){
        boolean exists = platformUserService.validatePhone(phone);

        if (exists) {
            return WsResponse.success(true);
        } else {
            return WsResponse.success(false);
        }
    }
}
