package com.cmdt.carrental.platform.service.api.ow;

import java.io.File;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.cmdt.carrental.common.entity.Role;
import com.cmdt.carrental.common.entity.RoleTemplate;
import com.cmdt.carrental.common.model.OwnerRentModel;
import com.cmdt.carrental.platform.service.api.BaseApi;
import com.cmdt.carrental.platform.service.biz.service.PlatformFileService;
import com.cmdt.carrental.platform.service.biz.service.PlatformRoleService;
import com.cmdt.carrental.platform.service.common.Constants;
import com.cmdt.carrental.platform.service.common.WsResponse;
import com.cmdt.carrental.platform.service.model.request.role.CreateRoleDto;
import com.cmdt.carrental.platform.service.model.request.role.DeleteRoleDto;
import com.cmdt.carrental.platform.service.model.request.role.RoleExportDataDto;
import com.cmdt.carrental.platform.service.model.request.role.RoleListDto;
import com.cmdt.carrental.platform.service.model.request.role.UpdateRoleDto;

public class RoleApi extends BaseApi {

    private static final Logger LOG = LoggerFactory.getLogger(RoleApi.class);

    @Autowired
    private PlatformRoleService roleService;

    @Autowired
    private PlatformFileService fileService;

    /**
     * [超级管理员]加载除超级管理员外的所有角色
     * [租户管理员,企业管理员,部门管理员]加载所属租户及通用的角色
     * 用途:用于左侧菜单功能、创建用户选择下拉列表
     */
    @POST
    @Path("/rolelist")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response roleList(@Valid @NotNull RoleListDto roleListDto) {
        WsResponse<List<Role>> wsResponse = new WsResponse<>();
        try {
            wsResponse.setResult(roleService.roleList(getUserById(roleListDto.getUserId()), roleListDto));
            wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
            wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
        } catch (Exception e) {
            LOG.error("RoleApi roleList by userId error, cause by: : ", e);
            wsResponse.setStatus(Constants.API_STATUS_FAILURE);
            wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
        }
        return Response.ok(wsResponse).build();
    }

    /**
     * [超级管理员]加载租户模板及租户级别以下的模板
     * [租户管理员]加载租户级别以下的模板
     */
    @GET
    @Path("/roleTemplateList/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response roleTemplateList(@PathParam("userId") Long userId) {
        WsResponse<List<RoleTemplate>> wsResponse = new WsResponse<>();
        try {
            wsResponse.setResult(roleService.roleTemplateList(getUserById(userId)));
            wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
            wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
        } catch (Exception e) {
            LOG.error("RoleApi roleTemplateList by userId error, cause by: : ", e);
            wsResponse.setStatus(Constants.API_STATUS_FAILURE);
            wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
        }
        return Response.ok(wsResponse).build();
    }

    /**
     * [超级管理员]加载租户模板及租户级别以下的模板
     * [租户管理员]加载租户级别以下的模板
     */
    @GET
    @Path("/template/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOneTemplate(@PathParam("id") Long id) {
        try {
            RoleTemplate template = roleService.getOneTemplate(id);
            if (template == null) {
                return buildFailureResponse("模板不存在");
            } else {
                return buildWsResponse(template);
            }
        } catch (Exception e) {
            LOG.error("RoleApi getOneTemplate by id error, cause by: : ", e);
            return buildFailureResponse();
        }
    }

    /**
     * 此方法暂时不用
     * [超级管理员]加载租户列表及通用
     * [租户管理员]加载自己及通用
     */
    @GET
    @Path("/ownerRentList/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response ownerRentList(@PathParam("userId") Long userId) {
        WsResponse<List<OwnerRentModel>> wsResponse = new WsResponse<>();
        try {
            wsResponse.setResult(roleService.ownerRentList(getUserById(userId)));
            wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
            wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
        } catch (Exception e) {
            LOG.error("RoleApi ownerRentList by userId error, cause by: : ", e);
            wsResponse.setStatus(Constants.API_STATUS_FAILURE);
            wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
        }
        return Response.ok(wsResponse).build();
    }

    /**
     * [超级管理员,管理员]新增角色
     */
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(@Valid @NotNull CreateRoleDto createRoleDto) {
        try {
            Role role = roleService.create(createRoleDto);
            if (role == null) {
                return buildFailureResponse("角色名称已经存在");
            } else {
                return buildWsResponse(role);
            }
        } catch (Exception e) {
            LOG.error("RoleApi create error, cause by: : ", e);
            return buildFailureResponse();
        }
    }

    /**
     * [超级管理员,管理员]获取角色
     * @return
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") Long id) {
        try {
            Role role = roleService.getRole(id);
            if (role == null) {
                return buildFailureResponse("角色不存在");
            } else {
                return buildWsResponse(role);
            }
        } catch (Exception e) {
            LOG.error("RoleApi showUpdateForm by id error, cause by: : ", e);
            return buildFailureResponse();
        }
    }

    /**
     * [超级管理员,管理员]修改角色
     * @param updateRoleDto
     * @return
     */
    @POST
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@Valid @NotNull UpdateRoleDto updateRoleDto) {
        try {
            Role role = roleService.update(updateRoleDto);
            if (role == null) {
                return buildFailureResponse();
            } else {
                return buildWsResponse();
            }
        } catch (Exception e) {
            LOG.error("RoleApi update error, cause by: : ", e);
            return buildFailureResponse();
        }
    }

    /**
     * [超级管理员,租户管理员]删除角色
     */
    @POST
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@Valid @NotNull DeleteRoleDto deleteRoleDto) {
        try {
            Pair<Boolean, String> result = roleService.delete(
                    getUserById(deleteRoleDto.getUserId()), deleteRoleDto.getId());
            if (result.getLeft()) {
                return buildWsResponse();
            } else {
                return buildFailureResponse(result.getRight());
            }
        } catch (Exception e) {
            LOG.error("RoleApi delete error, cause by: : ", e);
            return buildFailureResponse();
        }
    }

    /**
     * 模板下载
     * @return
     */
    @GET
    @Path("/loadTemplate")
    @Produces("application/vnd.ms-excel")
    public Response downloadFile() {
        String relativePath = File.separator + "template" + File.separator + "role" + File.separator + "template.xls";
        try {
            PhaseInterceptorChain.getCurrentMessage().getExchange().put(Constants.INTECEPTOR_SKIP_FLAG, Boolean.TRUE);

            String newFileName = URLEncoder.encode("template.xls", "UTF-8");
            File file = fileService.downloadTempalte(relativePath);
            ResponseBuilder response = Response.ok((Object) file);
            response.header("Content-Disposition", "attachment; filename=" + newFileName);
            return response.build();
        } catch (Exception e) {
            LOG.error("RoleApi downloadFile error, cause by: ", e);
            WsResponse<String> wsResponse = new WsResponse<>();
            wsResponse.setStatus(Constants.API_STATUS_FAILURE);
            wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
            return Response.ok(wsResponse).build();
        }
    }

    /**
     *  [租户管理员]excel导入角色信息
     * @param attachments
     * @return
     */
    @POST
    @Path("/import")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces({MediaType.APPLICATION_JSON})
    public Response importRole(List<Attachment> attachments) {
        try {
            if (attachments.size() == 1) {
                String result = "";
                for (Attachment attach : attachments) {
                    String extension = StringUtils.getFilenameExtension(attach.getDataHandler().getName());
                    if (null != extension && ("xls".equalsIgnoreCase(extension)
                            || "xlsx".equalsIgnoreCase(extension) || "csv".equalsIgnoreCase(extension))) {
                        DataHandler handler = attach.getDataHandler();
                        result = roleService.importData(handler);
                        break;
                    } else {
                        WsResponse<String> wsResponse = new WsResponse<>();
                        wsResponse.getMessages().add("文件格式不正确！");
                        wsResponse.setStatus(Constants.API_STATUS_FAILURE);
                        return Response.ok(wsResponse).build();
                    }
                }
                WsResponse<String> wsResponse = new WsResponse<>();
                wsResponse.setResult(result);
                wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
                wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
                return Response.ok(wsResponse).build();
            } else {
                WsResponse<String> wsResponse = new WsResponse<>();
                wsResponse.getMessages().add("必须上传且只能上传一个文件！");
                wsResponse.setStatus(Constants.API_STATUS_FAILURE);
                return Response.ok(wsResponse).build();
            }
        } catch (Exception e) {
            LOG.error("RoleApi importRole error, cause by: ", e);
            e.printStackTrace();
            WsResponse<String> wsResponse = new WsResponse<>();
            wsResponse.setStatus(Constants.API_STATUS_FAILURE);
            wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
            return Response.ok(wsResponse).build();
        }
    }

    @POST
    @Path("/exportData")
    @Produces("application/vnd.ms-excel")
    public Response exportData(@Valid @NotNull RoleExportDataDto dto) {
        try {
            File file = roleService.exportData(dto);
            String newFileName = URLEncoder.encode(file.getName(), "UTF-8");

            ResponseBuilder response = Response.ok((Object) file);
            response.header("Content-Disposition", "attachment; filename=" + newFileName);
            return response.build();
        } catch (Exception e) {
            LOG.error("RoleApi exportData error, cause by: ", e);
            WsResponse<String> wsResponse = new WsResponse<>();
            wsResponse.setStatus(Constants.API_STATUS_FAILURE);
            wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
            return Response.ok(wsResponse).build();
        }
    }

    @POST
    @Path("/exportResource")
    @Produces("application/vnd.ms-excel")
    public Response exportResource() {
        try {
            File file = roleService.exportResourceData();
            String newFileName = URLEncoder.encode(file.getName(), "UTF-8");

            ResponseBuilder response = Response.ok((Object) file);
            response.header("Content-Disposition", "attachment; filename=" + newFileName);
            return response.build();
        } catch (Exception e) {
            LOG.error("RoleApi exportResource error, cause by: ", e);
            WsResponse<String> wsResponse = new WsResponse<>();
            wsResponse.setStatus(Constants.API_STATUS_FAILURE);
            wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
            return Response.ok(wsResponse).build();
        }
    }
}
