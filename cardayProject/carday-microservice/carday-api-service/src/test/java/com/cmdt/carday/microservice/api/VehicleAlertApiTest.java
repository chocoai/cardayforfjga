package com.cmdt.carday.microservice.api;

import com.cmdt.carday.microservice.model.request.alert.AlertExportDto;
import com.cmdt.carrental.common.model.QueryAlertInfoModel;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: joe
 * @Date: 17-7-21 上午9:10.
 * @Description:
 */
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
@RunWith(SpringRunner.class)
@SpringBootTest
public class VehicleAlertApiTest extends BaseApiTest {


    @Test
    public void generateAlertExport() throws Exception {
//        AlertExportDto dto = new AlertExportDto();
//        dto.setVehicleType("-1");
//        dto.setAlertType("OUTBOUND");
//        dto.setUserId(252L);
//        dto.setStartTime("2017-06-21 00:00:00");
//        dto.setEndTime("2017-07-20 23:59:59");
//        dto.setFromOrgId("-1");
//        dto.setDeptId(121);

        Map<String, String> params = new HashMap<>();
        params.put("vehicleType", "-1");
        params.put("alertType", "OUTBOUND");
        params.put("userId", "252");
        params.put("startTime", "2017-06-21 00:00:00");
        params.put("endTime", "2017-07-20 23:59:59");
        params.put("fromOrgId", "-1");
        params.put("deptId", "121");

        runGet("/vehicle-alert/export", params);
    }

    @Test
    public void findVehicleAlertInfo() throws Exception {
        QueryAlertInfoModel dto = new QueryAlertInfoModel();
        dto.setVehicleType("-1");
        dto.setAlertType("OUTBOUND");
        dto.setStartTime(DATE_FORMAT.parse("2017-06-21 00:00:00"));
        dto.setEndTime(DATE_FORMAT.parse("2017-07-20 23:59:59"));
        dto.setFromOrgId(-1L);
        dto.setDeptId(121L);
        dto.setCurrentPage(1);
        dto.setNumPerPage(20);
        dto.setIncludeSelf(true);
        dto.setIncludeChild(false);

        runPost("/vehicle-alert/user/252", dto);
    }
}
