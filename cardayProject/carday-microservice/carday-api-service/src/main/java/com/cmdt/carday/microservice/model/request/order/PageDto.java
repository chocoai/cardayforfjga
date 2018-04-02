package com.cmdt.carday.microservice.model.request.order;

/**
 * @Author: joe
 * @Date: 17-7-19 下午5:14.
 * @Description:
 */
public class PageDto {

    private Long loginUserId;

    private Integer currentPage;

    private Integer numPerPage;

    public Long getLoginUserId() {
        return loginUserId;
    }

    public void setLoginUserId(Long loginUserId) {
        this.loginUserId = loginUserId;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getNumPerPage() {
        return numPerPage;
    }

    public void setNumPerPage(Integer numPerPage) {
        this.numPerPage = numPerPage;
    }
}
