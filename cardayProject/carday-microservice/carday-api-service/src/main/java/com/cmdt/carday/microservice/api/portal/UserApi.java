package com.cmdt.carday.microservice.api.portal;

import com.cmdt.carday.microservice.api.BaseApi;
import com.cmdt.carday.microservice.biz.service.PlatformUserService;
import com.cmdt.carday.microservice.common.Constants;
import com.cmdt.carday.microservice.common.model.response.WsResponse;
import com.cmdt.carday.microservice.model.request.user.*;
import com.cmdt.carday.microservice.model.response.user.UserLoginRetDto;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carday.microservice.common.model.response.MessageCode;
import com.cmdt.carday.microservice.common.model.response.exception.ServiceException;
import com.cmdt.carrental.common.model.UserModel;
import io.swagger.annotations.*;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.websocket.server.PathParam;
import java.util.List;


@Api("/user")
@SwaggerDefinition(info = @Info(contact = @Contact(name = "Sen Li",
        email = "sen.li@cm-dt.com"),
        title = "The user api for user-management",
        version = Constants.Project_Version))
@Validated
@RestController
@RequestMapping("/user")
public class UserApi extends BaseApi {

    private static final Logger LOG = LogManager.getLogger(UserApi.class);

    @Autowired
    private PlatformUserService platformUserService;

    /**
     * [超级管理员]查询所有租户
     * [租户管理员]查询企业管理员
     *
     * @return
     */
//    @POST
//    @Path("/list")
//    @Consumes(MediaType.APPLICATION_JSON)
    public List<UserModel> list(@Valid @NotNull UserListDto userListDto) {
        LOG.info("Enter UserApi list");
        return platformUserService.listUser(super.getUserById(userListDto.getUserId()), userListDto);

    }

    /**
     * 查询当前节点的企业管理员和部门管理员
     * param:orgId
     *
     * @return
     */
//    @GET
//    @Path("/{orgId}/listOrgAdminListByOrgId")
    public List<UserModel> listOrgAdminListByOrgId(@PathParam("orgId") @NotNull Long organizationId) {
        LOG.info("Enter UserApi listOrgAdminListByOrgId");
        return platformUserService.listOrgAdminListByOrgId(organizationId);

    }


    /**
     * 根据登录名查询用户
     *
     * @param name
     * @return
     */
//    @GET
//    @Path("/{name}/findByName")
    public UserModel findByName(@PathParam("name") @NotNull String name) {
        LOG.info("Enter UserApi findByName");
        return platformUserService.findUserModelByName(name);
    }

    /**
     * 添加用户
     *
     * @param userCreateDto
     * @return
     */
    @ApiOperation(value = "创建用户.", response = User.class)
    @PostMapping(value = "/user")
    public User create(@ApiParam(value="用户信息实体",required=true)@RequestBody @Valid @NotNull UserCreateDto userCreateDto) {
        return platformUserService.createUser(userCreateDto);
    }

    /**
     * 根据id查询用户
     *
     * @param id
     * @return
     */
//    @GET
//    @Path("/{id}/findUserById")
    public UserModel findUserById(@PathParam("id") @NotNull Long id) {
        LOG.info("Enter UserApi findUserById");
        return platformUserService.findUserModel(id);
    }

    /**
     * 修改用户信息
     *
     * @param userUpdateDto
     * @return
     */
//    @POST
//    @Path("/update")
//    @Consumes(MediaType.APPLICATION_JSON)
    public void update(@Valid @NotNull UserUpdateDto userUpdateDto) {
        LOG.info("Enter UserApi update");
        platformUserService.update(userUpdateDto);
    }

    /**
     * 删除用户信息
     *
     * @param userDeleteDto
     * @return
     */
//    @POST
//    @Path("/delete")
//    @Consumes(MediaType.APPLICATION_JSON)
    public void delete(@Valid @NotNull UserDeleteDto userDeleteDto) {
        LOG.info("Enter UserApi delete");
        platformUserService.delete(super.getUserById(userDeleteDto.getUserId()), userDeleteDto);

    }

    /**
     * 修改密码
     *
     * @param userChangePasswDto
     * @return
     */
//    @POST
//    @Path("/changePassword")
//    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "修改密码.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @PostMapping(value = "/password", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void changePassword(@ApiParam(value = "修改密码信息", required = true)
                               @RequestBody @Valid @NotNull UserChangePasswDto userChangePasswDto) {
        platformUserService.changePassword(userChangePasswDto);
    }

    /**
     * 加载当前user
     * param:userId
     *
     * @return
     */
//    @GET
//    @Path("/loadCurrentUser/{userId}")
    public String loadCurrentUser(@PathParam("userId") @NotNull Long userId) throws Exception {
        LOG.info("Enter UserApi loadCurrentUser");
        return platformUserService.loadCurrentUser(super.getUserById(userId));
    }

    /**
     * [企业管理员,部门管理员]查询下级部门的部门管理员，员工，不包含司机
     * param:userListDto
     *
     * @return
     */
//    @POST
//    @Path("/listDirectUserListByOrgId")
//    @Consumes(MediaType.APPLICATION_JSON)
    public List<UserModel> listDirectUserListByOrgId(@Valid @NotNull UserListDto userListDto) {
        LOG.info("Enter UserApi listDirectUserListByOrgId");
        return platformUserService.listDirectUserListByOrgId(super.getUserById(userListDto.getUserId()), userListDto);
    }

    /**
     * [企业管理员]查询企业根节点用户，不包含企业管理员自己与司机
     * param:userId
     *
     * @return
     */
//    @GET
//    @Path("/listEnterpriseRootNodeUserList/{userId}")
    public List<UserModel> listEnterpriseRootNodeUserList(@PathParam("userId") @NotNull Long userId) {
        LOG.info("Enter UserApi listEnterpriseRootNodeUserList");
        return platformUserService.listEnterpriseRootNodeUserList(super.getUserById(userId));
    }

    /**
     * [企业管理员]将部门管理员或员工移除到企业根节点
     * [部门管理员]将下级部门管理员或员工移除到企业根节点
     * param:userListDto
     *
     * @return
     */
//    @POST
//    @Path("/removeUserToEnterpriseRootNode")
//    @Consumes(MediaType.APPLICATION_JSON)
    public void removeUserToEnterpriseRootNode(@Valid @NotNull UserListDto userListDto) {
        LOG.info("Enter UserApi removeUserToEnterpriseRootNode");
        platformUserService.removeUserToEnterpriseRootNode(super.getUserById(userListDto.getUserId()), userListDto);
    }

    /**
     * [企业管理员]将部门管理员或员工更换组织
     * [部门管理员]将下级部门管理员或员工更换组织
     * param:userListDto
     *
     * @return
     */
//    @POST
//    @Path("/batchChangeUserOrganization")
//    @Consumes(MediaType.APPLICATION_JSON)
    public void batchChangeUserOrganization(@Valid @NotNull UserListDto userListDto) throws Exception {
        LOG.info("Enter UserApi batchChangeUserOrganization");
        platformUserService.batchChangeUserOrganization(super.getUserById(userListDto.getUserId()), userListDto);
    }

    /**
     * 修改个人基本信息
     *
     * @return
     */
    @ApiOperation(value = "个人信息修改", response = Boolean.class)
    @PostMapping(value = "/changeUserInfo", consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean changeUserInfo(@ApiParam(value="个人信息修改参数",required=true) 
    		@RequestBody @Valid @NotNull UserInfoChangeDto userInfoChangeDto) throws Exception {
        LOG.info("Enter UserApi changeUserInfo");
        platformUserService.changeUserInfo(super.getUserById(userInfoChangeDto.getUserId()), userInfoChangeDto);
        return true;
    }


    @ApiOperation(value = "用户登录", response = UserLoginRetDto.class)
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public WsResponse<UserLoginRetDto> login(@ApiParam(value = "用户登录信息", required = true) @RequestBody UserLoginDto dto) {
        Pair<UserLoginRetDto, String> result = platformUserService.login(dto);

        if (result.getLeft() != null) {
            return WsResponse.success(result.getLeft());
        } else {
            return WsResponse.failure(result.getRight());
        }
    }


    @ApiOperation(value = "请求发送手机验证码.", response = User.class)
    @PostMapping(value = "/sendVerificationCode", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public WsResponse sendVerificationCode(@ApiParam(value = "用户手机信息", required = true)
                                           @NotNull @Valid @RequestBody VerificationCodeRequestDto request) {
    	boolean result = platformUserService.sendVertificationCode(request);
        if (result) {
            return WsResponse.success();
        } else {
            throw new ServiceException(MessageCode.COMMON_FAILURE);
        }
    }

    @ApiOperation(value = "手机验证码验证.", response = WsResponse.class)
    @PostMapping(value = "/checkVerificationCode", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public WsResponse checkVerificationCode(@ApiParam(value = "验证码信息", required = true)
                                            @RequestBody @Valid @NotNull VerificationCodeDto dto) {
        Pair<Boolean, String> result = platformUserService.checkVertificationCode(dto.getPhoneNumber(), dto.getCode());

        if (result.getLeft()) {
            return WsResponse.success();
        } else {
            return WsResponse.failure(result.getRight());
        }
    }


    @ApiOperation(value = "重置密码.", response = WsResponse.class)
    @PostMapping(value = "/resetPassword", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public WsResponse resetPassword(@ApiParam(value = "重置密码信息", required = true)
                                    @RequestBody @Valid @NotNull ResetPasswordDto dto) {
        Pair<Boolean, String> result = platformUserService.resetPassword(dto.getUsername(), dto.getPassword());

        if (result.getLeft()) {
            return WsResponse.success();
        } else {
            return WsResponse.failure(result.getRight());
        }
    }

    /**
     * @param phone
     * @return
     */
    @ApiOperation(value = "手机号校验.", response = Boolean.class)
    @GetMapping(value = "/phone-validate", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean validatePhone(@Valid @NotNull @RequestParam("phone") String phone) {
        if (platformUserService.validatePhone(phone)) {
            return true;
        } else {
            throw new ServiceException(MessageCode.USER_INVALIDATE_PHONE);
        }
    }
}
