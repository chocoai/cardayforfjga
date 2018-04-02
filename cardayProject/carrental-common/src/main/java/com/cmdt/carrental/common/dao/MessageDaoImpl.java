package com.cmdt.carrental.common.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cmdt.carrental.common.constants.Constants;
import com.cmdt.carrental.common.entity.Message;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.model.DateCountModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.service.MessageServiceImpl;
import com.cmdt.carrental.common.util.BusinessConstants;
import com.cmdt.carrental.common.util.Pagination;
import com.cmdt.carrental.common.util.SqlUtil;

@Repository
public class MessageDaoImpl implements MessageDao {
	
	private static final Logger LOG = LoggerFactory.getLogger(MessageServiceImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<DateCountModel> getMsgCountByUser(User user, Long orgId,List<Long> list, Organization topOrg) {
		LOG.debug("MessageDaoImpl.getMsgCountByUser["+orgId+"]");
	    StringBuffer buffer = new StringBuffer();
	 //   buffer.append(" select type as date, count(*) as value ");
	    buffer.append("select f.new_type as date,count(*) as value");
	    buffer.append(" from (");
	    buffer.append(" select e.*,( CASE WHEN type in (");
	    buffer.append(" 'OVERSPEED','VEHICLEBACK','OUTBOUND','VIOLATE') THEN 'ABNORMAL'");
	    buffer.append(" WHEN type = 'TRAVEL' THEN 'TRIP' WHEN type = 'TASK' THEN 'TRIP' ELSE type END) new_type");
	    buffer.append(" from (");
	    buffer.append(" select distinct a.id,a.type");
	    buffer.append(" from message a");
	    buffer.append(" left join busi_order b");
	    buffer.append(" on a.order_id=b.id");
	    buffer.append(" left join read_message c");
	    buffer.append(" on c.message_id=a.id");
	    buffer.append(" where 1=1"); 
	    List<Object> params = new ArrayList<Object>();
	    StringBuffer orgIdsStr = new StringBuffer();
	    for(Long id : list){
	    	orgIdsStr.append(id+",");
	    }
	    if(orgIdsStr.length()>0){
	    	orgIdsStr.replace(orgIdsStr.length()-1, orgIdsStr.length(), "");
	    }
	    if(user.isDriver()){
	    	buffer.append(" and ((a.type='TASK' and b.driver_id=?)"); //来自于属于司机的消息列表
	    	buffer.append(" or (a.type = 'SYSTEM' and a.org_id = ?))");
			params.add(user.getId());
			params.add(topOrg.getId());
		}else if(user.isEndUser()){
			buffer.append(" and ((a.type='TRAVEL' and b.order_userid=?)"); //来自于属于员工的消息列表
			buffer.append(" or (a.type = 'SYSTEM' and a.org_id = ?))");
			params.add(user.getId());
			params.add(topOrg.getId());
			
		}else if(user.isEntAdmin()){
			//企业管理员
			buffer.append(" and (a.org_id in (").append(orgIdsStr).append("))");
			buffer.append(" and type in ('SYSTEM','OVERSPEED','VEHICLEBACK','OUTBOUND','MAINTAIN','VIOLATE')");
			
		}else{
			//部门管理员
			buffer.append(" and ((a.type='TRAVEL' and b.order_userid=?)");
			buffer.append(" or (a.type='SYSTEM' and a.org_id=?)");
			buffer.append(" or (a.type in('OVERSPEED','VEHICLEBACK','OUTBOUND','MAINTAIN','VIOLATE') and a.org_id in (").append(orgIdsStr).append(")))");
			params.add(user.getId());
			params.add(topOrg.getId());
		}
	    buffer.append(" and a.id not in (select message_id from read_message where user_id=?)");
	    params.add(user.getId());
	    buffer.append(" ) e");
	    buffer.append(" ) f group by f.new_type");	
		List<DateCountModel> retList = jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper<DateCountModel>(DateCountModel.class), params.toArray() );
	    if(retList.isEmpty()) {
	        return null;
	    }
	    return retList;
	}

	@Override
	public PagModel getMessageByUser(User user, String msgType, Integer currentPage, Integer pageSize,List<Long> list,Organization topOrg) {
		LOG.debug("MessageDaoImpl.getMessageByUser["+user+"]");
		List<Object> params = new ArrayList<Object>();
		StringBuilder sb=new StringBuilder();
		sb.append("select a.id,type,car_no as carNo,time,org_id as orgId,location,is_end as isEnd,warning_id as warningId,msg,order_id,title from message a ");
		sb.append("left join busi_order b ");
		sb.append("on a.order_id=b.id ");
		sb.append("where 1=1 ");
	//	sb.append(" and (org_id is null or org_id = ? or org_id in (select id from sys_organization where parent_id = ?)) ");		
	//	params.add(user.getOrganizationId());
	//	params.add(user.getOrganizationId());
		if (msgType != null) {
			StringBuffer orgIdsStr = new StringBuffer();
		    for(Long id : list){
		    	orgIdsStr.append(id+",");
		    }
		    if(orgIdsStr.length()>0){
		    	orgIdsStr.replace(orgIdsStr.length()-1, orgIdsStr.length(), "");
		    }
			if(user.isDriver()){
				if(BusinessConstants.MSG_TYPE_TRIP.equalsIgnoreCase(msgType)){
					sb.append(" and a.type = 'TASK' ");
					sb.append(" and b.driver_id=?"); //来自于属于员工的消息列表
					params.add(user.getId());
				}else if(BusinessConstants.MSG_TYPE_SYSTEM.equalsIgnoreCase(msgType)){
					sb.append(" and a.type = 'SYSTEM' ");
					sb.append(" and a.org_id = ?");
					params.add(topOrg.getId());
				}else if(BusinessConstants.MSG_TYPE_ALL.equalsIgnoreCase(msgType)){
					sb.append(" and (a.type='TASK' and b.driver_id=?)"); //来自于属于司机的消息列表
					sb.append(" or (a.type = 'SYSTEM' and a.org_id =?)");
					params.add(user.getId());
					params.add(topOrg.getId());
					
				}
			}else if(user.isEndUser()){
				if(BusinessConstants.MSG_TYPE_TRIP.equalsIgnoreCase(msgType)){
					sb.append(" and a.type = 'TRAVEL'");
					sb.append(" and b.order_userid=?"); //来自于属于员工的消息列表
					params.add(user.getId());
				}else if(BusinessConstants.MSG_TYPE_SYSTEM.equalsIgnoreCase(msgType)){
					sb.append(" and a.type = 'SYSTEM'");
					sb.append(" and a.org_id = ?");
					params.add(topOrg.getId());
				}else if(BusinessConstants.MSG_TYPE_ALL.equalsIgnoreCase(msgType)){
					sb.append(" and (a.type='TRAVEL' and b.order_userid=?)"); //来自于属于员工的消息列表
					sb.append(" or (a.type = 'SYSTEM' and a.org_id = ?)");
					params.add(user.getId());
					params.add(topOrg.getId());
				}
				
			}else if(user.isEntAdmin()){
				//企业管理员
					sb.append(" and (a.org_id in (").append(orgIdsStr).append("))");
				if(BusinessConstants.MSG_TYPE_ABNORMAL.equalsIgnoreCase(msgType)){
					sb.append(" and a.type in ('OVERSPEED','VEHICLEBACK','OUTBOUND','VIOLATE')");
				}else if(BusinessConstants.MSG_TYPE_SYSTEM.equalsIgnoreCase(msgType)){
//					sb.append(" and type in ('SYSTEM','MAINTAIN')");
					sb.append(" and type in ('SYSTEM')");
				}else if(BusinessConstants.MSG_TYPE_ALL.equalsIgnoreCase(msgType)){
					sb.append(" and type in ('SYSTEM','OVERSPEED','VEHICLEBACK','OUTBOUND','MAINTAIN','VIOLATE') ");
					
				}
			}else{
				//部门管理员
				if(BusinessConstants.MSG_TYPE_ABNORMAL.equalsIgnoreCase(msgType)){
					sb.append(" and a.type in ('OVERSPEED','VEHICLEBACK','OUTBOUND','VIOLATE')");
					sb.append(" and (a.org_id in (").append(orgIdsStr).append("))");
					params.add(user.getOrganizationId());
				}else if(BusinessConstants.MSG_TYPE_SYSTEM.equalsIgnoreCase(msgType)){
					sb.append(" and (a.type='SYSTEM' and a.org_id=?)");
//					sb.append(" or (type='MAINTAIN' and a.org_id in (").append(orgIdsStr).append("))");
					params.add(topOrg.getId());
					
				}else if(BusinessConstants.MSG_TYPE_ALL.equalsIgnoreCase(msgType)){
					sb.append(" and (a.type='TRAVEL' and b.order_userid=?)");
					sb.append(" or (a.type='SYSTEM' and a.org_id=?)");
					sb.append(" or (a.type in('OVERSPEED','VEHICLEBACK','OUTBOUND','MAINTAIN','VIOLATE') and a.org_id in (").append(orgIdsStr).append("))");
					params.add(user.getId());
					params.add(topOrg.getId());
				}else if (BusinessConstants.MSG_TYPE_TRIP.equalsIgnoreCase(msgType)) {
					sb.append(" and a.type='TRAVEL' and b.order_userid=?");
					params.add(user.getId());
				}
			}
			sb.append(" order by id desc");
		}
		Pagination page=new Pagination(sb.toString(), currentPage, pageSize,Message.class,jdbcTemplate, params.toArray());
		
		return page.getResult();
	}
	
	
	@Override
	public PagModel getMessageByUserForApp(User user, String msgType, Integer currentPage, Integer pageSize, String app,List<Long> list, Organization topOrg) {
		LOG.debug("MessageDaoImpl.getMessageByUser["+user+"]");
		List<Object> params = new ArrayList<Object>();
		StringBuilder sb=new StringBuilder();
		sb.append("select a.id,type,car_no as carNo,time,org_id as orgId,location,is_end as isEnd,warning_id as warningId,msg,order_id,title from message a ");
		sb.append("left join busi_order b ");
		sb.append("on a.order_id=b.id ");
		sb.append("where 1=1 ");
	//	sb.append(" and (org_id is null or org_id = ? or org_id in (select id from sys_organization where parent_id = ?)) ");		
	//	params.add(user.getOrganizationId());
	//	params.add(user.getOrganizationId());
		if (msgType != null) {
			StringBuffer orgIdsStr = new StringBuffer();
		    for(Long id : list){
		    	orgIdsStr.append(id+",");
		    }
		    if(orgIdsStr.length()>0){
		    	orgIdsStr.replace(orgIdsStr.length()-1, orgIdsStr.length(), "");
		    }
			if(user.isDriver()){
				if(BusinessConstants.MSG_TYPE_TRIP.equals(msgType)){
					sb.append(" and a.type = 'TASK' ");
					sb.append(" and b.driver_id=?"); //来自于属于员工的消息列表
					params.add(user.getId());
				}else if(BusinessConstants.MSG_TYPE_SYSTEM.equals(msgType)){
					sb.append(" and a.type = 'SYSTEM' ");
					sb.append(" and a.org_id = ?");
					params.add(topOrg.getId());
				}else if(BusinessConstants.MSG_TYPE_ALL.equals(msgType)){
					sb.append(" and (a.type='TASK' and b.driver_id=?)"); //来自于属于司机的消息列表
					sb.append(" or (a.type = 'SYSTEM' and a.org_id =?)");
					params.add(user.getId());
					params.add(topOrg.getId());
				}
			}else if(user.isEndUser()){
				if(BusinessConstants.MSG_TYPE_TRIP.equals(msgType)){
					sb.append(" and a.type = 'TRAVEL'");
					sb.append(" and b.order_userid=?"); //来自于属于员工的消息列表
					params.add(user.getId());
				}else if(BusinessConstants.MSG_TYPE_SYSTEM.equals(msgType)){
					sb.append(" and a.type = 'SYSTEM'");
					sb.append(" and a.org_id = ?");
					params.add(topOrg.getId());
				}else if(BusinessConstants.MSG_TYPE_ALL.equals(msgType)){
					sb.append(" and (a.type='TRAVEL' and b.order_userid=?)"); //来自于属于员工的消息列表
					sb.append(" or (a.type = 'SYSTEM' and a.org_id = ?)");
					params.add(user.getId());
					params.add(topOrg.getId());
				}
				
			}else if(user.isEntAdmin()){
				//企业管理员
					sb.append(" and (a.org_id in (").append(orgIdsStr).append("))");
				if(BusinessConstants.MSG_TYPE_ABNORMAL.equals(msgType)){
					sb.append(" and a.type in ('OVERSPEED','VEHICLEBACK','OUTBOUND','VIOLATE')");
				}else if(BusinessConstants.MSG_TYPE_SYSTEM.equals(msgType)){
					sb.append(" and type in ('SYSTEM','MAINTAIN')");
				}else if(BusinessConstants.MSG_TYPE_ALL.equals(msgType)){
					sb.append(" and type in ('SYSTEM','OVERSPEED','VEHICLEBACK','OUTBOUND','MAINTAIN','VIOLATE') ");
					
				}
			}else{
				//部门管理员
				//CR2215
				if(StringUtils.isNotBlank(app) && Constants.CARDAY_ENDUSER.equals(app)){
					if(BusinessConstants.MSG_TYPE_ABNORMAL.equals(msgType)){
						sb.append(" and a.type in ('THEREISNOPERMISSIONFORTHISUSETYPE')");
						sb.append(" and a.org_id=?");
						params.add(user.getOrganizationId());
					}else if(BusinessConstants.MSG_TYPE_SYSTEM.equals(msgType)){
						sb.append(" and (a.type = 'SYSTEM' and a.org_id =?)");
						params.add(topOrg.getId());
						
//						sb.append(" and (a.type='SYSTEM' and a.org_id=(select CASE WHEN parent_id=0 THEN id ELSE parent_id END entId from sys_organization  where id =?))");
//						sb.append(" or (type='MAINTAIN' and a.org_id=?)");
//						params.add(user.getOrganizationId());
//						params.add(user.getOrganizationId());
					}else if (BusinessConstants.MSG_TYPE_TRIP.equals(msgType)) {
						sb.append(" and a.type='TRAVEL' and b.order_userid=?");
						params.add(user.getId());
					}else if(BusinessConstants.MSG_TYPE_ALL.equals(msgType)){
						sb.append(" and (a.type='TRAVEL' and b.order_userid=?)");
						sb.append(" or (a.type='SYSTEM'  and a.org_id =?)");
						//sb.append(" or (a.type in('OVERSPEED','VEHICLEBACK','OUTBOUND') and a.org_id=?)");
						//sb.append(" or (a.type='MAINTAIN' and a.org_id=?)");
						params.add(user.getId());
						params.add(topOrg.getId());
//						params.add(user.getOrganizationId());
//						params.add(user.getOrganizationId());
					}
				}else{
					if(BusinessConstants.MSG_TYPE_ABNORMAL.equals(msgType)){
						sb.append(" and a.type in ('OVERSPEED','VEHICLEBACK','OUTBOUND','VIOLATE')");
						sb.append(" and (a.org_id in (").append(orgIdsStr).append("))");
//						sb.append(" and a.org_id=?");
//						params.add(user.getOrganizationId());
					}else if(BusinessConstants.MSG_TYPE_SYSTEM.equals(msgType)){
						sb.append(" and (a.type='SYSTEM' and a.org_id =?)");
						sb.append(" or (type='MAINTAIN' and a.org_id in (").append(orgIdsStr).append("))");
						params.add(topOrg.getId());
						
					}else if(BusinessConstants.MSG_TYPE_ALL.equals(msgType)){
//						sb.append(" and (a.type='TRAVEL' and b.order_userid=?)");  //部门管理员使用管理员App不能看到Travel的消息
						sb.append(" and (a.type='SYSTEM' and a.org_id =?)");
						sb.append(" or (a.type in('OVERSPEED','VEHICLEBACK','OUTBOUND','MAINTAIN','VIOLATE') and a.org_id in (").append(orgIdsStr).append("))");
//						sb.append(" or (a.type='MAINTAIN' and a.org_id=?)");
//						params.add(user.getId());
						params.add(topOrg.getId());
//						params.add(user.getOrganizationId());
//						params.add(user.getOrganizationId());
					}
					/*else if (BusinessConstants.MSG_TYPE_TRIP.equals(msgType)) {
						sb.append(" and a.type='TRAVEL' and b.order_userid=?");
						params.add(user.getId());
					}*/
				}
			}
			sb.append(" order by id desc");
		}
		Pagination page=new Pagination(sb.toString(), currentPage, pageSize,Message.class,jdbcTemplate, params.toArray());
		
		return page.getResult();
	}
	

	@Override
	public void setMsgAsRead(Long msgId) {
		LOG.debug("MessageDaoImpl.setMsgAsRead["+msgId+"]");
		String sql = "update message set is_new=0 where id = ?";
        jdbcTemplate.update(sql,msgId);
	}

	@Override
	public Message findById(Long msgId) {
		String sql = "select id,type,car_no as carNo,time,org_id as orgId,location,is_end as isEnd,is_new as isNew,warning_id as warningId,msg,title,order_id from message where id=?";
        List<Message> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Message.class), msgId);
        if(list.size() == 0) {
            return null;
        }
        return list.get(0);
	}
	
	@Override
	public void saveMessages(final List<Message> messages){
		 String sql = "insert into message(type,car_no,time,org_id,location,is_end,is_new,warning_id,msg,order_id,title) values (?,?,?,?,?,?,?,?,?,?,?)";
		 jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter()
		  {
			   public void setValues(PreparedStatement ps,int i)throws SQLException
			   {
				   if (null!=messages.get(i).getType().name()) {
					   ps.setString(1, messages.get(i).getType().name());
				   }else{
					   ps.setNull(1, java.sql.Types.CHAR);
				   }
				   if (null!=messages.get(i).getCarNo()) {
					   ps.setString(2, messages.get(i).getCarNo());
				   }else{
					   ps.setNull(2, java.sql.Types.CHAR);
				   }
				   if(null!=messages.get(i).getTime()){
					   ps.setTimestamp(3, new java.sql.Timestamp(messages.get(i).getTime().getTime()));
	               }else{
	                   ps.setNull(3, java.sql.Types.TIMESTAMP);
	               }
				   if(null!=messages.get(i).getOrgId()){
					   ps.setLong(4, messages.get(i).getOrgId());
	               }else{
	                   ps.setNull(4, java.sql.Types.INTEGER);
	               }
				   if (null!=messages.get(i).getLocation()) {
					   ps.setString(5, messages.get(i).getLocation());
				   }else{
					   ps.setNull(5, java.sql.Types.CHAR);
				   }
				   if(null!=messages.get(i).getIsEnd()){
					   ps.setLong(6, messages.get(i).getIsEnd());
	               }else{
	                   ps.setNull(6, java.sql.Types.INTEGER);
	               }
				   if(null!=messages.get(i).getIsNew()){
					   ps.setLong(7, messages.get(i).getIsNew());
				   }else{
	                   ps.setNull(7, java.sql.Types.INTEGER);
	               }
				   if (null!=messages.get(i).getWarningId()) {
					   ps.setLong(8, messages.get(i).getWarningId());
				   }else{
					   ps.setNull(8, java.sql.Types.INTEGER);
				   }
				   if (null!=messages.get(i).getMsg()) {
					   ps.setString(9, messages.get(i).getMsg());
				   }else{
					   ps.setNull(9, java.sql.Types.CHAR);
				   }
				   if (null!=messages.get(i).getOrderId()) {
					   ps.setLong(10, messages.get(i).getOrderId());
				   }else{
					   ps.setNull(10, java.sql.Types.INTEGER);
				   }
				   if (null!=messages.get(i).getTitle()) {
					   ps.setString(11, messages.get(i).getTitle());
				   }else{
					   ps.setNull(11, java.sql.Types.CHAR);
				   }
			   }
			   public int getBatchSize()
			   {
			       return messages.size();
			   }
		  });
	}

	@Override
	public PagModel findAllSysMessage(String title,Integer currentPage, Integer numPerPage) {
		LOG.debug("Inside MessageDaoImpl.findAllSysMessage");
		List<Object> params = new ArrayList<Object>();
		params.add("SYSTEM");
		String sql="select id,type,time,msg from message where type=?";
		if (StringUtils.isNotBlank(title)) {
			sql+=" and title like "+SqlUtil.processLikeInjectionStatement(title);
		}
		Pagination page=new Pagination(sql, currentPage, numPerPage,Message.class,jdbcTemplate, params.toArray());
		PagModel pageModel = page.getResult();
		return pageModel;
	}

	@Override
	public List<Message> getLatestSystemMessage(Long organizationId) {
		String sql="select * from message where type='SYSTEM' and org_id=? order by id desc limit 3";
	    return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Message>(Message.class), organizationId);
	}
	
	public void updateOutBoundMsgStatus(Long alertId){
		String sql = "update message set is_end=1 where warning_id = ?";
        jdbcTemplate.update(sql,alertId);
	}

	@Override
	public Integer getReadMessageByUserId(Long userId,Long messageId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(*) from read_message where 1=1 ");
		sql.append(" and message_id=? and user_id=?");
		List<Object> params = new ArrayList<Object>();
		params.add(messageId);
		params.add(userId);
		Integer count=jdbcTemplate.queryForObject(sql.toString(), Integer.class, params.toArray());
		return count;
	}

	@Override
	public void saveReadMessageByUserId(Long userId, Long messageId) {
        String sql = "INSERT INTO read_message(message_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sql,messageId,userId);
	}

	@Override
	public Message queryMessageById(Long messageId) {
		String sql="select * from message where id=?";
	   List<Message> list=jdbcTemplate.query(sql, new BeanPropertyRowMapper<Message>(Message.class), messageId);
	   if (!list.isEmpty()) {
		   return list.get(0);
	   }
	   return null;
	}
}
