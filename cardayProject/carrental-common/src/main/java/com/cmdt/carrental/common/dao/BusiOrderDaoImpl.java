package com.cmdt.carrental.common.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.cmdt.carday.microservice.common.model.response.MessageCode;
import com.cmdt.carrental.common.constants.Constants;
import com.cmdt.carrental.common.entity.BusiOrder;
import com.cmdt.carrental.common.entity.BusiOrderAuditRecord;
import com.cmdt.carrental.common.entity.BusiOrderIgnore;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.integration.ServiceAdapter;
import com.cmdt.carrental.common.integration.ShouqiService;
import com.cmdt.carrental.common.integration.model.ActionName;
import com.cmdt.carrental.common.model.BusiOrderQueryDto;
import com.cmdt.carrental.common.model.DriverModel;
import com.cmdt.carrental.common.model.OrderSchedule;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.VehReturnRegistModel;
import com.cmdt.carrental.common.util.DateUtils;
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.common.util.Pagination;
import com.cmdt.carrental.common.util.SqlUtil;
import com.cmdt.carrental.common.util.TypeUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class BusiOrderDaoImpl implements BusiOrderDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ShouqiService shouqiService;

//    @Autowired
//    private VehicleDao vehicleDao;

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public BusiOrder createOrder(final BusiOrder order) {
        final String sql =
                "insert into busi_order(order_no,order_time,order_userid, city,from_place,to_place,plan_st_time,"
                        + "duration_time,wait_time,plan_ed_time,vehicle_type,passenger_num,order_reason,return_type,comments,status,organization_id,order_type,from_lat,from_lng,to_lat,to_lng,"
                        + "driving_type, vehicle_usage, secret_level, unit_name, order_attach)"
                        + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection)
                    throws SQLException {
                PreparedStatement psst = connection.prepareStatement(sql, new String[]{"id"}); //NOSONAR statement and conn already been closed in jdbcTemplate update method
                int count = 1;
                psst.setString(count++, order.getOrderNo());
                psst.setTimestamp(count++, new java.sql.Timestamp(order.getOrderTime().getTime()));
                psst.setLong(count++, order.getOrderUserid());
                psst.setString(count++, order.getCity());
                psst.setString(count++, order.getFromPlace());
                psst.setString(count++, order.getToPlace());
                psst.setTimestamp(count++, new java.sql.Timestamp(order.getPlanStTime().getTime()));
                psst.setDouble(count++, order.getDurationTime());
                psst.setDouble(count++, order.getWaitTime());
                psst.setTimestamp(count++, new java.sql.Timestamp(order.getPlanEdTime().getTime()));
                psst.setString(count++, order.getVehicleType());
                psst.setInt(count++, order.getPassengerNum());
                psst.setString(count++, order.getOrderReason());
                psst.setInt(count++, order.getReturnType());
                psst.setString(count++, order.getComments());
                psst.setInt(count++, order.getStatus());
                psst.setLong(count++, order.getOrganizationId());
                psst.setInt(count++, order.getOrderType());
                psst.setDouble(count++, order.getFromLat());
                psst.setDouble(count++, order.getFromLng());
                psst.setDouble(count++, order.getToLat());
                psst.setDouble(count++, order.getToLng());
                psst.setInt(count++, order.getDrivingType());
                psst.setInt(count++, order.getVehicleUsage());
                // secret level
                if (order.getSecretLevel() == null) {
                    psst.setNull(count++, Types.INTEGER);
                } else {
                    psst.setInt(count++, order.getSecretLevel());
                }
                psst.setString(count++, order.getUnitName());
                psst.setString(count, order.getOrderAttach());
                
                return psst;
            }
        }, keyHolder);
        order.setId(keyHolder.getKey().longValue());
        return order;
    }
    
	public BusiOrder createWithoutApprovalOrder(final BusiOrder order) {
        final String sql =
                "insert into busi_order(order_no,order_time,order_userid, city,from_place,to_place,plan_st_time,"
                        + "duration_time,wait_time,plan_ed_time,vehicle_type,passenger_num,order_reason,return_type,comments,status,organization_id,order_type,from_lat,from_lng,to_lat,to_lng,"
                        + "driving_type, vehicle_usage, secret_level, fact_st_time, unit_name, vehicle_id)"
                        + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection)
                    throws SQLException {
                PreparedStatement psst = connection.prepareStatement(sql, new String[]{"id"}); //NOSONAR statement and conn already been closed in jdbcTemplate update method
                int count = 1;
                psst.setString(count++, order.getOrderNo());
                psst.setTimestamp(count++, new java.sql.Timestamp(order.getOrderTime().getTime()));
                psst.setLong(count++, order.getOrderUserid());
                psst.setString(count++, order.getCity());
                psst.setString(count++, order.getFromPlace());
                psst.setString(count++, order.getToPlace());
                psst.setTimestamp(count++, new java.sql.Timestamp(order.getPlanStTime().getTime()));
                psst.setDouble(count++, order.getDurationTime());
                psst.setDouble(count++, order.getWaitTime());
                psst.setTimestamp(count++, new java.sql.Timestamp(order.getPlanEdTime().getTime()));
                psst.setString(count++, order.getVehicleType());
                psst.setInt(count++, order.getPassengerNum());
                psst.setString(count++, order.getOrderReason());
                psst.setInt(count++, order.getReturnType());
                psst.setString(count++, order.getComments());
                psst.setInt(count++, order.getStatus());
                psst.setLong(count++, order.getOrganizationId());
                psst.setInt(count++, order.getOrderType());
                psst.setDouble(count++, order.getFromLat());
                psst.setDouble(count++, order.getFromLng());
                psst.setDouble(count++, order.getToLat());
                psst.setDouble(count++, order.getToLng());
                psst.setInt(count++, order.getDrivingType());
                psst.setInt(count++, order.getVehicleUsage());
                // secret level
                psst.setInt(count++, order.getSecretLevel());
                psst.setTimestamp(count++, new java.sql.Timestamp(new Timestamp(System.currentTimeMillis()).getTime()));
                psst.setString(count++, order.getUnitName());
                psst.setLong(count, order.getVehicleId());

                return psst;
            }
        }, keyHolder);
        order.setId(keyHolder.getKey().longValue());
        return order;
    }


    public List<Map<String, Object>> getConflict(final BusiOrder order) {
        String sql = "select * from (select count(1) userCNum from busi_order "
                + " where order_userid=? and id<>? and ((fact_st_time<=? and fact_ed_time>=?) or (fact_st_time<=? and fact_ed_time>=?) or (fact_st_time>=? and fact_ed_time<=?))"
                + " and status=4" + ") a," + "(select count(1) vehicleCNum from busi_order "
                + " where vehicle_id=? and id<>? and ((fact_st_time<=? and fact_ed_time>=?) or (fact_st_time<=? and fact_ed_time>=?) or (fact_st_time>=? and fact_ed_time<=?))"
                + " and status=4" + ") b," + "(select count(1) driverCNum from busi_order "
                + " where driver_id=? and id<>? and ((fact_st_time<=? and fact_ed_time>=?) or (fact_st_time<=? and fact_ed_time>=?) or (fact_st_time>=? and fact_ed_time<=?))"
                + " and status=4" + ") c";
        Long id = order.getId();
        if (id == null) {
            id = 0l;
        }
        List<Object> params = new ArrayList<Object>();
        params.add(order.getOrderUserid());
        params.add(id);
        params.add(order.getFactStTime());
        params.add(order.getFactStTime());
        params.add(order.getFactEdTime());
        params.add(order.getFactEdTime());
        params.add(order.getFactStTime());
        params.add(order.getFactEdTime());
        params.add(order.getVehicleId());
        params.add(id);
        params.add(order.getFactStTime());
        params.add(order.getFactStTime());
        params.add(order.getFactEdTime());
        params.add(order.getFactEdTime());
        params.add(order.getFactStTime());
        params.add(order.getFactEdTime());
        params.add(order.getDriverId());
        params.add(id);
        params.add(order.getFactStTime());
        params.add(order.getFactStTime());
        params.add(order.getFactEdTime());
        params.add(order.getFactEdTime());
        params.add(order.getFactStTime());
        params.add(order.getFactEdTime());
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, params.toArray());
        return list;
    }

    public BusiOrder recreateOrder(final BusiOrder order) {
        final String sql =
                "insert into busi_order(order_no,order_time,order_userid, city,from_place,to_place,fact_st_time,"
                        + "fact_ed_time,duration_time,vehicle_type,passenger_num,order_reason,return_type,comments,status,vehicle_id,driver_id,organization_id,wait_time,order_type,"
                        + "unit_name,st_mileage,ed_mileage,vehicle_usage,driving_type,paper_order_no,order_attach )"
                        + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection)
                    throws SQLException {
                PreparedStatement psst = connection.prepareStatement(sql, new String[]{"id"}); //NOSONAR statement and conn already been closed in jdbcTemplate update method
                int count = 1;
                psst.setString(count++, order.getOrderNo());
                psst.setTimestamp(count++, new java.sql.Timestamp(order.getOrderTime().getTime()));
                psst.setLong(count++, order.getOrderUserid());
                psst.setString(count++, order.getCity());
                psst.setString(count++, order.getFromPlace());
                psst.setString(count++, order.getToPlace());
                psst.setTimestamp(count++, new java.sql.Timestamp(order.getFactStTime().getTime()));
                psst.setTimestamp(count++, new java.sql.Timestamp(order.getFactEdTime().getTime()));
                psst.setDouble(count++, order.getDurationTime());
                psst.setString(count++, order.getVehicleType());
                psst.setInt(count++, order.getPassengerNum());
                psst.setString(count++, order.getOrderReason());
                psst.setInt(count++, order.getReturnType());
                psst.setString(count++, order.getComments());
                psst.setInt(count++, order.getStatus());
                psst.setLong(count++, order.getVehicleId());
                psst.setLong(count++, order.getDriverId());
                psst.setLong(count++, order.getOrganizationId());
                psst.setDouble(count++, order.getWaitTime());
                psst.setInt(count++, order.getOrderType());
                psst.setString(count++, order.getUnitName());
                psst.setLong(count++, order.getStMileage());
                psst.setLong(count++, order.getEdMileage());
                psst.setInt(count++, order.getVehicleUsage());
                psst.setInt(count++, order.getDrivingType());
                psst.setString(count++, order.getPaperOrderNo());
                psst.setString(count, order.getOrderAttach());
                return psst;
            }
        }, keyHolder);
        order.setId(keyHolder.getKey().longValue());
        return order;
    }

    public BusiOrder updateOrder(BusiOrder order) {
        final String sql = "update busi_order set order_userid=?, city=?, from_place=?,from_lng=?,from_lat=?,to_place=?,to_lng=?,to_lat=?,plan_st_time=?,"
                + "duration_time=?,wait_time=?,plan_ed_time=?,vehicle_type=?,passenger_num=?,order_reason=?,return_type=?,comments=?,status=?,refuse_comments=null,organization_id=?,secret_level=?,"
                + "vehicle_usage=?, driving_type=?, order_attach=? where id=?";
        jdbcTemplate.update(sql,
                order.getOrderUserid(),
                order.getCity(),
                order.getFromPlace(),
                order.getFromLng(),
                order.getFromLat(),
                order.getToPlace(),
                order.getToLng(),
                order.getToLat(),
                new java.sql.Timestamp(order.getPlanStTime().getTime()),
                order.getDurationTime(),
                order.getWaitTime(),
                new java.sql.Timestamp(order.getPlanEdTime().getTime()),
                order.getVehicleType(),
                order.getPassengerNum(),
                order.getOrderReason(),
                order.getReturnType(),
                order.getComments(),
                order.getStatus(),
                order.getOrganizationId(),
                order.getSecretLevel(),
                order.getVehicleUsage(),
                order.getDrivingType(),
                order.getOrderAttach(),
                order.getId());
        return order;
    }

    public BusiOrder updateReCreateOrder(BusiOrder order) {
        final String sql = "update busi_order set order_userid=?, city=?, from_place=?,to_place=?,fact_st_time=?,"
                + "duration_time=?,wait_time=?,fact_ed_time=?,vehicle_type=?,passenger_num=?,order_reason=?,return_type=?,comments=?,refuse_comments=null,organization_id=?,vehicle_id=?,"
                + "driver_id=?,unit_name=?,st_mileage=?, ed_mileage=?,vehicle_usage=?, driving_type=?,order_attach=? where id=?";
        jdbcTemplate.update(sql,
                order.getOrderUserid(),
                order.getCity(),
                order.getFromPlace(),
                order.getToPlace(),
                new java.sql.Timestamp(order.getFactStTime().getTime()),
                order.getDurationTime(),
                order.getWaitTime(),
                new java.sql.Timestamp(order.getFactEdTime().getTime()),
                order.getVehicleType(),
                order.getPassengerNum(),
                order.getOrderReason(),
                order.getReturnType(),
                order.getComments(),
                order.getOrganizationId(),
                order.getVehicleId(),
                order.getDriverId(),
                order.getUnitName(),
                order.getStMileage(),
                order.getEdMileage(),
                order.getVehicleUsage(),
                order.getDrivingType(),
                order.getOrderAttach(),
                order.getId());
        return order;
    }

    @SuppressWarnings("unchecked")
    public String orderAudit(User loginUser, String json) {
        Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
        Long id = TypeUtils.obj2Long(jsonMap.get("id"));
        Integer status = TypeUtils.obj2Integer(jsonMap.get("status"));
        String refuseComments = TypeUtils.obj2String(jsonMap.get("refuseComments"));
        //纸质排车单号，规则：P+订单编号
        String paperOrderNo = "P" + TypeUtils.obj2String(jsonMap.get("orderNo1"));
        String msg = "";
        if (id > 0 && (status == 1 || status == 5)) {
            String sql = "update busi_order set status=?,refuse_comments=?, paper_order_no=? where id=? and status=0";
            List<Object> params = new ArrayList<Object>();
            params.add(status);
            params.add(refuseComments);
            params.add(paperOrderNo);
            params.add(id);
            int linenum = jdbcTemplate.update(sql, params.toArray());
            if (linenum > 0) {
                // 审核完成后记录审核数据
                BusiOrderAuditRecord record = new BusiOrderAuditRecord();
                record.setOrderId(id);
                record.setAuditUserId(loginUser.getId());
                record.setAuditUserName(loginUser.getRealname());
                record.setAuditUserPhone(loginUser.getPhone());
                record.setStatus(status);
                record.setRefuseComments(refuseComments);
                record.setAuditTime(new Date());
                createAuditRecord(record);
            } else {
                msg = "非法请求，操作失败!";
            }
        } else {
            msg = "非法请求，操作失败!";
        }
        return msg;
    }

    @Override
    public MessageCode orderAudit(User loginUser, Long orderId, Integer status, String refuseComment) {
        String sql = "update busi_order set status=?,refuse_comments=? where id=? and status=0";
        List<Object> params = new ArrayList<>();
        params.add(status);
        params.add(refuseComment);
        params.add(orderId);
        int linenum = jdbcTemplate.update(sql, params.toArray());
        if (linenum > 0) {
            // 审核完成后记录审核数据
            BusiOrderAuditRecord record = new BusiOrderAuditRecord();
            record.setOrderId(orderId);
            record.setAuditUserId(loginUser.getId());
            record.setAuditUserName(loginUser.getRealname());
            record.setAuditUserPhone(loginUser.getPhone());
            record.setStatus(status);
            record.setRefuseComments(refuseComment);
            record.setAuditTime(new Date());
            createAuditRecord(record);
            return MessageCode.COMMON_SUCCESS;
        } else {
            return MessageCode.COMMON_FAILURE;
        }
    }

    public BusiOrderAuditRecord createAuditRecord(final BusiOrderAuditRecord record) {
        final String sql =
                "insert into busi_order_audit_record(order_id,audit_user_id,audit_user_name, audit_user_phone, status, refuse_comments,audit_time)"
                        + " values(?,?,?,?,?,?,?)";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection)
                    throws SQLException {
                PreparedStatement psst = connection.prepareStatement(sql, new String[]{"id"}); //NOSONAR statement and conn already been closed in jdbcTemplate update method
                int count = 1;
                psst.setLong(count++, record.getOrderId());
                psst.setLong(count++, record.getAuditUserId());
                psst.setString(count++, record.getAuditUserName());
                psst.setString(count++, record.getAuditUserPhone());
                psst.setInt(count++, record.getStatus());
                psst.setString(count++, record.getRefuseComments());
                psst.setTimestamp(count, new java.sql.Timestamp(record.getAuditTime().getTime()));
                return psst;
            }
        }, keyHolder);
        record.setId(keyHolder.getKey().longValue());
        return record;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public List<BusiOrderAuditRecord> findAuditRecordByOrderId(Long orderId) {
        if (orderId > 0) {
            String sql = "select t.*,to_char(audit_time,'yyyy-MM-dd hh24:mi:ss') auditTimeF from busi_order_audit_record t where order_id=?";
            List<Object> params = new ArrayList<>();
            params.add(orderId);
            sql += " order by id desc";
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper(BusiOrderAuditRecord.class), params.toArray());
        } else {
            return null;
        }
    }

    @Override
    public Boolean checkOnTask(Long vehicleId) {
        String sql =
                "select count(*) from busi_order where vehicle_id = ? and fact_st_time <= now() and fact_ed_time is NULL";
        List<Object> params = new ArrayList<Object>();
        params.add(vehicleId);
        Integer count = jdbcTemplate.queryForObject(sql.toString(), Integer.class, params.toArray());
        if (count > 0) {
            return true;
        }
        return false;
    }

    @Override
    public PagModel getAvailableDrivers(Long orgid, BusiOrderQueryDto busiOrderModel) {
        if (busiOrderModel != null) {
            String sql = "select u.id,u.realname,u.phone,d.station_id,d.drv_status,d.trip_quantity,d.trip_mileage,bs.station_name from sys_user u "
                    + " left join sys_driver d on u.id = d.id"
                    + " left join sys_role r on u.role_id = r.id"
                    + " left join busi_station bs on bs.id=d.station_id"
                    + " where (u.organization_id = ? or d.dep_id = ?)"
                    + " AND r.template_id = 5"
                    + " and u. id not in (select driver_id from busi_order where organization_id =? and status in (2,3,11,12,13) and driver_id is not null "
                    + " and ((plan_st_time <=? and plan_ed_time >=?) or (plan_st_time <=? and plan_ed_time >=?) or (plan_st_time >? and plan_ed_time <?))) ";

            Long id = busiOrderModel.getId();
            BusiOrder order = this.findOne(id);
            // 处理非法请求
            if (order.getStatus() != 1) {
                return null;
            }
            int currentPage = 1;
            int numPerPage = 10;
            if (busiOrderModel.getCurrentPage() != null) {
                currentPage = busiOrderModel.getCurrentPage();
            }
            if (busiOrderModel.getNumPerPage() != null) {
                numPerPage = busiOrderModel.getNumPerPage();
            }
            List<Object> params = new ArrayList<>();
            params.add(orgid);
            params.add(orgid);
            params.add(orgid);
            params.add(order.getPlanStTime());
            params.add(order.getPlanStTime());
            params.add(order.getPlanEdTime());
            params.add(order.getPlanEdTime());
            params.add(order.getPlanStTime());
            params.add(order.getPlanEdTime());
            sql += " order by id asc";
            Pagination page =
                    new Pagination(sql, currentPage, numPerPage, DriverModel.class, jdbcTemplate, params.toArray());
            return page.getResult();
        } else {
            return null;
        }
    }

    @Override
    public PagModel getAvailableDrivers(Long orgId, Long orderId, Integer curPage, Integer numPage)
    {
        String sql = "select u.id,u.realname,u.phone,d.station_id,bs.station_name from sys_user u "
                + " left join sys_driver d on u.id = d.id"
                + " left join sys_role r on u.role_id = r.id"
                + " left join busi_station bs on bs.id=d.station_id"
                + " where (u.organization_id = ? or d.dep_id = ?)"
                + " AND r.template_id = 5"
                + " and u. id not in (select driver_id from busi_order where organization_id =? and status in (2,3,11,12,13) and driver_id is not null "
                + " and ((plan_st_time <=? and plan_ed_time >=?) or (plan_st_time <=? and plan_ed_time >=?) or (plan_st_time >? and plan_ed_time <?))) ";

        BusiOrder order = this.findOne(orderId);
        // 处理非法请求
        if (order.getStatus() != 1) {
            return null;
        }
        int currentPage = curPage == null ? 1 : curPage;
        int numPerPage = numPage == null ? 10 : numPage;
        List<Object> params = new ArrayList<>();
        params.add(orgId);
        params.add(orgId);
        params.add(orgId);
        params.add(order.getPlanStTime());
        params.add(order.getPlanStTime());
        params.add(order.getPlanEdTime());
        params.add(order.getPlanEdTime());
        params.add(order.getPlanStTime());
        params.add(order.getPlanEdTime());
        sql += " order by id asc";
        Pagination page =
                new Pagination(sql, currentPage, numPerPage, DriverModel.class, jdbcTemplate, params.toArray());
        return page.getResult();
    }

    public int getAvailableDriversCount(Long orgid, Long orderId, Long driverId) {
        String sql = "select id,realname,phone,station_id,station_name from("
                + "select u.id,u.realname,u.phone,d.station_id,bs.station_name from sys_driver d "
                + " left join sys_user u on u. id = d. id"
                + " left join busi_station bs on bs.id=d.station_id"
                + " where (u.organization_id = ? or d.dep_id = ?)"
                + " and u. id not in (select driver_id from busi_order where organization_id =? and status in (2, 3) and driver_id is not null "
                + " and ((plan_st_time <=? and plan_ed_time >=?) or (plan_st_time <=? and plan_ed_time >=?) or (plan_st_time >? and plan_ed_time <?)))) v ";
        BusiOrder order = this.findOne(orderId);
        List<Object> params = new ArrayList<Object>();
        params.add(orgid);
        params.add(orgid);
        params.add(orgid);
        params.add(order.getPlanStTime());
        params.add(order.getPlanStTime());
        params.add(order.getPlanEdTime());
        params.add(order.getPlanEdTime());
        params.add(order.getPlanStTime());
        params.add(order.getPlanEdTime());
        if (driverId != null) {
            sql += " where id=?";
            params.add(driverId);
        }
        sql = "select count(1) from (" + sql + ") tb";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, params.toArray());
        return count;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<OrderSchedule> showOrderSchedule(String json) {
        Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
        Long id = TypeUtils.obj2Long(jsonMap.get("id"));
        Long vehicleId = TypeUtils.obj2Long(jsonMap.get("vehicleId"));
        String sql = "select a.*,a.from_time||'-'||a.to_time boxLabel,b.flag from order_schedule a left outer join ("
                + "select (select id from order_schedule where from_time=to_char(plan_st_time,'hh24:mi')) start_id,"
                + "(select id from order_schedule where to_time=to_char(plan_ed_time,'hh24:mi')) end_id,1 flag from busi_order where vehicle_id=? and status in(2,3)"
                + " union "
                + "select (select id from order_schedule where from_time=to_char(plan_st_time,'hh24:mi')) start_id,"
                + "(select id from order_schedule where to_time=to_char(plan_ed_time,'hh24:mi')) end_id,2 flag from busi_order where id=?"
                + ") b on a.id>=b.start_id and a.id<=b.end_id order by a.id asc";
        List<Object> params = new ArrayList<Object>();
        params.add(vehicleId);
        params.add(id);
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper(OrderSchedule.class), params.toArray());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<OrderSchedule> showOrderSchedule(Long vehicleId, Long orderId) {
        String sql = "select a.*,a.from_time||'-'||a.to_time boxLabel,b.flag from order_schedule a left outer join ("
                + "select (select id from order_schedule where from_time=to_char(plan_st_time,'hh24:mi')) start_id,"
                + "(select id from order_schedule where to_time=to_char(plan_ed_time,'hh24:mi')) end_id,1 flag from busi_order where vehicle_id=? and status in(2,3)"
                + " union "
                + "select (select id from order_schedule where from_time=to_char(plan_st_time,'hh24:mi')) start_id,"
                + "(select id from order_schedule where to_time=to_char(plan_ed_time,'hh24:mi')) end_id,2 flag from busi_order where id=?"
                + ") b on a.id>=b.start_id and a.id<=b.end_id order by a.id asc";
        List<Object> params = new ArrayList<Object>();
        params.add(vehicleId);
        params.add(orderId);
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper(OrderSchedule.class), params.toArray());
    }

    @SuppressWarnings("unchecked")
    public String orderAllocate(String json) {
        String msg = "";
        Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
        Long id = TypeUtils.obj2Long(jsonMap.get("id"));
        Long vehicleId = TypeUtils.obj2Long(jsonMap.get("vehicleId"));
        Long driverId = TypeUtils.obj2Long(jsonMap.get("driverId"));
        Long stMileage = TypeUtils.obj2Long(jsonMap.get("stMileage"));
        Date factStTime = TypeUtils.obj2Date(jsonMap.get("factStTime"));
        BusiOrder order = this.findOrderByIdSimple(id);
        Long orgId = order.getOrganizationId();
        int driverCount = getAvailableDriversCount(orgId, id, driverId);
        if (driverCount == 0) {
            msg = "司机无效，操作失败!";
            return msg;
        }
//        int vehicleCount=vehicleDao.getAvailableVehiclesCount(orgId, id, vehicleId);
//        if(vehicleCount==0){
//        	msg = "车辆无效，操作失败!";
//        	return msg;
//        }

        if (id > 0 && vehicleId > 0 && driverId > 0) {
            String sql = "update busi_order set vehicle_id=?,driver_id=?,st_mileage=?,fact_st_time=?,status=2 where id=? and status=1";
            List<Object> params = new ArrayList<Object>();
            params.add(vehicleId);
            params.add(driverId);
            params.add(stMileage);
            params.add(factStTime);
            params.add(id);
            int linenum = jdbcTemplate.update(sql, params.toArray());
            if (linenum == 0) {
                msg = "非法请求，操作失败!";
            }else {
            	//修改司机的状态为已出场
            	String driversql="update sys_driver set drv_status=6 where id=?";
        		jdbcTemplate.update(driversql,driverId);
        		//修改车辆的状态为已出车
        		String vehicleSql="update busi_vehicle set veh_status=0 where id=?";
        		jdbcTemplate.update(vehicleSql,vehicleId);
            }
        } else {
            msg = "非法请求，操作失败!";
        }
        return msg;
    }

    public String orderAllocate(long orderId, long vehicleId, long driverId) {
        String msg = "";
        BusiOrder order = this.findOrderByIdSimple(orderId);
        Long orgId = order.getOrganizationId();
        int driverCount = getAvailableDriversCount(orgId, orderId, driverId);
        if (driverCount == 0) {
            msg = "司机无效，操作失败!";
            return msg;
        }
//        int vehicleCount=vehicleDao.getAvailableVehiclesCount(orgId, id, vehicleId);
//        if(vehicleCount==0){
//        	msg = "车辆无效，操作失败!";
//        	return msg;
//        }

        if (orderId > 0 && vehicleId > 0 && driverId > 0) {
            String sql = "update busi_order set vehicle_id=?,driver_id=?,status=2 where id=? and status=1";
            List<Object> params = new ArrayList<Object>();
            params.add(vehicleId);
            params.add(driverId);
            params.add(orderId);
            int linenum = jdbcTemplate.update(sql, params.toArray());
            if (linenum == 0) {
                msg = "非法请求，操作失败!";
            }
        } else {
            msg = "非法请求，操作失败!";
        }
        return msg;
    }

    public String orderVehicleOut(Long id) {
        String msg = "";
        String sql = "update busi_order set status=11 where id=? and status=2";
        List<Object> params = new ArrayList<Object>();
        params.add(id);
        int linenum = jdbcTemplate.update(sql, params.toArray());
        if (linenum == 0) {
            msg = "非法请求，操作失败!";
        }
        return msg;
    }

    public String orderReachFromPlace(Long id) {
        String msg = "";
        String sql = "update busi_order set status=12 where id=? and status=11";
        List<Object> params = new ArrayList<Object>();
        params.add(id);
        int linenum = jdbcTemplate.update(sql, params.toArray());
        if (linenum == 0) {
            msg = "非法请求，操作失败!";
        }
        return msg;
    }

    public String orderOngoing(Long id) throws Exception {
        String msg = "";
        BusiOrder order = this.findOne(id);
        String imei = order.getDeviceNumber();
        List<Object> params = new ArrayList<Object>();
        String sql = "";
        if (order.getFactStTime() != null) {
            sql = "update busi_order set status=3 where id=? and (status=12 or status=13)";
        } else {
            if (StringUtils.isNotBlank(imei)) {
                Long st_mileage = 0l;
                //填充累计总油耗,累计里程(调用首汽接口)
                Map<String, Object> result = new ServiceAdapter(shouqiService).doService(ActionName.GETLOCATIONBYIMEI, new Object[]{imei});
                if (result != null && result.get("status").equals("success") && result.get("data") != null && StringUtils.isNotEmpty(result.get("data").toString())) {
                    JsonNode jsonNode = MAPPER.readTree(result.get("data").toString());
                    if ("000".equals(jsonNode.get("status").asText())) {
                        JsonNode resultNode = jsonNode.get("result");
                        if (resultNode != null) {
                            if (resultNode.get("mileage") != null) {
                                String mileageVal = resultNode.get("mileage").asText();
                                st_mileage = TypeUtils.obj2Long(df.format((double) Integer.valueOf(mileageVal) / 1000));//米转换为千米
                            }
                        }
                    }
                }
                sql = "update busi_order set status=3,fact_st_time=now(),st_mileage=? where id=? and (status=12 or status=13)";
                params.add(st_mileage);
            } else {
                return "车辆绑定了非法OBD或者未绑定OBD。操作失败。";
            }
        }
        params.add(id);
        int linenum = jdbcTemplate.update(sql, params.toArray());
        if (linenum == 0) {
            msg = "非法请求，操作失败!";
        }
        return msg;
    }

    public String orderWaiting(Long id) {
        String msg = "";
        String sql = "update busi_order set status=13 where id=? and status=3";
        List<Object> params = new ArrayList<Object>();
        params.add(id);
        int linenum = jdbcTemplate.update(sql, params.toArray());
        if (linenum == 0) {
            msg = "非法请求，操作失败!";
        }
        return msg;
    }

    public String orderFinish(Long id) throws Exception {
        String msg = "";
        BusiOrder order = findOne(id);
        String imei = order.getDeviceNumber();
//        String starttime="";
        String endtime = "";
        Long ed_mileage = 0l;
        if (StringUtils.isNotBlank(imei)) {
//			starttime = order.getFactStTimeF();
            endtime = DateUtils.getNowDate(DateUtils.FORMAT_YYYY_MM_DD_HH_MI_SS);
            // Map<String, Object> result = new
            // ServiceAdapter(shouqiService).doService(ActionName.FINDTRIPPROPERTYDATABYTIMERANGE,
            // new Object[] { imei, starttime, endtime });
            // if (result.get("data") != null) {
            // JsonNode jsonNode =
            // MAPPER.readTree(result.get("data").toString());
            // if ("000".equals(jsonNode.get("status").asText())) {
            // ArrayNode rows = (ArrayNode) jsonNode.get("result");
            // if (rows != null && rows.size() > 0) {
            // JsonNode dataNode = null;
            // // 涉及到obd更换，只获得最新obd数据
            // if (rows.size() == 1) {
            // dataNode = rows.get(0);
            // } else if (rows.size() == 2) {
            // dataNode = rows.get(1);
            // }
            // fact_mileage=dataNode.get("mileage").asLong();
            // }
            // }
            // }
            // 填充累计总油耗,累计里程(调用首汽接口)
            Map<String, Object> result = new ServiceAdapter(shouqiService).doService(ActionName.GETLOCATIONBYIMEI,
                    new Object[]{imei});
            if (result != null && result.get("status").equals("success") && result.get("data") != null
                    && StringUtils.isNotEmpty(result.get("data").toString())) {
                JsonNode jsonNode = MAPPER.readTree(result.get("data").toString());
                if ("000".equals(jsonNode.get("status").asText())) {
                    JsonNode resultNode = jsonNode.get("result");
                    if (resultNode != null) {
                        if (resultNode.get("mileage") != null) {
                            String mileageVal = resultNode.get("mileage").asText();
                            ed_mileage = TypeUtils.obj2Long(df.format((double) Integer.valueOf(mileageVal) / 1000));// 米转换为千米
                        }
                    }
                }
            }else{
                ed_mileage = 1+ (long)(new Random().nextDouble()*100);
            }
        } else {
            return "车辆绑定了非法OBD或者未绑定OBD。操作失败。";
        }

        java.sql.Timestamp endTime = new java.sql.Timestamp(DateUtils.string2Date(endtime, DateUtils.FORMAT_YYYY_MM_DD_HH_MI_SS).getTime());

        Double fact_durationTime=60D;
        if(order.getFactStTime() !=null){
             fact_durationTime = (double) (endTime.getTime() - order.getFactStTime().getTime() / (1000 * 60));
        }

        fact_durationTime = Math.floor(fact_durationTime);
        Long fact_mileage = ed_mileage;
        if(order.getStMileage() !=null){
            fact_mileage = ed_mileage - order.getStMileage();
        }
        String sql = "update busi_order set status=16,fact_ed_time=?,fact_duration_time=?,ed_mileage=?,fact_mileage=? where id=? and status=2";
        List<Object> params = new ArrayList<Object>();
        params.add(endTime);
        params.add(fact_durationTime);
        params.add(ed_mileage);
        params.add(fact_mileage);
        params.add(id);
        int linenum = jdbcTemplate.update(sql, params.toArray());
        if (linenum == 0) {
            msg = "非法请求，操作失败!";
        }
        return msg;
    }

//    @Override
//    public String orderPay(Long id)
//    {
//        String msg = "";
//        String sql = "update busi_order set status=15 where id=? and status=4";
//        List<Object> params = new ArrayList<Object>();
//        params.add(id);
//        int linenum = jdbcTemplate.update(sql, params.toArray());
//        if (linenum == 0)
//        {
//            msg = "非法请求，操作失败!";
//        }
//        return msg;
//    }
//    
//    @Override
//    public String orderEvaluation(Long id)
//    {
//        String msg = "";
//        String sql = "update busi_order set status=16 where id=? and status=15";
//        List<Object> params = new ArrayList<Object>();
//        params.add(id);
//        int linenum = jdbcTemplate.update(sql, params.toArray());
//        if (linenum == 0)
//        {
//            msg = "非法请求，操作失败!";
//        }
//        return msg;
//    }

    public void deleteOrder(Long id) {
        final String sql = "delete from busi_order where id=?";
        jdbcTemplate.update(sql, id);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public BusiOrder findOne(Long id) {
        String sql = "select t.*, csu.realname orderUsername, csu.phone orderUserphone, dsu.realname driverName, dsu.phone driverPhone, "
                + "to_char(order_time,'yyyy-MM-dd') order_time_f, to_char(plan_st_time,'yyyy-MM-dd hh24:mi') plan_st_time_f, "
                + "to_char(plan_ed_time,'yyyy-MM-dd hh24:mi:ss') plan_ed_time_f, to_char(fact_st_time,'yyyy-MM-dd hh24:mi:ss') fact_st_time_f, "
                + "to_char(fact_ed_time,'yyyy-MM-dd hh24:mi:ss') fact_ed_time_f, bv.vehicle_model vehicle_model, bv.vehicle_brand vehicle_brand, "
                + "bv.vehicle_number vehicle_number, bv.seat_number seat_number, bv.device_number device_number, so.name org_name, "
                + "tt.payment_cash,tt.comment,tt.comment_level  from busi_order t  "
                + "left join payment_comment tt on t.id=tt.order_id "
                + "left join sys_user csu on t.order_userid=csu.id "
                + "left join sys_user dsu on t.driver_id=dsu.id "
                + "left join busi_vehicle bv on t.vehicle_id=bv.id "
                + "left join sys_organization so on t.organization_id=so.id "
                + "where t.id=? ";
        List<BusiOrder> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper(BusiOrder.class), id);
        if (null != list && list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
    public BusiOrder findByOrderId(Long id) {
        String sql = "select * from busi_order where id=?";
        List<BusiOrder> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(BusiOrder.class), id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

//    @SuppressWarnings({ "unchecked", "rawtypes" })
//	@Override
//	public BusiOrder queryBusiOrderByOrderNo(Integer orderId) {
//		String sql = "select t.*, csu.realname orderUsername, csu.phone orderUserphone, dsu.realname driverName, dsu.phone driverPhone, "
//        		+ "to_char(order_time,'yyyy-MM-dd') order_time_f, to_char(plan_st_time,'yyyy-MM-dd hh24:mi') plan_st_time_f, "
//        		+ "to_char(plan_ed_time,'yyyy-MM-dd hh24:mi:ss') plan_ed_time_f, to_char(fact_st_time,'yyyy-MM-dd hh24:mi:ss') fact_st_time_f, "
//        		+ "to_char(fact_ed_time,'yyyy-MM-dd hh24:mi:ss') fact_ed_time_f, bv.vehicle_model vehicle_model, bv.vehicle_brand vehicle_brand, "
//        		+ "bv.vehicle_number vehicle_number, bv.device_number device_number, so.name org_name, "
//        		+ "tt.payment_cash,tt.comment,tt.comment_level  from busi_order t  "
//        		+ "left join payment_comment tt on t.id=tt.order_id "
//        		+ "left join sys_user csu on t.order_userid=csu.id "
//        		+ "left join sys_user dsu on t.driver_id=dsu.id "
//        		+ "left join busi_vehicle bv on t.vehicle_id=bv.id "
//        		+ "left join sys_organization so on t.organization_id=so.id "
//				+ " where t.id = ?";
// 		List<BusiOrder> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper(BusiOrder.class), orderId);
// 		if(list != null && list.size() > 0){
// 			return list.get(0);
// 		}
//		return null;
//	}

    /**
     * 通过订单编号获得相应的订单信息
     *
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public BusiOrder findOrderByIdSimple(Long order_id) {
        String sql = "select t.order_no, t.status , t.order_userid ,t.driver_id, t.organization_id, t.vehicle_id, t.plan_st_time, t.from_place, t.to_place, t.secret_level from busi_order t where t.id = ? ";
        List<BusiOrder> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper(BusiOrder.class), order_id);
        if (null != list && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 展示限定為快車（個人用車與企業用車）
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public List<BusiOrder> findLatNearestOrder() {
        final String sql = "select t.*, csu.realname orderUsername, csu.phone orderUserphone, dsu.realname driverName, dsu.phone driverPhone, "
                + "to_char(order_time,'yyyy-MM-dd') order_time_f, to_char(plan_st_time,'yyyy-MM-dd hh24:mi') plan_st_time_f, "
                + "to_char(plan_ed_time,'yyyy-MM-dd hh24:mi:ss') plan_ed_time_f, to_char(fact_st_time,'yyyy-MM-dd hh24:mi:ss') fact_st_time_f, "
                + "to_char(fact_ed_time,'yyyy-MM-dd hh24:mi:ss') fact_ed_time_f, bv.vehicle_model vehicle_model, bv.vehicle_brand vehicle_brand, "
                + "bv.vehicle_number vehicle_number, bv.device_number device_number, so.name org_name, "
                + "tt.payment_cash,tt.comment,tt.comment_level  from busi_order t  "
                + "left join payment_comment tt on t.id=tt.order_id "
                + "left join sys_user csu on t.order_userid=csu.id "
                + "left join sys_user dsu on t.driver_id=dsu.id "
                + "left join busi_vehicle bv on t.vehicle_id=bv.id "
                + "left join sys_organization so on t.organization_id=so.id "
                + " where t.order_type = 1 and t.status = 1 and t.plan_st_time < now() order by t.plan_st_time desc ";
        List<BusiOrder> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper(BusiOrder.class));
        if (list.size() == 0) {
            return null;
        }
        return list;
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public PagModel findAll(Long orgid, String json) {
        String sql = "select t.*, csu.realname orderUsername, csu.phone orderUserphone, dsu.realname driverName, dsu.phone driverPhone, "
                + "to_char(order_time,'yyyy-MM-dd') order_time_f, to_char(plan_st_time,'yyyy-MM-dd hh24:mi') plan_st_time_f, "
                + "to_char(plan_ed_time,'yyyy-MM-dd hh24:mi:ss') plan_ed_time_f, to_char(fact_st_time,'yyyy-MM-dd hh24:mi:ss') fact_st_time_f, "
                + "to_char(fact_ed_time,'yyyy-MM-dd hh24:mi:ss') fact_ed_time_f, bv.vehicle_model vehicle_model, bv.vehicle_brand vehicle_brand, "
                + "bv.vehicle_number vehicle_number, bv.device_number device_number, so.name org_name, "
                + "tt.payment_cash,tt.comment,tt.comment_level  from busi_order t  "
                + "left join payment_comment tt on t.id=tt.order_id "
                + "left join sys_user csu on t.order_userid=csu.id "
                + "left join sys_user dsu on t.driver_id=dsu.id "
                + "left join busi_vehicle bv on t.vehicle_id=bv.id "
                + "left join sys_organization so on t.organization_id=so.id ";

        String countsql =
                "select t.id "
                        + "from busi_order t  "
                        + "left join sys_organization so on t.organization_id=so.id ";

        String whereSql = "where 1=1 ";
        String orderSql = "";
        List<Object> params = new ArrayList<Object>();
        int currentPage = 1;
        int numPerPage = 10;
        String colOrderBy = "";
        if (StringUtils.isNotBlank(json)) {
            Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
            if (jsonMap.get("currentPage") != null) {
                currentPage = Integer.valueOf(String.valueOf(jsonMap.get("currentPage")));
            }
            if (jsonMap.get("numPerPage") != null) {
                numPerPage = Integer.valueOf(String.valueOf(jsonMap.get("numPerPage")));
            }
            Long deptId = TypeUtils.obj2Long(jsonMap.get("organizationId"));
            if (deptId == 0l) {
//            	whereSql += " and t.organization_id in(select id from sys_organization where parent_id=?)";
                whereSql += " and so.parent_id=? ";
                params.add(orgid);
            } else {
                whereSql += " and t.organization_id=?";
                params.add(deptId);
            }
            String orderNo = TypeUtils.obj2String(jsonMap.get("orderNo"));
            if (StringUtils.isNotBlank(orderNo)) {
                whereSql += " and t.order_no like " + SqlUtil.processLikeInjectionStatement(orderNo);
            }
            Date orderTimeSt = TypeUtils.obj2DateFormat(jsonMap.get("orderTime"));
            if (null != orderTimeSt) {
                Date orderTimeEd = DateUtils.addDays(orderTimeSt, 1);
                whereSql += " and t.order_time>? and t.order_time<?";
                params.add(orderTimeSt);
                params.add(orderTimeEd);
            }
            //remove default day limit
//            else{
//				// for app
//				Date st = DateUtils.addDays(new Date(), -5);
//				Date ed = DateUtils.addDays(new Date(), 1);
//				sql += " and t.order_time>? and t.order_time<?";
//                params.add(st);
//                params.add(ed);
//            }
            if (StringUtils.isNotBlank(TypeUtils.obj2String(jsonMap.get("orderCat")))) {
                Integer orderCat = TypeUtils.obj2Integer(jsonMap.get("orderCat"));
                if (orderCat == 1) {
                    whereSql += " and t.plan_st_time is not null";
                } else if (orderCat == 2) {
                    whereSql += " and t.plan_st_time is null";
                } else {
                    return null;
                }
            }
            if (StringUtils.isNotBlank(TypeUtils.obj2String(jsonMap.get("status")))) {
                Integer status = TypeUtils.obj2Integer(jsonMap.get("status"));
                whereSql += " and t.status=?";
                params.add(status);
            }
            Date planTime_st = TypeUtils.obj2DateFormat(jsonMap.get("planTime"));
            if (null != planTime_st) {
                Date planTime_ed = DateUtils.addDays(planTime_st, 1);
                whereSql += " and t.plan_st_time>? and t.plan_st_time<?";
                params.add(planTime_st);
                params.add(planTime_ed);
            }

            // filter order by order type
            if (jsonMap.get("orderType") != null && !"".equals(jsonMap.get("orderType"))) {
                Integer orderType = TypeUtils.obj2Integer(jsonMap.get("orderType"));
                whereSql += " and t.order_type = ? ";
                params.add(orderType);
            }

            // for app
            Date startTime = TypeUtils.obj2DateFormat(jsonMap.get("startTime"));
            Date endTime = TypeUtils.obj2DateFormat(jsonMap.get("endTime"));
            if (null != startTime && null != endTime) {
                endTime = DateUtils.addDays(endTime, 1);
                whereSql += " and ((plan_st_time>=? and plan_st_time<=?) or (fact_st_time>=? and fact_st_time<=?))";
                params.add(new Timestamp(startTime.getTime()));
                params.add(new Timestamp(endTime.getTime()));
                params.add(new Timestamp(startTime.getTime()));
                params.add(new Timestamp(endTime.getTime()));
            }
            colOrderBy = TypeUtils.obj2String(jsonMap.get("colOrderBy"));
        }
        if (StringUtils.isNotBlank(colOrderBy)) {
            orderSql += " " + colOrderBy;
        } else {
            orderSql += " order by t.id desc";
        }

        //organize SQL
        countsql = countsql.concat(whereSql);
        sql = sql.concat(whereSql).concat(orderSql);

        Pagination page = new Pagination(countsql, sql, currentPage, numPerPage, BusiOrder.class, jdbcTemplate, params.toArray());
        return page.getResult();
    }

    @Override
    public PagModel queryOrderAsAdmin(List<Long> orgIdList, BusiOrderQueryDto busiOrderModel) {
        StringBuffer orgIdsStr = new StringBuffer();
        for (Long id : orgIdList) {
            orgIdsStr.append(id + ",");
        }
        if (orgIdsStr != null && orgIdsStr.length() > 0) {
            orgIdsStr.replace(orgIdsStr.length() - 1, orgIdsStr.length(), "");
        }
        String sql = "select t.*, csu.realname orderUsername, csu.phone orderUserphone, dsu.realname driverName, dsu.phone driverPhone, "
                + "to_char(order_time,'yyyy-MM-dd') order_time_f, to_char(plan_st_time,'yyyy-MM-dd hh24:mi') plan_st_time_f, "
                + "to_char(plan_ed_time,'yyyy-MM-dd hh24:mi:ss') plan_ed_time_f, to_char(fact_st_time,'yyyy-MM-dd hh24:mi:ss') fact_st_time_f, "
                + "to_char(fact_ed_time,'yyyy-MM-dd hh24:mi:ss') fact_ed_time_f, bv.vehicle_model vehicle_model, bv.vehicle_brand vehicle_brand, "
                + "bv.vehicle_number vehicle_number, bv.device_number device_number, so.name org_name, "
                + "tt.payment_cash,tt.comment,tt.comment_level  from busi_order t  "
                + "left join payment_comment tt on t.id=tt.order_id "
                + "left join sys_user csu on t.order_userid=csu.id "
                + "left join sys_user dsu on t.driver_id=dsu.id "
                + "left join busi_vehicle bv on t.vehicle_id=bv.id "
                + "left join sys_organization so on t.organization_id=so.id ";

        String countsql =
                "select t.id "
                        + "from busi_order t  "
                        + "left join sys_organization so on t.organization_id=so.id ";
        String whereSql = "where 1=1 ";
        String orderSql = "";
        List<Object> params = new ArrayList<Object>();
        int currentPage = 1;
        int numPerPage = 10;
        String colOrderBy = "";
        if (busiOrderModel != null) {
            if (busiOrderModel.getCurrentPage() != null) {
                currentPage = busiOrderModel.getCurrentPage();
            }
            if (busiOrderModel.getNumPerPage() != null) {
                numPerPage = busiOrderModel.getNumPerPage();
            }

            if (StringUtils.isNotBlank(orgIdsStr)) {
                whereSql += " and t.organization_id in (" + orgIdsStr + ")";
            }

            String orderNo = busiOrderModel.getOrderNo();
            if (StringUtils.isNotBlank(orderNo)) {
                whereSql += " and t.order_no like " + SqlUtil.processLikeInjectionStatement(orderNo);
            }

            Date orderTimeSt = TypeUtils.obj2DateFormat(busiOrderModel.getOrderTime());
            ;
            if (null != orderTimeSt) {
                Date orderTimeEd = DateUtils.addDays(orderTimeSt, 1);
                whereSql += " and t.order_time>? and t.order_time<?";
                params.add(orderTimeSt);
                params.add(orderTimeEd);
            }
            //remove default day limit
//            else { //预约订单，查询订单日期为空，默认查询当前日期向前推5天的订单，补录订单不做限制
//				Date st = DateUtils.addDays(new Date(), -5);
//				Date ed = DateUtils.addDays(new Date(), 1);
//				whereSql += " and t.order_time>? and t.order_time<?";
//                params.add(st);
//                params.add(ed);
//            }

            if (busiOrderModel.getOrderCat() != null) {
                Integer orderCat = TypeUtils.obj2Integer(busiOrderModel.getOrderCat());
                if (orderCat == 1) {
                    whereSql += " and t.plan_st_time is not null";
                } else if (orderCat == 2) {
                    whereSql += " and t.plan_st_time is null";
                } else {
                    return null;
                }
            }

            if (busiOrderModel.getStatus() != null) {
                Integer status = TypeUtils.obj2Integer(busiOrderModel.getStatus());
                whereSql += " and t.status=?";
                params.add(status);
            }

            //Date planTime_st = TypeUtils.obj2DateFormat(jsonMap.get("planTime"));
            Date planTime_st = TypeUtils.obj2DateFormat(busiOrderModel.getPlanTime());
            if (null != planTime_st) {
                Date planTime_ed = DateUtils.addDays(planTime_st, 1);
                whereSql += " and t.plan_st_time>? and t.plan_st_time<?";
                params.add(planTime_st);
                params.add(planTime_ed);
            }

            // filter order by order type
            Integer orderType = busiOrderModel.getOrderType();
            if (orderType != null) {
                whereSql += " and t.order_type = ? ";
                params.add(orderType);
            }

            // for app
            Date startTime = TypeUtils.obj2DateFormat(busiOrderModel.getStartTime());
            Date endTime = TypeUtils.obj2DateFormat(busiOrderModel.getEndTime());
            if (null != startTime && null != endTime) {
                endTime = DateUtils.addDays(endTime, 1);
                whereSql += " and ((plan_st_time>=? and plan_st_time<=?) or (fact_st_time>=? and fact_st_time<=?))";
                params.add(startTime);
                params.add(endTime);
                params.add(startTime);
                params.add(endTime);
            }
            colOrderBy = busiOrderModel.getColOrderBy();
        }

        // 特殊警务权限过滤
        if (!SecurityUtils.getSubject().isPermitted(Constants.SPECIAL_SERVICE)) {
            whereSql += " and t.secret_level is null ";
        }
        
        // 获取 (1:机密 / 2: 绝密 / 3: 免审批 )对应订单
        if (busiOrderModel.getSecretLevel() != null) {
        	whereSql += " and t.secret_level = ? ";
            params.add(busiOrderModel.getSecretLevel());
        }

        if (StringUtils.isNotBlank(colOrderBy)) {
            orderSql += " " + colOrderBy;
        } else {
            orderSql += " order by t.id desc";
        }

        //organize SQL
        countsql = countsql.concat(whereSql);
        sql = sql.concat(whereSql).concat(orderSql);
        Pagination page = new Pagination(countsql, sql, currentPage, numPerPage, BusiOrder.class, jdbcTemplate, params.toArray());
        return page.getResult();
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public PagModel findDeptLevel(Long orgid, String json) {
        String sql = "select t.*, csu.realname orderUsername, csu.phone orderUserphone, dsu.realname driverName, dsu.phone driverPhone, "
                + "sr.template_id user_category, to_char(order_time,'yyyy-MM-dd') order_time_f, to_char(plan_st_time,'yyyy-MM-dd hh24:mi') plan_st_time_f, "
                + "to_char(plan_ed_time,'yyyy-MM-dd hh24:mi:ss') plan_ed_time_f, to_char(fact_st_time,'yyyy-MM-dd hh24:mi:ss') fact_st_time_f, "
                + "to_char(fact_ed_time,'yyyy-MM-dd hh24:mi:ss') fact_ed_time_f, bv.vehicle_model vehicle_model, "
                + "bv.vehicle_brand vehicle_brand, bv.vehicle_number vehicle_number, so.name org_name, "
                + "tt.payment_cash  from busi_order t  "
                + "left join payment_comment tt on t.id=tt.order_id "
                + "left join sys_user csu on t.order_userid=csu.id "
                + "left join sys_user dsu on t.driver_id=dsu.id "
                + "left join busi_vehicle bv on t.vehicle_id=bv.id "
                + "left join sys_organization so on t.organization_id=so.id "
                + "left join sys_role sr on csu.role_id=sr.id "
                + "where 1=1 ";
        List<Object> params = new ArrayList<Object>();
        int currentPage = 1;
        int numPerPage = 10;
        Integer orderType = 0;
        String colOrderBy = "";
        if (StringUtils.isNotBlank(json)) {
            Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
            if (jsonMap.get("currentPage") != null) {
                currentPage = Integer.valueOf(String.valueOf(jsonMap.get("currentPage")));
            }
            if (jsonMap.get("numPerPage") != null) {
                numPerPage = Integer.valueOf(String.valueOf(jsonMap.get("numPerPage")));
            }
            String orderNo = TypeUtils.obj2String(jsonMap.get("orderNo"));
            if (StringUtils.isNotBlank(orderNo)) {
                sql += " and t.order_no like " + SqlUtil.processLikeInjectionStatement(orderNo);
            }
            Date orderTimeSt = TypeUtils.obj2DateFormat(jsonMap.get("orderTime"));
            if (null != orderTimeSt) {
                Date orderTimeEd = DateUtils.addDays(orderTimeSt, 1);
                sql += " and t.order_time>? and t.order_time<?";
                params.add(orderTimeSt);
                params.add(orderTimeEd);
            }
            if (StringUtils.isNotBlank(TypeUtils.obj2String(jsonMap.get("orderCat")))) {
                Integer orderCat = TypeUtils.obj2Integer(jsonMap.get("orderCat"));
                if (orderCat == 1) {
                    sql += " and plan_st_time is not null";
                } else if (orderCat == 2) {
                    sql += " and plan_st_time is null";
                } else {
                    return null;
                }
            }
            if (StringUtils.isNotBlank(TypeUtils.obj2String(jsonMap.get("status")))) {
                Integer status = TypeUtils.obj2Integer(jsonMap.get("status"));
                sql += " and t.status=?";
                params.add(status);
            }
            Date planTime_st = TypeUtils.obj2DateFormat(jsonMap.get("planTime"));
            if (null != planTime_st) {
                Date planTime_ed = DateUtils.addDays(planTime_st, 1);
                sql += " and t.plan_st_time>? and t.plan_st_time<?";
                params.add(planTime_st);
                params.add(planTime_ed);
            }

            // filter order by order type
            if (jsonMap.get("orderType") != null && !"".equals(jsonMap.get("orderType"))) {
                orderType = TypeUtils.obj2Integer(jsonMap.get("orderType"));
                sql += " and t.order_type = ? ";
                params.add(orderType);
            }

            // for app
            Date startTime = TypeUtils.obj2DateFormat(jsonMap.get("startTime"));
            Date endTime = TypeUtils.obj2DateFormat(jsonMap.get("endTime"));
            if (null != startTime && null != endTime) {
                endTime = DateUtils.addDays(endTime, 1);
                sql += " and ((plan_st_time>=? and plan_st_time<=?) or (fact_st_time>=? and fact_st_time<=?))";
                params.add(startTime);
                params.add(endTime);
                params.add(startTime);
                params.add(endTime);
            }
            colOrderBy = TypeUtils.obj2String(jsonMap.get("colOrderBy"));
        }

        // check whether orgid is needed
        if (orderType == 0) {
            sql += " and t.organization_id=?";
            params.add(orgid);
        }
        if (StringUtils.isNotBlank(colOrderBy)) {
            sql += " " + colOrderBy;
        } else {
            sql += " order by t.id desc";
        }
        Pagination page = new Pagination(sql, currentPage, numPerPage, BusiOrder.class, jdbcTemplate, params.toArray());
        return page.getResult();
        // return jdbcTemplate.query(sql, new BeanPropertyRowMapper(BusiOrder.class), params.toArray());
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public PagModel findEmpLevel(Long userId, String json) {
        String sql = "select t.*, csu.realname orderUsername, csu.phone orderUserphone, dsu.realname driverName, dsu.phone driverPhone, "
                + "to_char(order_time,'yyyy-MM-dd') order_time_f, to_char(plan_st_time,'yyyy-MM-dd hh24:mi') plan_st_time_f, "
                + "to_char(plan_ed_time,'yyyy-MM-dd hh24:mi:ss') plan_ed_time_f, to_char(fact_st_time,'yyyy-MM-dd hh24:mi:ss') fact_st_time_f, "
                + "to_char(fact_ed_time,'yyyy-MM-dd hh24:mi:ss') fact_ed_time_f, bv.vehicle_model vehicle_model, "
                + "bv.vehicle_brand vehicle_brand, bv.vehicle_number vehicle_number, "
                + "so.name org_name, tt.payment_cash  from busi_order t  "
                + "left join payment_comment tt on t.id=tt.order_id "
                + "left join sys_user csu on t.order_userid=csu.id "
                + "left join sys_user dsu on t.driver_id=dsu.id "
                + "left join busi_vehicle bv on t.vehicle_id=bv.id "
                + "left join sys_organization so on t.organization_id=so.id "
                + "where t.order_userid=? ";
        List<Object> params = new ArrayList<Object>();
        params.add(userId);
        int currentPage = 1;
        int numPerPage = 10;
        String colOrderBy = "";
        if (StringUtils.isNotBlank(json)) {
            Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
            if (jsonMap.get("currentPage") != null) {
                currentPage = Integer.valueOf(String.valueOf(jsonMap.get("currentPage")));
            }
            if (jsonMap.get("numPerPage") != null) {
                numPerPage = Integer.valueOf(String.valueOf(jsonMap.get("numPerPage")));
            }
            String orderNo = TypeUtils.obj2String(jsonMap.get("orderNo"));
            if (StringUtils.isNotBlank(orderNo)) {
                sql += " and t.order_no like " + SqlUtil.processLikeInjectionStatement(orderNo);
            }
            Date orderTimeSt = TypeUtils.obj2DateFormat(jsonMap.get("orderTime"));
            if (null != orderTimeSt) {
                Date orderTimeEd = DateUtils.addDays(orderTimeSt, 1);
                sql += " and t.order_time>? and t.order_time<?";
                params.add(orderTimeSt);
                params.add(orderTimeEd);
            }
            if (StringUtils.isNotBlank(TypeUtils.obj2String(jsonMap.get("status")))) {
                Integer status = TypeUtils.obj2Integer(jsonMap.get("status"));
                sql += " and t.status=?";
                params.add(status);
            }
            Date planTime_st = TypeUtils.obj2DateFormat(jsonMap.get("planTime"));
            if (null != planTime_st) {
                Date planTime_ed = DateUtils.addDays(planTime_st, 1);
                sql += " and t.plan_st_time>? and t.plan_st_time<?";
                params.add(planTime_st);
                params.add(planTime_ed);
            }

            // filter order by order type
            if (jsonMap.get("orderType") != null && !"".equals(jsonMap.get("orderType"))) {
                Integer orderType = TypeUtils.obj2Integer(jsonMap.get("orderType"));
                sql += " and t.order_type = ? ";
                params.add(orderType);
            }

            // for app
            Date startTime = TypeUtils.obj2DateFormat(jsonMap.get("startTime"));
            Date endTime = TypeUtils.obj2DateFormat(jsonMap.get("endTime"));
            if (null != startTime && null != endTime) {
                endTime = DateUtils.addDays(endTime, 1);
                sql += " and ((plan_st_time>=? and plan_st_time<=?) or (fact_st_time>=? and fact_st_time<=?))";
                params.add(startTime);
                params.add(endTime);
                params.add(startTime);
                params.add(endTime);
            }
            colOrderBy = TypeUtils.obj2String(jsonMap.get("colOrderBy"));
        }
        if (StringUtils.isNotBlank(colOrderBy)) {
            sql += " " + colOrderBy;
        } else {
            sql += " order by t.id desc";
        }
        Pagination page = new Pagination(sql, currentPage, numPerPage, BusiOrder.class, jdbcTemplate, params.toArray());
        return page.getResult();
        // return jdbcTemplate.query(sql, new BeanPropertyRowMapper(BusiOrder.class), params.toArray());
    }

    @Override
    public PagModel queryOrderAsEmp(Long userId, BusiOrderQueryDto busiOrderModel) {
        String sql = "select t.*, csu.realname orderUsername, csu.phone orderUserphone, dsu.realname driverName, dsu.phone driverPhone, "
                + "to_char(order_time,'yyyy-MM-dd') order_time_f, to_char(plan_st_time,'yyyy-MM-dd hh24:mi') plan_st_time_f, "
                + "to_char(plan_ed_time,'yyyy-MM-dd hh24:mi:ss') plan_ed_time_f, to_char(fact_st_time,'yyyy-MM-dd hh24:mi:ss') fact_st_time_f, "
                + "to_char(fact_ed_time,'yyyy-MM-dd hh24:mi:ss') fact_ed_time_f, bv.vehicle_model vehicle_model, "
                + "bv.vehicle_brand vehicle_brand, bv.vehicle_number vehicle_number, "
                + "so.name org_name, tt.payment_cash  from busi_order t  "
                + "left join payment_comment tt on t.id=tt.order_id "
                + "left join sys_user csu on t.order_userid=csu.id "
                + "left join sys_user dsu on t.driver_id=dsu.id "
                + "left join busi_vehicle bv on t.vehicle_id=bv.id "
                + "left join sys_organization so on t.organization_id=so.id "
                + "where t.order_userid=? ";
        List<Object> params = new ArrayList<Object>();
        params.add(userId);
        int currentPage = 1;
        int numPerPage = 10;
        String colOrderBy = "";
        if (busiOrderModel != null) {
            if (busiOrderModel.getCurrentPage() != null) {
                currentPage = busiOrderModel.getCurrentPage();
            }
            if (busiOrderModel.getNumPerPage() != null) {
                numPerPage = busiOrderModel.getNumPerPage();
            }
            String orderNo = busiOrderModel.getOrderNo();
            if (StringUtils.isNotBlank(orderNo)) {
                sql += " and t.order_no like " + SqlUtil.processLikeInjectionStatement(orderNo);
            }
            //Date orderTimeSt = TypeUtils.obj2DateFormat(jsonMap.get("orderTime"));
            Date orderTimeSt = TypeUtils.obj2DateFormat(busiOrderModel.getOrderTime());
            if (null != orderTimeSt) {
                Date orderTimeEd = DateUtils.addDays(orderTimeSt, 1);
                sql += " and t.order_time>? and t.order_time<?";
                params.add(orderTimeSt);
                params.add(orderTimeEd);
            }

            if (busiOrderModel.getStatus() != null) {
                Integer status = TypeUtils.obj2Integer(busiOrderModel.getStatus());
                sql += " and t.status=?";
                params.add(status);
            }

            //Date planTime_st = TypeUtils.obj2DateFormat(jsonMap.get("planTime"));
            Date planTime_st = TypeUtils.obj2DateFormat(busiOrderModel.getPlanTime());
            if (null != planTime_st) {
                Date planTime_ed = DateUtils.addDays(planTime_st, 1);
                sql += " and t.plan_st_time>? and t.plan_st_time<?";
                params.add(planTime_st);
                params.add(planTime_ed);
            }

            // filter order by order type
            if (busiOrderModel.getOrderType() != null) {
                Integer orderType = busiOrderModel.getOrderType();
                sql += " and t.order_type = ? ";
                params.add(orderType);
            }

            // for app
            Date startTime = TypeUtils.obj2DateFormat(busiOrderModel.getStartTime());
            Date endTime = TypeUtils.obj2DateFormat(busiOrderModel.getEndTime());
            if (null != startTime && null != endTime) {
                endTime = DateUtils.addDays(endTime, 1);
                sql += " and ((plan_st_time>=? and plan_st_time<=?) or (fact_st_time>=? and fact_st_time<=?))";
                params.add(startTime);
                params.add(endTime);
                params.add(startTime);
                params.add(endTime);
            }
            colOrderBy = busiOrderModel.getColOrderBy();
        }

        // 特殊警务权限过滤
        if (!SecurityUtils.getSubject().isPermitted(Constants.SPECIAL_SERVICE)) {
            sql += " and t.secret_level is null ";
        }
        // 获取 (1:机密 / 2: 绝密 / 3: 免审批 )对应订单
        if (busiOrderModel.getSecretLevel() != null) {
        	sql += " and t.secret_level = ? ";
            params.add(busiOrderModel.getSecretLevel());
        }

        if (StringUtils.isNotBlank(colOrderBy)) {
            sql += " " + colOrderBy;
        } else {
            sql += " order by t.id desc";
        }
        Pagination page = new Pagination(sql, currentPage, numPerPage, BusiOrder.class, jdbcTemplate, params.toArray());
        return page.getResult();
        // return jdbcTemplate.query(sql, new BeanPropertyRowMapper(BusiOrder.class), params.toArray());
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public PagModel auditList(Long orgid, String json) {
        String sql = "select * from ("
                + "select t.*,(select realname from sys_user where id=t.order_userid) orderUsername,(select phone from sys_user where id=t.order_userid) orderUserphone,"
                + "(select realname from sys_user where id=t.driver_id) driverName,(select phone from sys_user where id=t.driver_id) driverPhone,"
                + "to_char(order_time,'yyyy-MM-dd') order_time_f,to_char(plan_st_time,'yyyy-MM-dd hh24:mi') plan_st_time_f,"
                + "to_char(plan_ed_time,'yyyy-MM-dd hh24:mi:ss') plan_ed_time_f,to_char(fact_st_time,'yyyy-MM-dd hh24:mi:ss') fact_st_time_f,"
                + "(select device_number from busi_vehicle where id=t.vehicle_id) deviceNumber,"
                + "(select station_name from busi_station where id=d.station_id) stationName,"
                + "to_char(fact_ed_time,'yyyy-MM-dd hh24:mi:ss') fact_ed_time_f,(select name from sys_organization where id=t.organization_id) org_name "
                + "from busi_order t left join sys_driver d on t.driver_id=d.id"
                + " where organization_id=? and plan_st_time is not null"
                + ") v where 1=1";
        List<Object> params = new ArrayList<Object>();
        params.add(orgid);
        int currentPage = 1;
        int numPerPage = 10;
        if (StringUtils.isNotBlank(json)) {
            Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
            if (jsonMap.get("currentPage") != null) {
                currentPage = Integer.valueOf(String.valueOf(jsonMap.get("currentPage")));
            }
            if (jsonMap.get("numPerPage") != null) {
                numPerPage = Integer.valueOf(String.valueOf(jsonMap.get("numPerPage")));
            }
            String orderUsername = TypeUtils.obj2String(jsonMap.get("orderUsername"));
            if (StringUtils.isNotBlank(orderUsername)) {
                sql += " and orderUsername like " + SqlUtil.processLikeInjectionStatement(orderUsername);
            }
            String orderNo = TypeUtils.obj2String(jsonMap.get("orderNo"));
            if (StringUtils.isNotBlank(orderNo)) {
                sql += " and order_no like " + SqlUtil.processLikeInjectionStatement(orderNo);
            }
            String queryCol = TypeUtils.obj2String(jsonMap.get("queryCol"));
            if (StringUtils.isNotBlank(queryCol)) {
                sql += " and (orderUsername like " + SqlUtil.processLikeInjectionStatement(queryCol) + " or order_no like " + SqlUtil.processLikeInjectionStatement(queryCol) + ")";
            }
            Date orderTimeSt = TypeUtils.obj2DateFormat(jsonMap.get("orderTime"));
            if (null != orderTimeSt) {
                Date orderTimeEd = DateUtils.addDays(orderTimeSt, 1);
                sql += " and order_time>? and order_time<?";
                params.add(orderTimeSt);
                params.add(orderTimeEd);
            }
            //for app:如果有查询条件就过滤待审核和已审核的数据
            if (StringUtils.isNotBlank(queryCol)) {
                sql += " and status in (0,1,2,3,13,4,5)";
            } else {
                if (StringUtils.isNotBlank(TypeUtils.obj2String(jsonMap.get("status")))) {
                    Integer status = TypeUtils.obj2Integer(jsonMap.get("status"));
                    sql += " and status=?";
                    params.add(status);
                } else {
                    sql += " and status in (1,2,3,13,4,5)";
                }
            }
            Date planTime_st = TypeUtils.obj2DateFormat(jsonMap.get("planTime"));
            if (null != planTime_st) {
                Date planTime_ed = DateUtils.addDays(planTime_st, 1);
                sql += " and plan_st_time>? and plan_st_time<?";
                params.add(planTime_st);
                params.add(planTime_ed);
            }
        }
        sql += " order by order_time asc";
        Pagination page = new Pagination(sql, currentPage, numPerPage, BusiOrder.class, jdbcTemplate, params.toArray());
        return page.getResult();
        // return jdbcTemplate.query(sql, new BeanPropertyRowMapper(BusiOrder.class), params.toArray());
    }

    @Override
    public PagModel auditList(List<Long> orgIdList, BusiOrderQueryDto busiOrderModel) {
        StringBuffer orgIdsStr = new StringBuffer();
        for (Long id : orgIdList) {
            orgIdsStr.append(id + ",");
        }
        if (orgIdsStr != null && orgIdsStr.length() > 0) {
            orgIdsStr.replace(orgIdsStr.length() - 1, orgIdsStr.length(), "");
        }
        StringBuffer sql = new StringBuffer();
        sql.append("select t1.*,t2.realname orderUsername, t2.phone orderUserphone,t3.realname driverName,t3.phone driverPhone,to_char(order_time,'yyyy-MM-dd') order_time_f,");
        sql.append("to_char(plan_st_time,'yyyy-MM-dd hh24:mi') plan_st_time_f,to_char(plan_ed_time,'yyyy-MM-dd hh24:mi:ss') plan_ed_time_f,to_char(fact_st_time,'yyyy-MM-dd hh24:mi:ss') fact_st_time_f,");
        sql.append("to_char(fact_ed_time,'yyyy-MM-dd hh24:mi:ss') fact_ed_time_f,t4.device_number deviceNumber,t6.station_name stationName,t7.name org_name");
        sql.append(" from busi_order t1 ");
        sql.append(" left join sys_user t2 on t1.order_userid=t2.id ");
        sql.append(" left join sys_user t3 on t1.driver_id=t3.id ");
        sql.append(" left join busi_vehicle t4 on t1.vehicle_id=t4.id ");
        sql.append(" left join sys_driver t5 on t1.driver_id=t5.id ");
        sql.append(" left join busi_station t6 on t5.station_id=t6.id ");
        sql.append(" left join sys_organization t7 on t1.organization_id=t7.id ");
        sql.append(" where 1=1 and t1.plan_st_time is not null ");
        List<Object> params = new ArrayList<Object>();
        int currentPage = 1;
        int numPerPage = 10;
        if (busiOrderModel != null) {
            if (busiOrderModel.getCurrentPage() != null) {
                currentPage = busiOrderModel.getCurrentPage();
            }
            if (busiOrderModel.getNumPerPage() != null) {
                numPerPage = busiOrderModel.getNumPerPage();
            }
            if (StringUtils.isNotBlank(orgIdsStr)) {
                sql.append(" and t1.organization_id in (" + orgIdsStr + ")");
            }

            if (StringUtils.isNotBlank(busiOrderModel.getOrderUsername())) {
                sql.append(" and t2.realname like " + SqlUtil.processLikeInjectionStatement(busiOrderModel.getOrderUsername()));
            }
            if (StringUtils.isNotBlank(busiOrderModel.getOrderNo())) {
                sql.append(" and t1.order_no like " + SqlUtil.processLikeInjectionStatement(busiOrderModel.getOrderNo()));
            }
            if (StringUtils.isNotBlank(busiOrderModel.getQueryCol())) {
                sql.append(" and (t2.realname like " + SqlUtil.processLikeInjectionStatement(busiOrderModel.getQueryCol()) + " or t1.order_no like " + SqlUtil.processLikeInjectionStatement(busiOrderModel.getQueryCol()) + ")");
            }
            Date orderTimeSt = TypeUtils.obj2DateFormat(busiOrderModel.getOrderTime());
            if (null != orderTimeSt) {
                Date orderTimeEd = DateUtils.addDays(orderTimeSt, 1);
                sql.append(" and t1.order_time>? and t1.order_time<?");
                params.add(orderTimeSt);
                params.add(orderTimeEd);
            }
            //for app:如果有查询条件就过滤待审核和已审核的数据
            if (StringUtils.isNotBlank(busiOrderModel.getQueryCol())) {
                sql.append(" and t1.status in (0,1,2,3,11,12,13,4,5,15)");
            } else {
                if (busiOrderModel.getStatus() != null) {
                    sql.append(" and t1.status=?");
                    params.add(busiOrderModel.getStatus());
                } else {
                    sql.append(" and t1.status in (1,2,3,11,12,13,4,5,15)");
                }
            }
            Date planTime_st = TypeUtils.obj2DateFormat(busiOrderModel.getPlanTime());
            if (null != planTime_st) {
                Date planTime_ed = DateUtils.addDays(planTime_st, 1);
                sql.append(" and t1.plan_st_time>? and t1.plan_st_time<?");
                params.add(planTime_st);
                params.add(planTime_ed);
            }
        }
        // 特殊警务权限过滤
        if (!SecurityUtils.getSubject().isPermitted(Constants.SPECIAL_SERVICE)) {
            sql.append(" and t1.secret_level is null ");
        }
        
        // 获取 (1:机密 / 2: 绝密 / 3: 免审批 )对应订单
        if (busiOrderModel.getSecretLevel() != null) {
        	sql.append(" and t.secret_level = ? ");
            params.add(busiOrderModel.getSecretLevel());
        }

        sql.append(" order by t1.order_time asc");
        Pagination page = new Pagination(sql.toString(), currentPage, numPerPage, BusiOrder.class, jdbcTemplate, params.toArray());
        return page.getResult();
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public PagModel allocateList(Long orgid, String json) {
        String sql =
                "select t.*,(select realname from sys_user where id=t.order_userid) orderUsername,(select phone from sys_user where id=t.order_userid) orderUserphone,"
                        + "(select realname from sys_user where id=t.driver_id) driverName,(select phone from sys_user where id=t.driver_id) driverPhone,"
                        + "to_char(order_time,'yyyy-MM-dd') order_time_f,to_char(plan_st_time,'yyyy-MM-dd hh24:mi') plan_st_time_f,"
                        + "to_char(plan_ed_time,'yyyy-MM-dd hh24:mi:ss') plan_ed_time_f,to_char(fact_st_time,'yyyy-MM-dd hh24:mi:ss') fact_st_time_f,"
                        + "to_char(fact_ed_time,'yyyy-MM-dd hh24:mi:ss') fact_ed_time_f,(select vehicle_model from busi_vehicle where id=t.vehicle_id) vehicle_model,"
                        + "(select name from sys_organization where id=t.organization_id) org_name,"
                        + "(select vehicle_number from busi_vehicle where id=t.vehicle_id) vehicleNumber,"
                        + "(select device_number from busi_vehicle where id=t.vehicle_id) deviceNumber,"
                        + "(select station_name from busi_station where id=d.station_id) stationName,"
                        + "tt.payment_cash from busi_order t left join payment_comment tt"
                        + " on t.id=tt.order_id left join sys_driver d on t.driver_id=d.id where  t.organization_id=?";

        List<Object> params = new ArrayList<Object>();
        params.add(orgid);
        int currentPage = 1;
        int numPerPage = 10;
        if (StringUtils.isNotBlank(json)) {
            Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
            if (jsonMap.get("currentPage") != null) {
                currentPage = Integer.valueOf(String.valueOf(jsonMap.get("currentPage")));
            }
            if (jsonMap.get("numPerPage") != null) {
                numPerPage = Integer.valueOf(String.valueOf(jsonMap.get("numPerPage")));
            }
            String orderNo = TypeUtils.obj2String(jsonMap.get("orderNo"));
            if (StringUtils.isNotBlank(orderNo)) {
                sql += " and t.order_no like " + SqlUtil.processLikeInjectionStatement(orderNo);
            }
            Date orderTimeSt = TypeUtils.obj2DateFormat(jsonMap.get("orderTime"));
            if (null != orderTimeSt) {
                Date orderTimeEd = DateUtils.addDays(orderTimeSt, 1);
                sql += " and t.order_time>? and t.order_time<?";
                params.add(orderTimeSt);
                params.add(orderTimeEd);
            }
            if (StringUtils.isNotBlank(TypeUtils.obj2String(jsonMap.get("status")))) {
                Integer status = TypeUtils.obj2Integer(jsonMap.get("status"));
                sql += " and t.status=?";
                params.add(status);
            } else {
//                sql += " and t.status in(2,3,4)";
                //CR-1831
                sql += " and t.status in(2,3,13,4)";
            }
            Date planTime_st = TypeUtils.obj2DateFormat(jsonMap.get("planTime"));
            if (null != planTime_st) {
                Date planTime_ed = DateUtils.addDays(planTime_st, 1);
                sql += " and t.plan_st_time>? and t.plan_st_time<?";
                params.add(planTime_st);
                params.add(planTime_ed);
            }
        }
        sql += " order by t.plan_st_time asc";
        Pagination page = new Pagination(sql, currentPage, numPerPage, BusiOrder.class, jdbcTemplate, params.toArray());
        return page.getResult();
        // return jdbcTemplate.query(sql, new BeanPropertyRowMapper(BusiOrder.class), params.toArray());
    }

    @Override
    public PagModel allocateList(List<Long> orgIdList, BusiOrderQueryDto busiOrderModel) {
        StringBuffer orgIdsStr = new StringBuffer();
        for (Long id : orgIdList) {
            orgIdsStr.append(id + ",");
        }
        if (orgIdsStr != null && orgIdsStr.length() > 0) {
            orgIdsStr.replace(orgIdsStr.length() - 1, orgIdsStr.length(), "");
        }

        String sql =
//            "select t.*,(select realname from sys_user where id=t.order_userid) orderUsername,(select phone from sys_user where id=t.order_userid) orderUserphone,"
//                + "(select realname from sys_user where id=t.driver_id) driverName,(select phone from sys_user where id=t.driver_id) driverPhone,"
//                + "to_char(order_time,'yyyy-MM-dd') order_time_f,to_char(plan_st_time,'yyyy-MM-dd hh24:mi') plan_st_time_f,"
//                + "to_char(plan_ed_time,'yyyy-MM-dd hh24:mi:ss') plan_ed_time_f,to_char(fact_st_time,'yyyy-MM-dd hh24:mi:ss') fact_st_time_f,"
//                + "to_char(fact_ed_time,'yyyy-MM-dd hh24:mi:ss') fact_ed_time_f,(select vehicle_model from busi_vehicle where id=t.vehicle_id) vehicle_model,"
//                + "(select name from sys_organization where id=t.organization_id) org_name,"
//                + "(select vehicle_number from busi_vehicle where id=t.vehicle_id) vehicleNumber,"
//                + "(select device_number from busi_vehicle where id=t.vehicle_id) deviceNumber,"
//                + "(select station_name from busi_station where id=d.station_id) stationName,"
//                + "tt.payment_cash from busi_order t left join payment_comment tt"
//                + " on t.id=tt.order_id left join sys_driver d on t.driver_id=d.id where  t.organization_id=?";
                "select t.*,"
                        + "csu.realname orderUsername,"
                        + "csu.phone orderUserphone,"
                        + "dsu.realname driverName,"
                        + "dsu.phone driverPhone,"
                        + "to_char(order_time,'yyyy-MM-dd') order_time_f,"
                        + "to_char(plan_st_time,'yyyy-MM-dd hh24:mi') plan_st_time_f,"
                        + "to_char(plan_ed_time,'yyyy-MM-dd hh24:mi:ss') plan_ed_time_f,"
                        + "to_char(fact_st_time,'yyyy-MM-dd hh24:mi:ss') fact_st_time_f,"
                        + "to_char(fact_ed_time,'yyyy-MM-dd hh24:mi:ss') fact_ed_time_f,"
                        + "bv.vehicle_model vehicle_model,"
                        + "bv.vehicle_number vehicleNumber,"
                        + "bv.device_number deviceNumber,"
                        + "bs.station_name stationName,"
                        + "so.name org_name,"
                        + "tt.payment_cash "
                        + "from busi_order t "
                        + "left join payment_comment tt on t.id=tt.order_id "
                        + "left join sys_user csu on t.order_userid=csu.id "
                        + "left join sys_user dsu on t.driver_id=dsu.id "
                        + "left join busi_vehicle bv on t.vehicle_id=bv.id "
                        + "left join sys_driver sd on t.driver_id=sd.id "
                        + "left join busi_station bs on sd.station_id=bs.id "
                        + "left join sys_organization so on t.organization_id=so.id "
                        + "where 1=1 ";
        List<Object> params = new ArrayList<Object>();
        int currentPage = 1;
        int numPerPage = 10;
        if (busiOrderModel != null) {
            if (busiOrderModel.getCurrentPage() != null) {
                currentPage = busiOrderModel.getCurrentPage();
            }
            if (busiOrderModel.getNumPerPage() != null) {
                numPerPage = busiOrderModel.getNumPerPage();
            }

            if (StringUtils.isNotBlank(orgIdsStr)) {
                sql += " and t.organization_id in (" + orgIdsStr + ")";
            }

            String orderNo = busiOrderModel.getOrderNo();
            if (StringUtils.isNotBlank(orderNo)) {
                sql += " and t.order_no like " + SqlUtil.processLikeInjectionStatement(orderNo);
            }

            Date orderTimeSt = TypeUtils.obj2DateFormat(busiOrderModel.getOrderTime());
            if (null != orderTimeSt) {
                Date orderTimeEd = DateUtils.addDays(orderTimeSt, 1);
                sql += " and t.order_time>? and t.order_time<?";
                params.add(orderTimeSt);
                params.add(orderTimeEd);
            }

            if (busiOrderModel.getStatus() != null) {
                Integer status = TypeUtils.obj2Integer(busiOrderModel.getStatus());
                sql += " and t.status=?";
                params.add(status);
            } else {
                //CR-1831,CR-3911
                sql += " and t.status in(2,3,13,4,11,12,15)";
            }
            Date planTime_st = TypeUtils.obj2DateFormat(busiOrderModel.getPlanTime());
            if (null != planTime_st) {
                Date planTime_ed = DateUtils.addDays(planTime_st, 1);
                sql += " and t.plan_st_time>? and t.plan_st_time<?";
                params.add(planTime_st);
                params.add(planTime_ed);
            }
        }

        // 特殊警务权限过滤
        if (!SecurityUtils.getSubject().isPermitted(Constants.SPECIAL_SERVICE)) {
            sql += " and t.secret_level is null ";
        }
        
        // 获取 (1:机密 / 2: 绝密 / 3: 免审批 )对应订单
        if (busiOrderModel.getSecretLevel() != null) {
        	sql += " and t.secret_level = ? ";
            params.add(busiOrderModel.getSecretLevel());
        }

        sql += " order by t.plan_st_time asc";
        Pagination page = new Pagination(sql, currentPage, numPerPage, BusiOrder.class, jdbcTemplate, params.toArray());
        return page.getResult();
        // return jdbcTemplate.query(sql, new BeanPropertyRowMapper(BusiOrder.class), params.toArray());
    }

    @Override
    public Integer getCountByUnCheck(Long id, boolean isEnt, Integer status) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(*) from busi_order where 1=1 ");
        sql.append("and status=? ");

        List<Object> params = new ArrayList<Object>();
        params.add(status);
        params.add(id);
        if (isEnt) {
            sql.append("and organization_id in (select id from sys_organization where parent_id =?)");
        } else {
            sql.append("and organization_id =?");

        }
        Integer count = jdbcTemplate.queryForObject(sql.toString(), Integer.class, params.toArray());

        return count;
    }

    @Override
    public void cancelOrder(Long id, String comments) {
        final String sql = "update busi_order set status=6, comments = ? where id=?";
        List<Object> params = new ArrayList<Object>();
        params.add(comments);
        params.add(id);
        jdbcTemplate.update(sql, params.toArray());
    }

    @Override
    public void pickupOrder(Long driverId, Long orderId, Long vehicleId, Long orgId) {
        final String sql =
                "update busi_order set status=2, driver_id = ?, vehicle_id=?, organization_id =? where id = ?";
        List<Object> params = new ArrayList<Object>();
        params.add(driverId);
        params.add(vehicleId);
        params.add(orgId);
        params.add(orderId);
        jdbcTemplate.update(sql, params.toArray());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public BusiOrder queryBusiOrderByVehicleId(Long vehicleId) {
        String sql = "select t2.id, t2.realname driverName, t2.phone driverPhone " + "from busi_order t1, sys_user t2 "
                + "where t1.fact_st_time < now() and t1.status = 3 and t1.vehicle_id = ? and t1.driver_id = t2.id";
        List<BusiOrder> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper(BusiOrder.class), vehicleId);
        if (null != list && list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public List<BusiOrder> queryVehicleSchedule(Long vehicleId, String planStTime) {
        String sql = "select t.* "
                // + "TO_CHAR(plan_ed_time,'yyyy-MM-dd hh24:mi:ss') planEdTimeF "
                + "from busi_order t " + "where vehicle_id = ? and status = 2 "
//            + "and TO_CHAR(plan_st_time,'yyyy-MM-dd') = TO_CHAR(?,'yyyy-MM-dd') "
//            + "and TO_CHAR(plan_ed_time,'yyyy-MM-dd') = TO_CHAR(?,'yyyy-MM-dd') ";
                + "and TO_CHAR(plan_st_time,'yyyy-MM-dd') = ? "
                + "and TO_CHAR(plan_ed_time,'yyyy-MM-dd') = ? ";
        List<BusiOrder> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper(BusiOrder.class), vehicleId, planStTime, planStTime);
        return list;
    }

    @Override
    public void updateOrderStatus(Integer status, Long order_id) {
        String sql = "update busi_order set status = ? where id = ?";
        jdbcTemplate.update(sql, status, order_id);
    }
    public void updateVehReturnRegis(Long orderId,Long edMileage,Date factEdTime,Long dispatcherId,Double durationTime,Long factMileag,Integer orderStaus,Integer returnType) {
        final String sql = "update busi_order set fact_ed_time=?, ed_mileage=?, fact_duration_time=?, fact_mileage=?, dispatcher_id=?, status=?, return_type=?  where id=?";
        jdbcTemplate.update(sql, factEdTime,edMileage,durationTime,factMileag,dispatcherId,orderStaus,returnType,orderId);
    }

    @Override
    public List<BusiOrder> listFinishedOrderByDepId(List<Long> orgIds, Date startDate, Date endDate) {
        StringBuffer orgIdsStr = new StringBuffer();
        for (Long id : orgIds) {
            orgIdsStr.append(id + ",");
        }
        if (orgIdsStr != null && orgIdsStr.length() > 0) {
            orgIdsStr.replace(orgIdsStr.length() - 1, orgIdsStr.length(), "");
        }

        String sql = "select * from busi_order t where t.status=16 ";
        List<Object> params = new ArrayList<Object>();

        if (StringUtils.isNotBlank(orgIdsStr)) {
            sql += " and t.organization_id in (" + orgIdsStr + ")";
        }

        if (startDate != null) {
            sql += " and t.order_time>?";
            params.add(startDate);
        }

        if (endDate != null) {
            Date planTime_ed = DateUtils.addDays(endDate, 1);
            sql += " and t.order_time<?";
            params.add(planTime_ed);
        }

        return jdbcTemplate.query(sql,
                new BeanPropertyRowMapper(BusiOrder.class), params.toArray());
    }

    @SuppressWarnings("unchecked")
    @Override
    public PagModel findOrderAsDriverLevel(Long userId, String json) {
        String sql = "select t.*, "
                + "csu.realname orderUsername, "
                + "csu.phone orderUserphone, "
                + "dsu.realname driverName, "
                + "dsu.phone driverPhone, "
                + "to_char(order_time,'yyyy-MM-dd') order_time_f, "
                + "to_char(plan_st_time,'yyyy-MM-dd hh24:mi') plan_st_time_f, "
                + "to_char(plan_ed_time,'yyyy-MM-dd hh24:mi:ss') plan_ed_time_f, "
                + "to_char(fact_st_time,'yyyy-MM-dd hh24:mi:ss') fact_st_time_f, "
                + "to_char(fact_ed_time,'yyyy-MM-dd hh24:mi:ss') fact_ed_time_f, "
                + "bv.vehicle_model vehicle_model, "
                + "bv.vehicle_brand vehicle_brand, "
                + "bv.vehicle_number vehicle_number, "
                + "so.name org_name, "
                + "tt.payment_cash  "
                + "from busi_order t  "
                + "left join payment_comment tt on t.id=tt.order_id "
                + "left join sys_user csu on t.order_userid=csu.id "
                + "left join sys_user dsu on t.driver_id=dsu.id "
                + "left join busi_vehicle bv on t.vehicle_id=bv.id "
                + "left join sys_organization so on t.organization_id=so.id "
                + "where (t.driver_id = ? or (t.order_type = 1 and t.status =1)) ";
        List<Object> params = new ArrayList<Object>();
        params.add(userId);
        int currentPage = 1;
        int numPerPage = 10;
        if (StringUtils.isNotBlank(json)) {
            Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
            if (jsonMap.get("currentPage") != null) {
                currentPage = Integer.valueOf(String.valueOf(jsonMap.get("currentPage")));
            }
            if (jsonMap.get("numPerPage") != null) {
                numPerPage = Integer.valueOf(String.valueOf(jsonMap.get("numPerPage")));
            }
            String orderNo = TypeUtils.obj2String(jsonMap.get("orderNo"));
            if (StringUtils.isNotBlank(orderNo)) {
                sql += " and order_no like " + SqlUtil.processLikeInjectionStatement(orderNo);
            }
            Date orderTimeSt = TypeUtils.obj2DateFormat(jsonMap.get("orderTime"));
            if (null != orderTimeSt) {
                Date orderTimeEd = DateUtils.addDays(orderTimeSt, 1);
                sql += " and t.order_time>? and t.order_time<?";
                params.add(orderTimeSt);
                params.add(orderTimeEd);
            }
            if (StringUtils.isNotBlank(TypeUtils.obj2String(jsonMap.get("status")))) {
                Integer status = TypeUtils.obj2Integer(jsonMap.get("status"));
                sql += " and status=?";
                params.add(status);
            }
            Date planTime_st = TypeUtils.obj2DateFormat(jsonMap.get("planTime"));
            if (null != planTime_st) {
                Date planTime_ed = DateUtils.addDays(planTime_st, 1);
                sql += " and t.plan_st_time>? and t.plan_st_time<?";
                params.add(planTime_st);
                params.add(planTime_ed);
            }
            // filter order by order type
            if (jsonMap.get("orderType") != null && !"".equals(jsonMap.get("orderType"))) {
                Integer orderType = TypeUtils.obj2Integer(jsonMap.get("orderType"));
                sql += " and t.order_type = ? ";
                params.add(orderType);
            }

            // for app
            Date startTime = TypeUtils.obj2DateFormat(jsonMap.get("startTime"));
            Date endTime = TypeUtils.obj2DateFormat(jsonMap.get("endTime"));
            if (null != startTime && null != endTime) {
                endTime = DateUtils.addDays(endTime, 1);
                sql += " and ((plan_st_time>=? and plan_st_time<=?) or (fact_st_time>=? and fact_st_time<=?))";
                params.add(startTime);
                params.add(endTime);
                params.add(startTime);
                params.add(endTime);
            }
        }
        sql += " order by id desc";
        Pagination page = new Pagination(sql, currentPage, numPerPage, BusiOrder.class, jdbcTemplate, params.toArray());
        return page.getResult();
    }

    @Override
    public PagModel queryOrderAsDriver(Long userId, BusiOrderQueryDto busiOrderModel) {
        String sql =
//            "select t.*,"
//        		+ "(select realname from sys_user where id=t.order_userid) orderUsername,"
//            	+ "(select phone from sys_user where id=t.order_userid) orderUserphone,"
//                + "(select realname from sys_user where id=t.driver_id) driverName,"
//                + "(select phone from sys_user where id=t.driver_id) driverPhone,"
//            	+ "(select vehicle_model from busi_vehicle where id=t.vehicle_id) vehicle_model,"
//            	+ "(select vehicle_brand from busi_vehicle where id=t.vehicle_id) vehicle_brand,"
//            	+ "(select vehicle_number from busi_vehicle where id=t.vehicle_id) vehicle_number,"
//                + "to_char(order_time,'yyyy-MM-dd') order_time_f,"
//                + "to_char(plan_st_time,'yyyy-MM-dd hh24:mi') plan_st_time_f,"
//                + "to_char(plan_ed_time,'yyyy-MM-dd hh24:mi:ss') plan_ed_time_f,"
//                + "to_char(fact_st_time,'yyyy-MM-dd hh24:mi:ss') fact_st_time_f,"
//                + "to_char(fact_ed_time,'yyyy-MM-dd hh24:mi:ss') fact_ed_time_f,"
//                + "(select name from sys_organization where id=t.organization_id) org_name,"
//                + "tt.payment_cash from busi_order t left join payment_comment tt"
//                + " on t.id=tt.order_id where (t.driver_id = ? or (t.order_type = 1 and t.status =1)) ";
                "select t.*, "
                        + "csu.realname orderUsername, "
                        + "csu.phone orderUserphone, "
                        + "dsu.realname driverName, "
                        + "dsu.phone driverPhone, "
                        + "to_char(order_time,'yyyy-MM-dd') order_time_f, "
                        + "to_char(plan_st_time,'yyyy-MM-dd hh24:mi') plan_st_time_f, "
                        + "to_char(plan_ed_time,'yyyy-MM-dd hh24:mi:ss') plan_ed_time_f, "
                        + "to_char(fact_st_time,'yyyy-MM-dd hh24:mi:ss') fact_st_time_f, "
                        + "to_char(fact_ed_time,'yyyy-MM-dd hh24:mi:ss') fact_ed_time_f, "
                        + "bv.vehicle_model vehicle_model, "
                        + "bv.vehicle_brand vehicle_brand, "
                        + "bv.vehicle_number vehicle_number, "
                        + "so.name org_name, "
                        + "tt.payment_cash  "
                        + "from busi_order t  "
                        + "left join payment_comment tt on t.id=tt.order_id "
                        + "left join sys_user csu on t.order_userid=csu.id "
                        + "left join sys_user dsu on t.driver_id=dsu.id "
                        + "left join busi_vehicle bv on t.vehicle_id=bv.id "
                        + "left join sys_organization so on t.organization_id=so.id "
                        + "where (t.driver_id = ? or (t.order_type = 1 and t.status =1)) ";
        List<Object> params = new ArrayList<Object>();
        params.add(userId);
        int currentPage = 1;
        int numPerPage = 10;
        if (busiOrderModel != null) {
            ;
            if (busiOrderModel.getCurrentPage() != null) {
                currentPage = busiOrderModel.getCurrentPage();
            }
            if (busiOrderModel.getNumPerPage() != null) {
                numPerPage = busiOrderModel.getNumPerPage();
            }
            String orderNo = busiOrderModel.getOrderNo();
            if (StringUtils.isNotBlank(orderNo)) {
                sql += " and order_no like " + SqlUtil.processLikeInjectionStatement(orderNo);
            }
            Date orderTimeSt = TypeUtils.obj2DateFormat(busiOrderModel.getOrderTime());
            if (null != orderTimeSt) {
                Date orderTimeEd = DateUtils.addDays(orderTimeSt, 1);
                sql += " and t.order_time>? and t.order_time<?";
                params.add(orderTimeSt);
                params.add(orderTimeEd);
            }
            if (busiOrderModel.getStatus() != null) {
                Integer status = TypeUtils.obj2Integer(busiOrderModel.getStatus());
                sql += " and status=?";
                params.add(status);
            }
            Date planTime_st = TypeUtils.obj2DateFormat(busiOrderModel.getPlanTime());
            if (null != planTime_st) {
                Date planTime_ed = DateUtils.addDays(planTime_st, 1);
                sql += " and t.plan_st_time>? and t.plan_st_time<?";
                params.add(planTime_st);
                params.add(planTime_ed);
            }
            // filter order by order type
            if (busiOrderModel.getOrderType() != null) {
                Integer orderType = busiOrderModel.getOrderType();
                sql += " and t.order_type = ? ";
                params.add(orderType);
            }

            // 特殊警务权限过滤
            if (!SecurityUtils.getSubject().isPermitted(Constants.SPECIAL_SERVICE)) {
                sql += " and t.secret_level is null ";
            }
            
            // 获取 (1:机密 / 2: 绝密 / 3: 免审批 )对应订单
            if (busiOrderModel.getSecretLevel() != null) {
            	sql += " and t.secret_level = ? ";
                params.add(busiOrderModel.getSecretLevel());
            }

            // for app
            Date startTime = TypeUtils.obj2DateFormat(busiOrderModel.getStartTime());
            Date endTime = TypeUtils.obj2DateFormat(busiOrderModel.getEndTime());
            if (null != startTime && null != endTime) {
                endTime = DateUtils.addDays(endTime, 1);
                sql += " and ((plan_st_time>=? and plan_st_time<=?) or (fact_st_time>=? and fact_st_time<=?))";
                params.add(startTime);
                params.add(endTime);
                params.add(startTime);
                params.add(endTime);
            }
        }
        sql += " order by id desc";
        Pagination page = new Pagination(sql, currentPage, numPerPage, BusiOrder.class, jdbcTemplate, params.toArray());
        return page.getResult();
    }

    public void ignoreOrder(final User loginUser, final Long id) {
        final String sql =
                "insert into busi_order_ignore(order_id,driver_id,time) values(?,?,?)";
        final GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection)
                    throws SQLException {
                PreparedStatement psst = connection.prepareStatement(sql, new String[]{"id"}); //NOSONAR statement and conn already been closed in jdbcTemplate update method
                int count = 1;
                psst.setLong(count++, id);
                psst.setLong(count++, loginUser.getId());
                psst.setTimestamp(count, new java.sql.Timestamp(new Date().getTime()));

                return psst;
            }
        }, keyHolder);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public List<BusiOrderIgnore> queryIgnoreOrderByDriverId(Long driverId) {
        String sql = "select a.* from busi_order_ignore a where driver_id = ? and time > now() + '-1day'";
        List<BusiOrderIgnore> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper(BusiOrderIgnore.class), driverId);
        if (list != null && list.size() > 0) {
            return list;
        }
        return null;
    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public Boolean checkIgnoreOrderByDriverId(Long driverId, Long orderId) {
        String sql = "select a.* from busi_order_ignore a where driver_id = ? and order_id = ?";
        List<BusiOrderIgnore> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper(BusiOrderIgnore.class), driverId, orderId);
        if (list != null && list.size() > 0) {
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public PagModel orderListCurrentForDriver(Long driverId, String json) {
        String sql =
//	            "select t.*,(select realname from sys_user where id=t.order_userid) orderUsername,(select phone from sys_user where id=t.order_userid) orderUserphone,"
//	                + "(select realname from sys_user where id=t.driver_id) driverName,(select phone from sys_user where id=t.driver_id) driverPhone,"
//	                + "to_char(order_time,'yyyy-MM-dd') order_time_f,to_char(plan_st_time,'yyyy-MM-dd hh24:mi') plan_st_time_f,"
//	                + "to_char(plan_ed_time,'yyyy-MM-dd hh24:mi:ss') plan_ed_time_f,to_char(fact_st_time,'yyyy-MM-dd hh24:mi:ss') fact_st_time_f,"
//	                + "to_char(fact_ed_time,'yyyy-MM-dd hh24:mi:ss') fact_ed_time_f,(select name from sys_organization where id=t.organization_id) org_name,"
//	                + "(select vehicle_number from busi_vehicle where id=t.vehicle_id) vehicleNumber,(select vehicle_model from busi_vehicle where id=t.vehicle_id) vehicleModel,(select vehicle_brand from busi_vehicle where id=t.vehicle_id) vehicleBrand,"
//	                + "tt.payment_cash from busi_order t left join payment_comment tt"
//	                + " on t.id=tt.order_id "
                "select t.*, csu.realname orderUsername, csu.phone orderUserphone, dsu.realname driverName, dsu.phone driverPhone, "
                        + "to_char(order_time,'yyyy-MM-dd') order_time_f, to_char(plan_st_time,'yyyy-MM-dd hh24:mi') plan_st_time_f, "
                        + "to_char(plan_ed_time,'yyyy-MM-dd hh24:mi:ss') plan_ed_time_f, to_char(fact_st_time,'yyyy-MM-dd hh24:mi:ss') fact_st_time_f, "
                        + "to_char(fact_ed_time,'yyyy-MM-dd hh24:mi:ss') fact_ed_time_f, bv.vehicle_model vehicle_model, bv.vehicle_brand vehicle_brand, "
                        + "bv.vehicle_number vehicle_number, bv.device_number device_number, so.name org_name, "
                        + "tt.payment_cash,tt.comment,tt.comment_level  from busi_order t  "
                        + "left join payment_comment tt on t.id=tt.order_id "
                        + "left join sys_user csu on t.order_userid=csu.id "
                        + "left join sys_user dsu on t.driver_id=dsu.id "
                        + "left join busi_vehicle bv on t.vehicle_id=bv.id "
                        + "left join sys_organization so on t.organization_id=so.id "
                        + " where t.driver_id = ? and t.status in(2,11,12,3,13) ";
        List<Object> params = new ArrayList<Object>();
        params.add(driverId);
        int currentPage = 1;
        int numPerPage = 10;
        if (StringUtils.isNotBlank(json)) {
            Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
            if (jsonMap.get("currentPage") != null) {
                currentPage = Integer.valueOf(String.valueOf(jsonMap.get("currentPage")));
            }
            if (jsonMap.get("numPerPage") != null) {
                numPerPage = Integer.valueOf(String.valueOf(jsonMap.get("numPerPage")));
            }
        }
        sql += " order by plan_st_time asc";
        Pagination page = new Pagination(sql, currentPage, numPerPage, BusiOrder.class, jdbcTemplate, params.toArray());
        return page.getResult();
    }


    @Override
    public PagModel orderListCurrentForDriver(Long driverId, Integer currentPage, Integer numPerPage) {
        String sql =
                "select t.*, csu.realname orderUsername, csu.phone orderUserphone, dsu.realname driverName, dsu.phone driverPhone, "
                        + "to_char(order_time,'yyyy-MM-dd') order_time_f, to_char(plan_st_time,'yyyy-MM-dd hh24:mi') plan_st_time_f, "
                        + "to_char(plan_ed_time,'yyyy-MM-dd hh24:mi:ss') plan_ed_time_f, to_char(fact_st_time,'yyyy-MM-dd hh24:mi:ss') fact_st_time_f, "
                        + "to_char(fact_ed_time,'yyyy-MM-dd hh24:mi:ss') fact_ed_time_f, bv.vehicle_model vehicle_model, bv.vehicle_brand vehicle_brand, "
                        + "bv.vehicle_number vehicle_number, bv.device_number device_number, so.name org_name, "
                        + "tt.payment_cash,tt.comment,tt.comment_level  from busi_order t  "
                        + "left join payment_comment tt on t.id=tt.order_id "
                        + "left join sys_user csu on t.order_userid=csu.id "
                        + "left join sys_user dsu on t.driver_id=dsu.id "
                        + "left join busi_vehicle bv on t.vehicle_id=bv.id "
                        + "left join sys_organization so on t.organization_id=so.id "
                        + " where t.driver_id = ? and t.status in(2,11,12,3,13) ";
        List<Object> params = new ArrayList<>();
        params.add(driverId);
        currentPage = currentPage == null ? 1 : currentPage;
        numPerPage = numPerPage == null ? 10 : numPerPage;
        sql += " order by plan_st_time asc";
        Pagination page = new Pagination(sql, currentPage, numPerPage, BusiOrder.class, jdbcTemplate, params.toArray());
        return page.getResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public PagModel orderListHistoryForDriver(Long driverId, String json) {
        String sql =
//	            "select t.*,(select realname from sys_user where id=t.order_userid) orderUsername,(select phone from sys_user where id=t.order_userid) orderUserphone,"
//	                + "(select realname from sys_user where id=t.driver_id) driverName,(select phone from sys_user where id=t.driver_id) driverPhone,"
//	                + "to_char(order_time,'yyyy-MM-dd') order_time_f,to_char(plan_st_time,'yyyy-MM-dd hh24:mi') plan_st_time_f,"
//	                + "to_char(plan_ed_time,'yyyy-MM-dd hh24:mi:ss') plan_ed_time_f,to_char(fact_st_time,'yyyy-MM-dd hh24:mi:ss') fact_st_time_f,"
//	                + "to_char(fact_ed_time,'yyyy-MM-dd hh24:mi:ss') fact_ed_time_f,(select name from sys_organization where id=t.organization_id) org_name,"
//	                + "(select vehicle_number from busi_vehicle where id=t.vehicle_id) vehicleNumber,(select vehicle_brand from busi_vehicle where id=t.vehicle_id) vehicleBrand,(select vehicle_model from busi_vehicle where id=t.vehicle_id) vehicleModel,"
//	                + "tt.payment_cash from busi_order t left join payment_comment tt"
//	                + " on t.id=tt.order_id "
                "select t.*, csu.realname orderUsername, csu.phone orderUserphone, dsu.realname driverName, dsu.phone driverPhone, "
                        + "to_char(order_time,'yyyy-MM-dd') order_time_f, to_char(plan_st_time,'yyyy-MM-dd hh24:mi') plan_st_time_f, "
                        + "to_char(plan_ed_time,'yyyy-MM-dd hh24:mi:ss') plan_ed_time_f, to_char(fact_st_time,'yyyy-MM-dd hh24:mi:ss') fact_st_time_f, "
                        + "to_char(fact_ed_time,'yyyy-MM-dd hh24:mi:ss') fact_ed_time_f, bv.vehicle_model vehicle_model, bv.vehicle_brand vehicle_brand, "
                        + "bv.vehicle_number vehicle_number, bv.device_number device_number, so.name org_name, "
                        + "tt.payment_cash,tt.comment,tt.comment_level  from busi_order t  "
                        + "left join payment_comment tt on t.id=tt.order_id "
                        + "left join sys_user csu on t.order_userid=csu.id "
                        + "left join sys_user dsu on t.driver_id=dsu.id "
                        + "left join busi_vehicle bv on t.vehicle_id=bv.id "
                        + "left join sys_organization so on t.organization_id=so.id "
                        + "where t.driver_id = ? and t.status in(4,15,16) ";
        List<Object> params = new ArrayList<Object>();
        params.add(driverId);
        int currentPage = 1;
        int numPerPage = 10;
        if (StringUtils.isNotBlank(json)) {
            Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
            if (jsonMap.get("currentPage") != null) {
                currentPage = Integer.valueOf(String.valueOf(jsonMap.get("currentPage")));
            }
            if (jsonMap.get("numPerPage") != null) {
                numPerPage = Integer.valueOf(String.valueOf(jsonMap.get("numPerPage")));
            }
        }
        sql += " order by fact_st_time desc";
        Pagination page = new Pagination(sql, currentPage, numPerPage, BusiOrder.class, jdbcTemplate, params.toArray());
        return page.getResult();
    }

    @Override
    public PagModel orderListHistoryForDriver(Long driverId, Integer currentPage, Integer numPerPage) {
        String sql =
                "select t.*, csu.realname orderUsername, csu.phone orderUserphone, dsu.realname driverName, dsu.phone driverPhone, "
                        + "to_char(order_time,'yyyy-MM-dd') order_time_f, to_char(plan_st_time,'yyyy-MM-dd hh24:mi') plan_st_time_f, "
                        + "to_char(plan_ed_time,'yyyy-MM-dd hh24:mi:ss') plan_ed_time_f, to_char(fact_st_time,'yyyy-MM-dd hh24:mi:ss') fact_st_time_f, "
                        + "to_char(fact_ed_time,'yyyy-MM-dd hh24:mi:ss') fact_ed_time_f, bv.vehicle_model vehicle_model, bv.vehicle_brand vehicle_brand, "
                        + "bv.vehicle_number vehicle_number, bv.device_number device_number, so.name org_name, "
                        + "tt.payment_cash,tt.comment,tt.comment_level  from busi_order t  "
                        + "left join payment_comment tt on t.id=tt.order_id "
                        + "left join sys_user csu on t.order_userid=csu.id "
                        + "left join sys_user dsu on t.driver_id=dsu.id "
                        + "left join busi_vehicle bv on t.vehicle_id=bv.id "
                        + "left join sys_organization so on t.organization_id=so.id "
                        + "where t.driver_id = ? and t.status in(4,15,16) ";
        List<Object> params = new ArrayList<>();
        params.add(driverId);
        currentPage = currentPage == null ? 1 : currentPage;
        numPerPage = numPerPage == null ? 10 : numPerPage;
        sql += " order by fact_st_time desc";
        Pagination page = new Pagination(sql, currentPage, numPerPage, BusiOrder.class, jdbcTemplate, params.toArray());
        return page.getResult();
    }

    @Override
    public List<BusiOrder> driverHasUnfinishedOrder(Long[] driverIdArr) {
        StringBuffer sql = new StringBuffer();
        sql.append("select o.id, o.order_no, o.status, o.driver_id, u.realname driver_name, u.phone driver_phone, ");
        sql.append("o.order_userid, ou.realname order_username, ou.phone order_userphone ");
        sql.append("from busi_order o ");
        sql.append("LEFT JOIN sys_user u on u.id = o.driver_id ");
        sql.append("LEFT JOIN sys_user ou on ou.id = o.order_userid ");
        sql.append("where o.status in (2,11,12,3,13,4,15) ");
        List<Object> params = new ArrayList<Object>();
        StringBuffer preparams = new StringBuffer();
        for (int i = 0, num = driverIdArr.length; i < num; i++) {
            if (i == num - 1) {
                preparams.append("?");
            } else {
                preparams.append("?,");
            }
            params.add(driverIdArr[i]);
        }
        sql.append("and o.driver_id in (").append(preparams).append(")");

        List<BusiOrder> vehicleIdDriverId = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<BusiOrder>(BusiOrder.class), params.toArray());
        return vehicleIdDriverId;
    }

    @Override
    public List<BusiOrder> empHasUnfinishedOrder(Long[] userIdArr) {
        StringBuffer sql = new StringBuffer();
        sql.append("select o.id, o.order_no, o.status, o.driver_id, u.realname driver_name, u.phone driver_phone, ");
        sql.append("o.order_userid, ou.realname order_username, ou.phone order_userphone ");
        sql.append("from busi_order o ");
        sql.append("LEFT JOIN sys_user u on u.id = o.driver_id ");
        sql.append("LEFT JOIN sys_user ou on ou.id = o.order_userid ");
        sql.append("where o.status in (0,1,2,11,12,3,13) ");
        List<Object> params = new ArrayList<Object>();
        StringBuffer preparams = new StringBuffer();
        for (int i = 0, num = userIdArr.length; i < num; i++) {
            if (i == num - 1) {
                preparams.append("?");
            } else {
                preparams.append("?,");
            }
            params.add(userIdArr[i]);
        }
        sql.append("and o.order_userid in (").append(preparams).append(")");

        List<BusiOrder> vehicleIdDriverId = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<BusiOrder>(BusiOrder.class), params.toArray());
        return vehicleIdDriverId;
    }

	@Override
	public VehReturnRegistModel findVehReturnRegistByOrderNo(Long id) {
		String sql = "select t.*, csu.realname orderUsername, csu.phone orderUserphone, dsu.realname driverName, dsu.phone driverPhone, "
                + "to_char(order_time,'yyyy-MM-dd') order_time_f, to_char(plan_st_time,'yyyy-MM-dd hh24:mi') plan_st_time_f, "
                + "to_char(plan_ed_time,'yyyy-MM-dd hh24:mi:ss') plan_ed_time_f, to_char(fact_st_time,'yyyy-MM-dd hh24:mi:ss') fact_st_time_f, "
                + "to_char(fact_ed_time,'yyyy-MM-dd hh24:mi:ss') fact_ed_time_f, bv.seat_number seatNumber, "
                + "bv.vehicle_number vehicleNumber, so.name orgName, auditor.realname auditUserName, "
                + "ar.status auditStatus,station.position parkingSite, dispatcher.realname dispatcherName "
                + "from busi_order t "
                + "left join sys_user csu on t.order_userid=csu.id "
                + "left join sys_user dsu on t.driver_id=dsu.id "
                + "left join busi_vehicle bv on t.vehicle_id=bv.id "
                + "left join busi_order_audit_record ar on ar.order_id=t.id "
                + "left join sys_organization so on t.organization_id=so.id "
                + "left join sys_user auditor on ar.audit_user_id=auditor.id "
                + "left join busi_vehicle_station vst on vst.vehicle_id=t.vehicle_id "
                + "left join busi_station station on station.id=vst.station_id  "
                + "left join sys_user dispatcher on t.dispatcher_id=dispatcher.id  "
                + "where t.id=? and (ar.audit_time=(select max(audit_time) from busi_order_audit_record where order_id=?) or ar.audit_time is null) ";
        List<VehReturnRegistModel> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(VehReturnRegistModel.class), id, id);
        return list.isEmpty() ?  null : list.get(0);
	}

}
