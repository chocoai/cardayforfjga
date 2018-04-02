package com.cmdt.carday.microservice.common.api.impl;

import com.cmdt.carday.microservice.common.model.response.WsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpMethod.POST;

/**
 * @Author: joe
 * @Date: 17-7-24 下午5:53.
 * @Description:
 */
public class BaseServiceImpl {

    @Autowired
    private RestTemplate restTemplate;


    /**
     * @Author zhou yangang
     * @Date 2017-04-21 15:48
     * @Descrition:  调用CommandApi本身的功能，不涉及向TU下发指令
     */
    protected <T, K> T postApiRequest(String apiUrl, K request,
                                    ParameterizedTypeReference<WsResponse<T>> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity httpEntity = new HttpEntity(request, headers);

        ResponseEntity<WsResponse<T>> resEntityVeh =
                restTemplate.exchange(apiUrl,
                        POST,
                        httpEntity,
                        responseType);

        WsResponse<T> resVeh = resEntityVeh.getBody();

        return resVeh.getResult();
    }
}
