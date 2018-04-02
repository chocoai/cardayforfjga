package com.cmdt.carday.geo.api.map;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cmdt.carday.microservice.common.model.geo.IpApiDTO;
import com.cmdt.carday.microservice.common.model.geo.POISearchRequest;
import com.cmdt.carday.microservice.common.model.geo.Point;
import com.cmdt.carday.microservice.common.model.geo.Points;
import com.cmdt.carday.microservice.common.model.geo.TransResponse;
import com.cmdt.carday.geo.exception.WSException;
import com.cmdt.carday.microservice.common.model.response.WsResponse;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/map")
public class MapApiService extends MapApiBase
{
    private static final Logger LOG = LogManager.getLogger(MapApiService.class);
    
    /*
     * (non-Javadoc)
     * 
     * @see shouqi.geo.service.api.base.BaiduApiBase#getAddressByPoint(shouqi.geo.service.api.model.Point)
     */
    @ApiOperation(value = "经纬度转地址", response = WsResponse.class)
    @PostMapping("/transPointToAddress")
    public String getAddressByPointMethod(@RequestBody Point coord)
        throws WSException
    {
        return super.getAddressByPoint(coord);
        // return "Funk man"+url;
        
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see shouqi.geo.service.api.base.BaiduApiBase#transGPStoBaiduMethod(shouqi.geo.service.api.model.Point)
     */
    
    @ApiOperation(value = "切换经纬度到地图经纬度", response = WsResponse.class)
    @PostMapping(value = "/transGPSToMapPoint", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Point> transGPStoMapPoint(@RequestBody Points points)
    {
        LOG.info("Inside BaiduApiService.transGPStoBaidu.");
        TransResponse<List<Point>> tResponse = new TransResponse<List<Point>>();
        try
        {
            tResponse = super.transGPStoMapGps(points);
            return tResponse.getResult();
        }
        catch (Exception e)
        {
            LOG.error("Failed in BaiduApiService.transGPStoBaidu!", e);
            throw new RuntimeException("transGPStoBaidu error!", e.getCause());
        }
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see shouqi.geo.service.api.base.BaiduApiBase#transBaidutoGPSMethod(shouqi.geo.service.api.model.Point)
     */
    @ApiOperation(value = "切换地图经纬度到普通经纬度", response = WsResponse.class)
    @PostMapping("/transBaiduToGPS")
    public List<Point> transBaidutoGPSMethod(@RequestBody List<Point> coords)
    {
        LOG.info("Inside BaiduApiService.transGPStoBaidu.");
        // TransResponse<List<Point>> tResponse = new TransResponse<List<Point>>();
        try
        {
            return super.transBaidutoGPS(coords);
        }
        catch (Exception e)
        {
            LOG.error("Failed in BaiduApiService.transBaidutoGPS!", e);
            throw new RuntimeException("transBaidutoGPS error!", e.getCause());
        }
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see shouqi.geo.service.api.base.BaiduApiBase#getCityByIP(String ip)
     */
    @ApiOperation(value = "通过IP获得城市", response = WsResponse.class)
    @PostMapping("/transIPToCity")
    public String getCityByIPMethod(@RequestBody IpApiDTO dto)
    {
        LOG.info("Inside MapApiService.getCityByIP.");
        // TransResponse<String> tResponse = new TransResponse<String>();
        try
        {
            return super.getCityByIP(dto);
        }
        catch (Exception e)
        {
            LOG.error("Failed in BaiduApiService.getCityByIP!", e);
            throw new RuntimeException("getCityByIP error!", e.getCause());
        }
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see shouqi.geo.service.api.base.BaiduApiBase#getPoi(POISearchRequest model)
     */
    @ApiOperation(value = "获得周边兴趣", response = WsResponse.class)
    @PostMapping("/queryPoi")
    public String getPoi(@RequestBody POISearchRequest model)
    {
        try
        {
            return super.poiSearch(model);
        }
        catch (WSException e)
        {
            LOG.error("Failed in BaiduApiService.POI!", e);
            throw new RuntimeException("getPoi error!", e.getCause());
        }
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see shouqi.geo.service.api.base.BaiduApiBase#getPoi(POISearchRequest model)
     */
    @ApiOperation(value = "通过地址获得经纬度", response = WsResponse.class)
    @PostMapping(value = "/transAddressToPoint", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getPointByAddress(@RequestBody POISearchRequest model)
    {
        try
        {
            return super.getPointByAddress(model);
        }
        catch (WSException e)
        {
            LOG.error("Failed in BaiduApiService.POI!", e);
            throw new RuntimeException("getPoi error!", e.getCause());
        }
    }
}
