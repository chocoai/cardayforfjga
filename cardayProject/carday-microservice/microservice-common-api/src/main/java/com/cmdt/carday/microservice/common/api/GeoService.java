package com.cmdt.carday.microservice.common.api;

import com.cmdt.carday.microservice.common.model.geo.BaiduLocation;
import com.cmdt.carday.microservice.common.model.geo.Point;
import com.cmdt.carday.microservice.common.model.geo.Points;

import java.io.IOException;
import java.util.List;

/**
 * @Author: joe
 * @Date: 17-7-24 下午5:35.
 * @Description:
 */
public interface GeoService {

    List<Point> transGPStoMapPoint(Points points);

    /**
     * 根据地址查经纬度
     * @param location 查询地址
     * @return
     */
    BaiduLocation getPointByAddress(String location) throws IOException;
}
