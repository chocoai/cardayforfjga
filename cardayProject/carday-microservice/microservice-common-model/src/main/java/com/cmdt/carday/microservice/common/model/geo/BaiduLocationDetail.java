package com.cmdt.carday.microservice.common.model.geo;

/**
 * @Author: joe
 * @Date: 17-7-25 下午1:25.
 * @Description:
 * "location":{"lng":121.61513124890567,"lat":31.250432343160256},"precise":1,"confidence":80,"level":"道路"
 */
public class BaiduLocationDetail {

    private BaiduLocation location;

    private Integer precise;

    private Integer confidence;

    private String level;

    public BaiduLocation getLocation() {
        return location;
    }

    public void setLocation(BaiduLocation location) {
        this.location = location;
    }

    public Integer getPrecise() {
        return precise;
    }

    public void setPrecise(Integer precise) {
        this.precise = precise;
    }

    public Integer getConfidence() {
        return confidence;
    }

    public void setConfidence(Integer confidence) {
        this.confidence = confidence;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}

