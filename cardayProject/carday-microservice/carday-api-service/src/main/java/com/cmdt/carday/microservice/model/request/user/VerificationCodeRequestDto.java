package com.cmdt.carday.microservice.model.request.user;

import com.cmdt.carday.microservice.common.Patterns;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @Author: joe
 * @Date: 17-7-10 下午5:01.
 * @Description:
 */
@ApiModel(value = "VerificationCodeRequest", description = "用户手机信息")
public class VerificationCodeRequestDto {

    @ApiModelProperty("用户登录账号")
    private String username;
    @NotNull(message = "手机号不能为空")
    @Pattern(regexp = Patterns.REG_PHONE, message="phone格式错误，应为以13,15,17,18开头的11位数字")
    @ApiModelProperty("手机号")
    private String phone;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
