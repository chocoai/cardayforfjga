package com.cmdt.carday.microservice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * Created by zhengjun.jing on 7/24/2017.
 */
public class WsRequest extends HttpServletRequestWrapper {
    public WsRequest(HttpServletRequest servletRequest) {
        super(servletRequest);
    }

    public String[] getParameterValues(String parameter) {
        String[] results = super.getParameterValues(parameter);
        if (results == null)
            return null;
        int count = results.length;
        String[] trimResults = new String[count];
        for (int i = 0; i < count; i++) {
            trimResults[i] = results[i].trim();
        }
        return trimResults;
    }

}
