package com.cmdt.carday.microservice.biz.service;

import com.cmdt.carday.microservice.common.Constants;
import com.cmdt.carday.microservice.exception.ServerException;
import com.cmdt.carday.microservice.model.request.user.*;
import com.cmdt.carday.microservice.model.response.user.UserLoginRetDto;
import com.cmdt.carrental.common.dao.UserDao;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.PhoneVerificationCode;
import com.cmdt.carrental.common.entity.Resource;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carday.microservice.common.model.response.MessageCode;
import com.cmdt.carday.microservice.common.model.response.exception.ServiceException;
import com.cmdt.carrental.common.integration.CommunicationService;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.ResponseModel;
import com.cmdt.carrental.common.model.StateResponseModel;
import com.cmdt.carrental.common.model.UserModel;
import com.cmdt.carrental.common.service.OrganizationService;
import com.cmdt.carrental.common.service.PasswordHelper;
import com.cmdt.carrental.common.service.ResourceService;
import com.cmdt.carrental.common.service.UserService;
import com.cmdt.carrental.common.util.DateUtils;
import com.cmdt.carrental.common.util.JsonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PlatformUserService {

    private static final Logger LOG = LoggerFactory.getLogger(PlatformUserService.class);

    private static final String ENCODING = "UTF-8";

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private PasswordHelper passwordHelper;

    @Autowired
    private CommunicationService commService;

    @Autowired
    private ResourceService resourceService;


    public List<UserModel> listUser(User loginUser, UserListDto userListDto) {
        try {
            List<UserModel> users = new ArrayList<UserModel>();
            if (loginUser.isSuperAdmin()) {
                if (StringUtils.isBlank(userListDto.getRealname()))
                    users = this.userService.findAll();
                else {
                    users = this.userService.findAllMatchRealname(userListDto.getRealname());
                }

            }

            if (loginUser.isRentAdmin()) {
                if (StringUtils.isBlank(userListDto.getRealname()))
                    users = this.userService.listEnterpriseAdminListByRentId(loginUser.getOrganizationId());
                else {
                    users = this.userService.listEnterpriseAdminListByRentIdMatchRealname(loginUser.getOrganizationId(),
                            userListDto.getRealname());
                }
            }
            return users;
        } catch (Exception e) {
            LOG.error("PlatformUserService list error, cause by: ", e);
            throw new ServerException(Constants.API_MESSAGE_FAILURE);
        }
    }

    public List<UserModel> listOrgAdminListByOrgId(Long orgId) {
        try {
            return this.userService.listOrgAdminListByOrgId(orgId);
        } catch (Exception e) {
            LOG.error("PlatformUserService listOrgAdminListByOrgId error, cause by: ", e);
            throw new ServerException(Constants.API_MESSAGE_FAILURE);
        }

    }

    public UserModel findUserModelByName(String name) {
        try {
            return this.userService.findUserModel(name);
        } catch (Exception e) {
            LOG.error("PlatformUserService findUserModelByName error, cause by: ", e);
            throw new ServerException(Constants.API_MESSAGE_FAILURE);
        }
    }

    public User createUser(UserCreateDto userCreateDto) {
        try {
            User user = new User();
            BeanUtils.copyProperties(user, userCreateDto);

            if (!this.userService.usernameIsValid(user.getUsername())) {
                throw new ServerException(Constants.API_ERROR_MSG_USERNAME_EXIST);
            }

            if (!this.userService.checkPhoneNumRule(user.getPhone())) {
                throw new ServerException(Constants.API_ERROR_MSG_PHONE_NOT_MATCHED);
            }

            if (!this.userService.phoneIsValid(user.getPhone())) {
                throw new ServerException(Constants.API_ERROR_MSG_PHONE_EXIST);
            }

            if (!this.userService.emialIsValid(user.getEmail())) {
                throw new ServerException(Constants.API_ERROR_MSG_EMAIL_EXIST);
            }

            user = this.userService.createUser(user);
            if (user == null) {
                throw new ServerException(Constants.API_ERROR_MSG_CREATEUSER_FAILED);
            }
            return user;
        } catch (ServerException e) {
            throw e;
        } catch (Exception e) {
            LOG.error("PlatformUserService createUser error, cause by: ", e);
            throw new ServerException(Constants.API_MESSAGE_FAILURE);
        }

    }

    public UserModel findUserModel(Long id) {
        try {
            UserModel user = this.userService.findUserModel(id);
            if (Long.valueOf(1L).equals(user.getUserCategory())) {
                user = this.userService.findEntUserModelById(id);
            }
            return user;
        } catch (Exception e) {
            LOG.error("PlatformUserService findUserModel error, cause by: ", e);
            throw new ServerException(Constants.API_MESSAGE_FAILURE);
        }
    }

    public void update(UserUpdateDto userUpdateDto) {
        try {
            User user = this.userService.findById(userUpdateDto.getId());
            if (user != null) {
                String currentPhone = user.getPhone();
                String currentEmail = user.getEmail();

                String phone = userUpdateDto.getPhone();
                String email = userUpdateDto.getEmail();

                if (phone != null) {
                    if (!this.userService.checkPhoneNumRule(user.getPhone())) {
                        throw new ServerException(Constants.API_ERROR_MSG_PHONE_NOT_MATCHED);
                    }

                    if (!phone.equals(currentPhone)) {
                        if (this.userService.phoneIsValid(phone))
                            user.setPhone(phone);
                        else {
                            throw new ServerException(Constants.API_ERROR_MSG_PHONE_EXIST);
                        }
                    }
                }

                if (email != null) {
                    if (!email.equals(currentEmail)) {
                        if (this.userService.emialIsValid(email))
                            user.setEmail(email);
                        else {
                            throw new ServerException(Constants.API_ERROR_MSG_EMAIL_EXIST);
                        }
                    }
                }
                if (userUpdateDto.getRoleId() != null) {
                    user.setRoleId(userUpdateDto.getRoleId());
                }
                if (userUpdateDto.getOrganizationId() != null) {
                    user.setOrganizationId(userUpdateDto.getOrganizationId());
                }
                if (userUpdateDto.getRealname() != null) {
                    user.setRealname(userUpdateDto.getRealname());
                }

                this.userService.updateUser(user);
            } else {
                throw new ServerException(Constants.API_ERROR_MSG_USER_NOT_EXIST);
            }

        } catch (ServerException e) {
            throw e;
        } catch (Exception e) {
            LOG.error("PlatformUserService update error, cause by: ", e);
            throw new ServerException(Constants.API_MESSAGE_FAILURE);
        }
    }

    public void delete(User loginUser, UserDeleteDto userDeleteDto) {
        try {
            if (loginUser.getId().longValue() != userDeleteDto.getId().longValue()) {//不能删除自己
                //超级管理员
                if (loginUser.isSuperAdmin()) {
                    userService.deleteUser(userDeleteDto.getId());
                    return;
                }

                User delUser = userService.findOne(userDeleteDto.getId());
                //租户管理员
                if (loginUser.isRentAdmin()) {
                    if (!delUser.isSuperAdmin()) {
                        userService.deleteUser(userDeleteDto.getId());
                    } else {
                        throw new ServerException(Constants.API_MESSAGE_FAILURE);
                    }
                }
                //管理员
                if (loginUser.isAdmin()) {
                    if (!(delUser.isSuperAdmin() || delUser.isRentAdmin())) {
                        userService.deleteUser(userDeleteDto.getId());
                    } else {
                        throw new ServerException(Constants.API_MESSAGE_FAILURE);
                    }
                }
            } else {
                throw new ServerException(Constants.API_MESSAGE_FAILURE);
            }
        } catch (ServerException e) {
            throw e;
        } catch (Exception e) {
            LOG.error("PlatformUserService delete error, cause by: ", e);
            throw new ServerException(Constants.API_MESSAGE_FAILURE);
        }
    }


    public void changePassword(UserChangePasswDto userChangePasswDto) {
        User loginUser = userService.findOne(userChangePasswDto.getUserId());
        //验证旧密码
        String oldPassword = userChangePasswDto.getOldPassword();
        String newPassword = userChangePasswDto.getNewPassword();
        if (userService.isValidPassword(oldPassword, loginUser)) {
            userService.changePassword(loginUser.getId(), newPassword);
        } else {
            throw new ServiceException(MessageCode.OLD_PASSWORD_ERROR);
        }
    }

    public List<UserModel> listDirectUserListByOrgId(User loginUser, UserListDto userListDto) {
        try {
            //管理员
            if (userListDto.getOrganizationId() != null) {
                if (loginUser.isSuperAdmin() || loginUser.isEntAdmin()) {
                    return userService.listDirectUserListByOrgId(userListDto.getOrganizationId());
                }
            }
            return null;
        } catch (Exception e) {
            LOG.error("PlatformUserService changePassword error, cause by: ", e);
            throw new ServerException(Constants.API_MESSAGE_FAILURE);
        }
    }

    public String loadCurrentUser(User loginUser) {
        try {
            ResponseModel<User> resp = new ResponseModel<User>();
            if (loginUser != null && StringUtils.isNotEmpty(loginUser.getUsername())) {
                resp.setData(loginUser);
                return success(resp);
            } else {
                return failure("Can't find current user info!");
            }
        } catch (Exception e) {
            LOG.error("PlatformUserService loadCurrentUser error, cause by: ", e);
            throw new ServerException(Constants.API_MESSAGE_FAILURE);
        }
    }

    public List<UserModel> listEnterpriseRootNodeUserList(User loginUser) {
        try {
            //企业管理员
            if (loginUser.isEntAdmin()) {
                return userService.listEnterpriseRootNodeUserListByEntId(loginUser.getOrganizationId(), loginUser.getId());
            }
            return null;
        } catch (Exception e) {
            LOG.error("PlatformUserService listEnterpriseRootNodeUserList error, cause by: ", e);
            throw new ServerException(Constants.API_MESSAGE_FAILURE);
        }
    }

    public void removeUserToEnterpriseRootNode(User loginUser, UserListDto userListDto) {
        try {
            if (loginUser.isAdmin() && userListDto.getOrganizationId() != null) {
                Long entId = organizationService.findEntIdByOrgId(userListDto.getOrganizationId());
                if (entId != 0) {
                    userService.removeUserToEnterpriseRootNode(userListDto.getUserId(), entId);
                }
            }
        } catch (Exception e) {
            LOG.error("PlatformUserService removeUserToEnterpriseRootNode error, cause by: ", e);
            throw new ServerException(Constants.API_MESSAGE_FAILURE);
        }

    }

    public void batchChangeUserOrganization(User loginUser, UserListDto userListDto) {

        if (loginUser.isAdmin()) {
            Long orgId = userListDto.getOrganizationId();
            String userIds = userListDto.getUserIds();
            if (StringUtils.isNotBlank(userIds)) {
                String[] userIds_arr = StringUtils.split(userIds, ",");
                if (userIds_arr == null) {//只更换一个用户的组织
                    userService.changeUserOrganization(orgId, Long.valueOf(userIds));
                } else {//批量更换组织
                    userService.batchChangeUserOrganization(orgId, userIds_arr);
                }
            }
        }
    }


    public void changeUserInfo(User loginUser, UserInfoChangeDto userInfoChangeDto) {
            String currentPhone = loginUser.getPhone();
            String currentEmail = loginUser.getEmail();

            UserModel user = userService.findUserModel(loginUser.getId());

            String phone = userInfoChangeDto.getPhone();
            String email = userInfoChangeDto.getEmail();
            String realname = userInfoChangeDto.getRealname() != null ? userInfoChangeDto.getRealname() : user.getRealname();

            if(StringUtils.isNotBlank(phone)){
            	if(!userService.checkPhoneNumRule(phone)){
            		 throw new ServiceException(MessageCode.USER_PHONE_EXIST);
            	}
            	
            	if(!phone.equals(currentPhone) && !userService.phoneIsValid(phone)){
            		throw new ServiceException(MessageCode.USER_PHONE_EXIST);
            	}
            }

            if (!email.equals(currentEmail) && !userService.emialIsValid(email)) {
                    throw new ServiceException(MessageCode.USER_EMAIL_EXIST);
            } 
            
            userService.changeUserInfo(loginUser.getId(), phone, email, realname);
    }


    public PagModel findAllAdminByRealName(UserPageDto userPageDto) {

        try {
            UserModel user = new UserModel();
            BeanUtils.copyProperties(user, userPageDto);
            return userService.findAllAdmin(user);

        } catch (Exception e) {
            throw new ServerException(e);
        }
    }

    public void modifyEnterAdmin(UserInfoDto userInfoDto) {
        try {
            User user = userDao.findEntAdmin(userInfoDto.getEntId());
            if (user != null) {
                userService.changePassword(user, userInfoDto.getPassword(), userInfoDto.getUsername());
            }
        } catch (Exception e) {
            LOG.error("PlatformUserService modifyEnterAdmin error, cause by: ", e);
            throw new ServerException(Constants.API_MESSAGE_FAILURE);
        }
    }

    public User findEnterAdmin(Long entId) {
        try {
            return userDao.findEntAdmin(entId);

        } catch (Exception e) {
            LOG.error("PlatformUserService findEnterAdmin error, cause by: ", e);
            throw new ServerException(Constants.API_MESSAGE_FAILURE);
        }

    }


    private String success(ResponseModel<?> resp) {
        resp.setSuccess(true);
        return formatResponse(resp, null, ResponseModel.SERVICE_SUCCESS);
    }

    private String failure(String msg) {
        return formatFailureResponse(new StateResponseModel(), ResponseModel.SERVICE_FAILURE, msg);
    }

    private String formatResponse(ResponseModel<?> resp, Integer totalPage, String status) {
        ObjectMapper om = new ObjectMapper();
        try {
            if (totalPage != null) {
                resp.setTotal(totalPage);
            }
            if (status != null) {
                resp.setStatus(status);
            }
            resp.setSuccess(true);
            final byte[] data = om.writeValueAsBytes(resp);
            String respStr = new String(data, ENCODING);
            LOG.debug("Response: " + respStr);
            return respStr;
        } catch (Exception e) {
            LOG.error("Failed to convert the marker objects into JSON format!", e);
            return formatFailureResponse(new StateResponseModel(), ResponseModel.SERVICE_FAILURE,
                    ResponseModel.JSON_FORMAT_FAILURE + ": " + e.getMessage());
        }
    }

    private String formatFailureResponse(ResponseModel<?> resp, String status, String failureMsg) {
        ObjectMapper om = new ObjectMapper();
        try {
            if (status != null) {
                resp.setStatus(status);
            }
            resp.setFailureMsg(failureMsg);
            resp.setSuccess(false);
            final byte[] data = om.writeValueAsBytes(resp);
            String respStr = new String(data, ENCODING);
            LOG.debug("Response: " + respStr);
            return respStr;
        } catch (Exception e) {
            LOG.error("Failed to convert the marker objects into JSON format!", e);
            return formatFailureResponse(new StateResponseModel(), ResponseModel.SERVICE_FAILURE,
                    ResponseModel.JSON_FORMAT_FAILURE + ": " + e.getMessage());
        }
    }


    public Pair<UserLoginRetDto, String> login(UserLoginDto dto) {
        User user = userService.findByUsername(dto.getUsername());
        if (null == user) {
            user = userService.findByPhoneNumber(dto.getUsername());
            if (null == user) {
                user = userService.findByEmail(dto.getUsername());
                if (null == user) {
                    return Pair.of(null, "账户不存在");
                }
            }
        }

        if (user.getOrganizationId() != null) {
            Organization org = findRootOrg(user.getOrganizationId());
            if (!"3".equals(org.getStatus())) {
                return Pair.of(null, "账户被锁定");
            }
        }

        if (Boolean.TRUE.equals(user.getLocked())) {
            return Pair.of(null, "账户被锁定");
        }

        String inputPassword = passwordHelper.getEncryptPassword(dto.getUsername(), dto.getPassword(), user.getSalt());

        if (!user.getPassword().equals(inputPassword)) {
            return Pair.of(null, "密码错误");
        }


        Set<String> roles = userService.findRoles(dto.getUsername());
//		Set<String> permissions = userService.findPermissions(dto.getUsername());
        List<Resource> resources = resourceService.findResourcesByRoleId(user.getRoleId());

        List<PermissionNode> permissionTree = getPermissionTree(resources);
        return Pair.of(new UserLoginRetDto(user, roles, permissionTree), null);
    }

    /**
     * 将登录用户的权限以树型结构，分模块返回
     *
     * @param permissions
     * @return
     */
    private List<PermissionNode> getPermissionTree(List<Resource> permissions) {

        // tree 只保存顶层权限节点
        ArrayList<PermissionNode> tree = new ArrayList<>();

        for (Resource resource : permissions) {

            List<String> parents = Arrays.asList(resource.getParentIds().split(","));

            PermissionNode cur = new PermissionNode(resource);
            boolean hasAdd = false;
            for (PermissionNode node : Lists.newArrayList(tree)) {
                if (parents.contains(node.getId().toString())) {
                    // 如果当前 resource从属于当前PermissionNode
                    node.addChild(cur);
                    hasAdd = true;
                    break;
                } else if (node.getParentIds().contains(cur.getId())) {
                    // 如果当前 resource是当前顶层权限节点的父节点，则进行替换
                    // tree中 永远只保存模块级别的顶层resource父节点
                    cur.addChild(node);
                    tree.remove(node);
                    tree.add(cur);
                    hasAdd = true;
                    break;
                }
            }

            // resource 属于新的模块
            if (!hasAdd) {
                tree.add(cur);
            }
        }

        return tree;
    }

//	private PermissionNode isResourceHasParentNode(List<PermissionNode> tree, Resource resource) {
//
//		for (PermissionNode node : tree) {
//			List<String> parents = Arrays.asList(resource.getParentIds().split(","));
//			if (parents.contains(node.getId().toString())) {
//				return node;
//			}
//		}
//
//		return null;
//	}


    private Organization findRootOrg(Long orgId) {
        Organization org = organizationService.findById(orgId);
        if (org.getParentId() != 0) {
            org = findRootOrg(org.getParentId());
        }
        return org;
    }


    public boolean sendVertificationCode(VerificationCodeRequestDto req) {

        User user = userService.findByUsername(req.getUsername());
        if (user == null || !req.getPhone().equals(user.getPhone())) {
            LOG.warn("username and phone-number are not consistent");
            throw new ServiceException(MessageCode.USER_PHONE_NOT_MATCH);
        }

        int num = (int) ((Math.random() * 9 + 1) * 100000);
        String randNum = String.valueOf(num);
        String msgContent = "您在CarDay请求的验证码是: " + randNum;
        String sendResp = commService.sendSms(msgContent, req.getPhone());
        Map<String, String> respMap = JsonUtils.json2Object(sendResp, Map.class);
        if (!Constants.SMS_Publish_Response_Success.equals(respMap.get("statusCode"))) {
            // 只有 statusCode = "20000”才代表短信推送成功
            // {"statusCode":"20000","messages":["SMS send success"],"returnEntity":null}
            return false;
        }

        PhoneVerificationCode code = new PhoneVerificationCode();
        code.setCode(randNum);
        code.setPhoneNumber(req.getPhone());
        code.setExpirationTime(new java.sql.Timestamp((DateUtils.addMinutes(new Date(), 1)).getTime()));

        if (userService.getCode(req.getPhone()) != null) {
            userService.updateCode(code);
        } else {
            userService.saveCode(code);
        }
        return true;
    }


    public Pair<Boolean, String> checkVertificationCode(String phoneNumber, String code) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            User user = userService.findByPhoneNumber(phoneNumber);
            if (user == null) {
                return Pair.of(false, "该手机号未被注册");
            }
            PhoneVerificationCode phoneCode = userService.checkCode(phoneNumber);
            // String phoneCode=redisService.get(phoneNumber);

            if (phoneCode != null && phoneCode.getCode().equals(code)) {
                return Pair.of(true, null);
            } else if (phoneCode == null) {
                return Pair.of(false, "验证码已失效");
            } else {
                return Pair.of(false, "验证码错误");
            }
        } catch (Exception e) {
            LOG.error("PlatformUserService checkVertificationCode error, cause by:", e);
            return Pair.of(false, null);
        }
    }

    public Pair<Boolean, String> resetPassword(String username, String newPassword) {
        User user = userService.findByUsername(username);
        if (null == user) {
            user = userService.findByPhoneNumber(username);
            if (null == user) {
                user = userService.findByEmail(username);
                if (null == user) {
//                    throw new ServerException("账户不存在");
                    return Pair.of(false, "账户不存在");
                }
            }
        }

        userService.changePassword(user.getId(), newPassword);
        return Pair.of(true, null);
    }

    /**
     * 验证手机号是否存在用户表中
     *
     * @param phone
     * @return
     */
    public boolean validatePhone(String phone) {
        return userService.findByPhoneNumber(phone) != null;
    }
}
