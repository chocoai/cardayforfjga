package com.cmdt.carday.microservice.api;

import com.cmdt.carday.microservice.model.request.order.AllocateDto;
import com.cmdt.carday.microservice.model.request.order.AuditSendMsgDto;
import com.cmdt.carday.microservice.model.request.order.OrderAuditDto;
import com.cmdt.carday.microservice.model.request.order.PageDto;
import com.cmdt.carday.microservice.common.model.Order.OrderDto;
import com.cmdt.carday.microservice.common.model.Order.enums.OrderReturnType;
import com.cmdt.carday.microservice.common.model.Order.enums.VehicleType;
import com.cmdt.carrental.common.model.BusiOrderQueryDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

/**
 * @Author: joe
 * @Date: 17-7-21 下午1:13.
 * @Description:
 */
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
@RunWith(SpringRunner.class)
@SpringBootTest
public class OderApiTest extends BaseApiTest{



    @Test
    public void orderListForPortal() throws Exception {


        runPost("/order/admin/list", createOrderQueryDto());
    }

    @Test
    public void auditList() throws Exception {
        runPost("/order/auditOrder/list", createOrderQueryDto());
    }

    @Test
    public void allocateList() throws Exception {
        runPost("/order/allocate/list", createOrderQueryDto());
    }

    @Test
    public void viewOrder() throws Exception {
        runGet("/order/2911/view", Collections.singletonMap("userId", "23"));
    }

    @Test
    public void createOrder() throws Exception {
        OrderDto dto = new OrderDto();
        dto.setLoginUserId(201L);
        dto.setOrderUserid(201L);
        dto.setCity("黄石");
        dto.setFromPlace("团城山广场");
        dto.setToPlace("黄石二中");
        dto.setPlanTime(DATE_FORMAT.parse("2017-07-17 18:00:00"));
        dto.setDurationTime(60.0);
        dto.setVehicleType(VehicleType.Economy);
        dto.setOrderReason("出差");
        dto.setPassengerNum(4);
        dto.setReturnType(OrderReturnType.One_Way);
        dto.setComments("我是一个银行家");
        runPost("/order/create", dto);

        runPost("/order/create", dto);
    }

    @Test
    public void audit() throws Exception {

        OrderAuditDto dto = new OrderAuditDto();
        dto.setLoginUserId(201L);
        dto.setOrderId(2812L);
        dto.setStatus(1);

        runPost("/order/audit", dto);
    }

    @Test
    public void sendMessage() throws Exception {
        AuditSendMsgDto dto = new AuditSendMsgDto();
        dto.setLoginUserId(201L);
        dto.setMsg("可惜不是你，真的属于我");
        dto.setPhone("12388888888");

        runPost("/order/audit/sendMsg", dto);

    }

    @Test
    public void queryAuditOrder() throws Exception {

        runGet("/order/auditHistory/201/2911", null);
    }

    @Test
    public void updateOrder() throws Exception {
        OrderDto dto = new OrderDto();
        dto.setId(2911L);
        dto.setLoginUserId(201L);
        dto.setOrderUserid(201L);
        dto.setCity("黄石");
        dto.setFromPlace("团城山广场");
        dto.setToPlace("黄石二中");
        dto.setPlanTime(DATE_FORMAT.parse("2017-07-17 18:00:00"));
        dto.setDurationTime(60.0);
        dto.setVehicleType(VehicleType.Economy);
        dto.setOrderReason("出差");
        dto.setPassengerNum(4);
        dto.setReturnType(OrderReturnType.One_Way);
        dto.setComments("我是一个银行家");

        runPost("/order/update", dto);
    }

    @Test
    public void getAvailableDrivers() throws Exception {

        PageDto dto = new PageDto();
        dto.setCurrentPage(1);
        dto.setNumPerPage(25);
        dto.setLoginUserId(201L);

        runPost("/order/2911/availableDrivers", dto);
    }

    @Test
    public void getAvailableVehicles() throws Exception {

        PageDto dto = new PageDto();
        dto.setCurrentPage(1);
        dto.setNumPerPage(25);
        dto.setLoginUserId(201L);

        runPost("/order/2911/availableVehicles", dto);
    }

    @Test
    public void allocate() throws Exception {

        AllocateDto dto = new AllocateDto();
        dto.setDriverId(246L);
        dto.setLoginUserId(201L);
        dto.setVehicleId(215L);

        runPost("/order/allocate", dto);
    }

    @Test
    public void recreate() throws Exception {

        OrderDto dto = new OrderDto();
        dto.setLoginUserId(201L);
        dto.setOrderUserid(201L);
        dto.setOrganizationId(2L);
        dto.setDriverId(246L);
        dto.setCity("黄石");
        dto.setFromPlace("团城山广场");
        dto.setToPlace("黄石二中");
        dto.setPlanTime(DATE_FORMAT.parse("2017-07-17 18:00:00"));
        dto.setDurationTime(60.0);
        dto.setVehicleType(VehicleType.Economy);
        dto.setVehicleId(215L);
        dto.setOrderReason("出差");
        dto.setPassengerNum(4);
        dto.setReturnType(OrderReturnType.One_Way);
        dto.setComments("我是一个银行家");

        runPost("/order/recreate", dto);
    }

    @Test
    public void updateRecreate() throws Exception {
        OrderDto dto = new OrderDto();
        dto.setId(2842L);
        dto.setLoginUserId(201L);
        dto.setOrderUserid(201L);
        dto.setOrganizationId(2L);
        dto.setDriverId(246L);
        dto.setCity("黄石");
        dto.setFromPlace("团城山广场");
        dto.setToPlace("黄石二中");
        dto.setPlanTime(DATE_FORMAT.parse("2017-07-17 18:00:00"));
        dto.setDurationTime(60.0);
        dto.setVehicleType(VehicleType.Economy);
        dto.setVehicleId(215L);
        dto.setOrderReason("出差");
        dto.setPassengerNum(4);
        dto.setReturnType(OrderReturnType.One_Way);
        dto.setComments("我是一个银行家");

        runPost("/order/recreateUpdate", dto);
    }

    @Test
    public void delete() throws Exception {

        runDelete("/order/2842/delete", null);
    }

    @Test
    public void vehicleSchedule() throws Exception {

        runGet("/order/vehicleSchedule/215/2017-07-19", null);
    }



    private BusiOrderQueryDto createOrderQueryDto() {
        BusiOrderQueryDto dto = new BusiOrderQueryDto();
        dto.setCurrentPage(1);
        dto.setNumPerPage(20);
        dto.setSelfDept(true);
        dto.setChildDept(true);
        dto.setOrganizationId(263L);
        dto.setLoginUserId(451L);

        return dto;
    }
}
