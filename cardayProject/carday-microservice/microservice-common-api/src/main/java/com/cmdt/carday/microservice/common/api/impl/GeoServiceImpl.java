package com.cmdt.carday.microservice.common.api.impl;

import com.cmdt.carday.microservice.common.api.GeoService;
import com.cmdt.carday.microservice.common.model.geo.*;
import com.cmdt.carday.microservice.common.model.response.WsResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @Author: joe
 * @Date: 17-7-24 下午5:52.
 * GEO 服务的Client Proxy服务类
 */
@Service
public class GeoServiceImpl extends BaseServiceImpl implements GeoService {

    private static final Logger LOG = LogManager.getLogger(GeoServiceImpl.class);

    @Value("${geo.service.url}")
    private String BASE_GEO_URL;

    @Value("${geo.service.api.transGPStoMapPoint}")
    private String TRANS_GPS_TO_MAP_POINT;

    @Value("${geo.service.api.transAddressToPoint}")
    private String TRANS_ADDRESS_TO_POINT;

    public static final ObjectMapper mapper = new ObjectMapper();


    @Override
    public List<Point> transGPStoMapPoint(Points points) {

        ParameterizedTypeReference<WsResponse<List<Point>>> responseType =
                new ParameterizedTypeReference<WsResponse<List<Point>>>() {};

        return postApiRequest(BASE_GEO_URL + TRANS_GPS_TO_MAP_POINT, points, responseType);
    }

    @Override
    public BaiduLocation getPointByAddress(String location) throws IOException {
        ParameterizedTypeReference<WsResponse<String>> responseType =
                new ParameterizedTypeReference<WsResponse<String>>() {};
        // 默认请求的是 百度地图
        POISearchRequest request = new POISearchRequest();
        request.setLocation(location);

        String locationDetail = postApiRequest(BASE_GEO_URL + TRANS_ADDRESS_TO_POINT, request, responseType);

        TypeReference<TransResponse<BaiduLocationDetail>> typeReference = new TypeReference<TransResponse<BaiduLocationDetail>>() {
        };

        try {
            TransResponse<BaiduLocationDetail> baiduResponse = mapper.readValue(locationDetail, typeReference);

            // 百度地图返回的 staus = 0
            // 表示解析成功
            if (baiduResponse.getStatus() == 0) {
                return baiduResponse.getResult().getLocation();
            } else {
                return null;
            }

        } catch (IOException e) {
            LOG.warn("{1} request json deserialize error", BASE_GEO_URL + TRANS_ADDRESS_TO_POINT);
            LOG.warn("error ", e);

            return null;
        }
    }
}
