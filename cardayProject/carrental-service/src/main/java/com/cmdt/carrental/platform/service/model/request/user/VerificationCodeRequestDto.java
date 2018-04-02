package com.cmdt.carrental.platform.service.model.request.user;

import javax.validation.constraints.NotNull;

/**
 * @Author: joe
 * @Date: 17-7-10 下午5:01.
 * @Description:
 */
public class VerificationCodeRequestDto {

//    @NotNull(message = "用户名不能为空")
    private String username;
    @NotNull(message = "手机号不能为空")
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
