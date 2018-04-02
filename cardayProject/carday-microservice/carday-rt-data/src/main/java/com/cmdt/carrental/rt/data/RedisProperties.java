package com.cmdt.carrental.rt.data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author: joe
 * @Date: 17-7-12 下午1:15.
 * @Description:
 */
//@ConfigurationProperties(prefix = "redis")
@Component
public class RedisProperties {

    @Value("${redis.maxTotal}")
    private String maxTotal;
    @Value("${redis.password}")
    private String password;
    @Value("${redis.node1.host}")
    private String node1Host;
    @Value("${redis.node2.host}")
    private String node2Host;
    @Value("${redis.node3.host}")
    private String node3Host;
    @Value("${redis.node4.host}")
    private String node4Host;
    @Value("${redis.node5.host}")
    private String node5Host;
    @Value("${redis.node6.host}")
    private String node6Host;

    public String getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(String maxTotal) {
        this.maxTotal = maxTotal;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNode1Host() {
        return node1Host;
    }

    public void setNode1Host(String node1Host) {
        this.node1Host = node1Host;
    }

    public String getNode2Host() {
        return node2Host;
    }

    public void setNode2Host(String node2Host) {
        this.node2Host = node2Host;
    }

    public String getNode3Host() {
        return node3Host;
    }

    public void setNode3Host(String node3Host) {
        this.node3Host = node3Host;
    }

    public String getNode4Host() {
        return node4Host;
    }

    public void setNode4Host(String node4Host) {
        this.node4Host = node4Host;
    }

    public String getNode5Host() {
        return node5Host;
    }

    public void setNode5Host(String node5Host) {
        this.node5Host = node5Host;
    }

    public String getNode6Host() {
        return node6Host;
    }

    public void setNode6Host(String node6Host) {
        this.node6Host = node6Host;
    }
}
