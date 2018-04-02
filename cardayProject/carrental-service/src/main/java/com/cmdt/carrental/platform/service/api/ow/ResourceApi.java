package com.cmdt.carrental.platform.service.api.ow;

import com.cmdt.carrental.common.entity.Resource;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.util.TreeNode;
import com.cmdt.carrental.common.util.TreeNodeView;
import com.cmdt.carrental.platform.service.api.BaseApi;
import com.cmdt.carrental.platform.service.biz.service.PlatformResouceService;
import com.cmdt.carrental.platform.service.common.Constants;
import com.cmdt.carrental.platform.service.exception.ServerException;
import com.cmdt.carrental.platform.service.model.request.resource.ResourceDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
public class ResourceApi extends BaseApi{

	private static final Logger LOG = LoggerFactory.getLogger(ResourceApi.class);

	@Autowired
	private PlatformResouceService resouceService;

	/**
	 * [超级管理员]查询所有资源
	 * [普通管理员]查询当前拥有资源
	 * @param userId
	 */
	@GET
	@Path("/user/{userId}")
	public TreeNode resourceList(@PathParam("userId") Long userId) {
		try{
			return resouceService.resourceList(getUserById(userId));
		}catch(Exception e){
			LOG.error("ResourceApi.resourceList failed, error : ",e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}

	/**
	 *  [超级管理员]查看已分配角色资源
	 * @param userId
	 * @param roleId
	 * @return
	 */
	@GET
	@Path("/user/{userId}/role/{roleId}")
	public Response showRoleResources(@PathParam("userId") Long userId, @PathParam("roleId") Long roleId) {
		try{
			TreeNodeView treeNodeView = resouceService.showRoleResources(getUserById(userId), roleId);
			if (treeNodeView == null) {
				return buildFailureResponse("没有操作权限");
			} else {
				return buildWsResponse(treeNodeView);
			}
		}catch(Exception e){
			LOG.error("ResourceApi.showRoleResources failed, error : ",e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}

	/**
	 * [超级管理员]在指定的父资源上，新增一个空的子资源
	 * @param parentId
	 * @return
	 */
	@GET
	@Path("/appendChild/{parentId}")
	public Resource showAppendChildForm(@PathParam("parentId") Long parentId) {
		try{
			return resouceService.showAppendChildForm(parentId);
		}catch(Exception e){
			LOG.error("ResourceApi.showAppendChildForm failed, error : ",e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}

	/**
	 * [超级管理员] 将新增子资源 保存到数据库
	 * @param resourceDto
	 * @return
	 */
	@POST
	@Path("/appendChild")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createResource(@Valid @NotNull ResourceDto resourceDto) {
		try{
			resouceService.create(resourceDto);
			return buildWsResponse();
		}catch(Exception e){
			LOG.error("ResourceApi.createResource failed, error : ",e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}

	/**
	 * [超级管理员]根据ID查询要修改的资源
	 * @return
	 */
	@GET
	@Path("/{id}/update")
	public Response getResource(@PathParam("id") Long id) {
		try{
			Resource resource = resouceService.showUpdateForm(id);
			if (resource == null){
				return buildFailureResponse("资源不存在");
			} else {
				return buildWsResponse(resource);
			}
		}catch(Exception e){
			LOG.error("ResourceApi.showUpdateForm failed, error : ",e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}

	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateResource(@Valid @NotNull ResourceDto resourceDto) {
		try{
			resouceService.update(resourceDto);
			return buildWsResponse();
		}catch(Exception e){
			LOG.error("ResourceApi.updateResource failed, error : ",e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}

	@POST
	@Path("/deleteResource")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteResource(@Valid @NotNull ResourceDto resourceDto) {
		try{
			User user = getUserById(resourceDto.getUserId());
			if (user != null) {
				if (user.isSuperAdmin()) {
					// 只有超级管理员可以删除资源
					resouceService.delete(user, resourceDto.getId());
					return buildWsResponse();
				} else {
					return buildFailureResponse("没有操作权限");
				}

			} else {
				return buildFailureResponse("操作用户不存在");
			}
		}catch(Exception e){
			LOG.error("ResourceApi.deleteResource failed, error : ",e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}

	@GET
	@Path("/loadUserMenus/{userId}")
	public List<Resource> loadUserMenus(@PathParam("userId") Long userId) {
		try{
			User user = getUserById(userId);
			if (user != null) {
				return resouceService.loadUserMenus(user);
			} else {
				LOG.warn("can't find user by id = %d", userId);
				return Collections.emptyList();
			}
		}catch(Exception e){
			LOG.error("ResourceApi.loadUserMenus failed, error : ",e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}

	@GET
	@Path("/loadUserNoPermissionMenus/{userId}")
	public List<Resource> loadUserNoPermissionMenus(@PathParam("userId") Long userId) {
		try{
			User user = getUserById(userId);
			if (user != null) {
				return resouceService.loadUserNoPermissionMenus(getUserById(userId));
			} else {
				LOG.warn("can't find user by id = %d", userId);
				return Collections.emptyList();
			}
		}catch(Exception e){
			LOG.error("ResourceApi.loadUserNoPermissionMenus failed, error : ",e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}

	@GET
	@Path("/loadUserNoPermissionbuttons/{userId}")
	public List<Resource> loadUserNoPermissionbuttons(@PathParam("userId") Long userId) {
		try{
			User user = getUserById(userId);
			if (user != null) {
				return resouceService.loadUserNoPermissionbuttons(getUserById(userId));
			} else {
				LOG.warn("can't find user by id = %d", userId);
				return Collections.emptyList();
			}
		}catch(Exception e){
			LOG.error("ResourceApi.loadUserNoPermissionbuttons failed, error : ",e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}
}
