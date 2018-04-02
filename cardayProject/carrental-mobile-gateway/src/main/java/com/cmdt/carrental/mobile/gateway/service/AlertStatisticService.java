package com.cmdt.carrental.mobile.gateway.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carrental.common.bean.AlertType;
import com.cmdt.carrental.common.constants.Constants;
import com.cmdt.carrental.common.dao.UserDao;
import com.cmdt.carrental.common.dao.VehicleAlertJobDao;
import com.cmdt.carrental.common.entity.EventConfig;
import com.cmdt.carrental.common.entity.Message;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.integration.CommunicationService;
import com.cmdt.carrental.common.integration.ServiceAdapter;
import com.cmdt.carrental.common.integration.ShouqiService;
import com.cmdt.carrental.common.integration.model.ActionName;
import com.cmdt.carrental.common.model.AlertStatisticModel;
import com.cmdt.carrental.common.model.CountModel;
import com.cmdt.carrental.common.model.DateCountModel;
import com.cmdt.carrental.common.model.DriverModel;
import com.cmdt.carrental.common.model.MarkerModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.Point;
import com.cmdt.carrental.common.model.StationModel;
import com.cmdt.carrental.common.model.StatisticModel;
import com.cmdt.carrental.common.model.TransGPStoAddress;
import com.cmdt.carrental.common.model.UserModel;
import com.cmdt.carrental.common.model.VehicleAlertModel;
import com.cmdt.carrental.common.model.VehicleModel;
import com.cmdt.carrental.common.service.MessageService;
import com.cmdt.carrental.common.service.OrganizationService;
import com.cmdt.carrental.common.service.VehicleAlertService;
import com.cmdt.carrental.common.util.DateUtils;
import com.cmdt.carrental.common.util.MessageTemplateUtil;
import com.cmdt.carrental.mobile.gateway.model.request.alert.AlertCreateDto;
import com.cmdt.carrental.mobile.gateway.model.request.alert.AlertOrgDailyStatisticDto;
import com.cmdt.carrental.mobile.gateway.model.request.alert.AlertOrgDto;
import com.cmdt.carrental.mobile.gateway.model.request.alert.AlertOrgStaticDto;
import com.cmdt.carrental.mobile.gateway.model.response.alert.AlertDailyStatisticRetDto;
import com.cmdt.carrental.mobile.gateway.model.response.alert.AlertOrgRetDto;
import com.cmdt.carrental.mobile.gateway.model.response.alert.AlertOrgStatisticRetDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

@Service
public class AlertStatisticService {
	
	private static final Logger LOG = LoggerFactory.getLogger(AlertStatisticService.class);
	private static final ObjectMapper MAPPER = new ObjectMapper();
  
	@Autowired
	private VehicleAlertJobDao vehicleAlertJobDao;
	
	@Autowired
    private VehicleAlertService vehicleService;
	
	@Autowired
	private CommunicationService commService;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private ShouqiService shouqiService;

    //	@Autowired
    //	private BaiduApi baiduApi;
	
	@Autowired
	private OrganizationService organizationService;
	
	
	public AlertOrgStatisticRetDto statisticAlertByTypeAndTimeRanger(AlertOrgStaticDto dto){
		
		 //构件树必须查询根节点。
		 List<Organization> orgList = organizationService.findDownOrganizationListByOrgId(dto.getOrgId(), true, dto.getChildDept());
		 
		 if(orgList!=null&&orgList.size()>0){
		 
			 List<AlertStatisticModel> treeList = vehicleAlertJobDao.statisticAlertByTypeAndTimeRanger(orgList,dto.getOrgId(),dto.getSelfDept(),dto.getChildDept(),dto.getType(),dto.getFrom()+" "+DateUtils.FORMAT_TIME_HH_MI_SS_START,dto.getTo()+" "+DateUtils.FORMAT_TIME_HH_MI_SS_END);
			 
			//构建树
			//首先筛选出根节点
			 AlertStatisticModel tree = null;
					for(AlertStatisticModel model : treeList){
							if(model.getId().longValue() == dto.getOrgId().longValue()){
								tree = model;
								tree.setRootNode(true);
								break;
					}
			 }
			 
			buildUsageReportTree(tree,treeList);
			
			List<StatisticModel> data = new ArrayList<>();
			
			for(AlertStatisticModel child : tree.getChildren()){
				data.add(new StatisticModel(child.getId(),child.getName(),child.getValueToal()));
			}
			if(dto.getSelfDept()){
				data.add(new StatisticModel(tree.getId(),tree.getName(),tree.getValueToal()));
			}
					
			return new AlertOrgStatisticRetDto(dto.getType(),dto.getFrom(),dto.getTo(),data);
		 }else{
			return new AlertOrgStatisticRetDto(dto.getType(),dto.getFrom(),dto.getTo(),new ArrayList<StatisticModel>());
		 }
	}
	
	/**
	 * 构建树
	 * @param node
	 * @param alert统计数列
	 */
	private void buildUsageReportTree(AlertStatisticModel rootNode, List<AlertStatisticModel> statList)
    {
		AlertStatisticModel currentNode = null;
        for (AlertStatisticModel model : statList)
        {
            if (model.getParentId().longValue() == rootNode.getId().longValue())
            {
            	currentNode = model;
            	rootNode.getChildren().add(currentNode);
            	buildUsageReportTree(currentNode, statList);
            }
        }
    }
	
	public VehicleAlertModel getAlertById(Long alertId){
		VehicleAlertModel alertModel = vehicleAlertJobDao.getAlertById(alertId);
		
		//if vehicle back alert need do more compose
				if(alertModel.getAlertType().equals(AlertType.VEHICLEBACK.toString())){
						if(alertModel.getBackStationIds()!=null&&alertModel.getBackStationIds().length()>0){
							List<StationModel> stations = vehicleAlertJobDao.findStationByIds(alertModel.getBackStationIds());
							if(stations!=null&&stations.size()>0){
								alertModel.setStations(stations);
							}
						}
				}
				
				if(alertModel.getAlertType().equals(AlertType.OUTBOUND.toString())){

						List<MarkerModel> markers = new ArrayList<>();
						List<MarkerModel> temp = vehicleAlertJobDao.findMarker(alertModel.getVehicleNumber());
						if(temp!=null&&temp.size()>0){
		                   for(MarkerModel marker : temp){
		                	   markers.add(vehicleService.findMarker(marker.getId()));
		                   }
						}
						alertModel.setMarkers(markers);
					
				}
		
		return alertModel;
	}
	
	public AlertDailyStatisticRetDto statisticDailyAlertByTypeAndTimeRanger(AlertOrgDailyStatisticDto dto){
		
		 List<Organization> orgList = organizationService.findDownOrganizationListByOrgId(dto.getOrgId(), dto.getSelfDept(), dto.getChildDept());
		 if(orgList!=null&&orgList.size()>0){
			 List<Long> orgIdList = new ArrayList<>();
			 for(Organization org : orgList){
				orgIdList.add(org.getId());
			 }		
			
			 List<CountModel> result = vehicleAlertJobDao.statisticDailyAlertByTypeAndTimeRanger(orgIdList,dto.getOrgId(), dto.getSelfDept(),dto.getType(),dto.getFrom(),dto.getTo());
		
			 List<DateCountModel> data = new ArrayList<>();
			 for(CountModel model : result){
				 data.add(new DateCountModel(model.getCountDate(), model.getCountValue()==null?0:model.getCountValue()));
			 }
			 return new AlertDailyStatisticRetDto(dto.getType(),dto.getFrom(),dto.getTo(),data);
		 }else{
			 return new AlertDailyStatisticRetDto(dto.getType(),dto.getFrom(),dto.getTo(), new ArrayList<DateCountModel>());
		 }
	}
	
	public AlertOrgRetDto queryAlertByTypeAndTimeRanger(AlertOrgDto dto){
		
		List<Organization> orgList = organizationService.findDownOrganizationListByOrgId(dto.getOrgId(), dto.getSelfDept(), dto.getChildDept());
		if(orgList!=null&&orgList.size()>0){
			PagModel page = vehicleAlertJobDao.queryAlertByOrgTypeAndTimeRange(dto.getType(),dto.getSelfDept(),dto.getOrgId(),dto.getFrom(),dto.getTo(),orgList,dto.getCurrentPage(),dto.getPageSize());
			List<VehicleAlertModel> alertList = page.getResultList();
			
			//if vehicle back alert need do more compose
			if(dto.getType().equals(AlertType.VEHICLEBACK.toString())){
				for(VehicleAlertModel model : alertList){
					if(model.getBackStationIds()!=null&&model.getBackStationIds().length()>0){
						List<StationModel> stations = vehicleAlertJobDao.findStationByIds(model.getBackStationIds());
						if(stations!=null&&stations.size()>0){
							model.setStations(stations);
						}
					}
				}
			}
			
			if(dto.getType().equals(AlertType.OUTBOUND.toString())){
				for(VehicleAlertModel model : alertList){
					List<MarkerModel> markers = new ArrayList<>();
					List<MarkerModel> temp = vehicleAlertJobDao.findMarker(model.getVehicleNumber());
					if(temp!=null&&temp.size()>0){
	                   for(MarkerModel marker : temp){
	                	   markers.add(vehicleService.findMarker(marker.getId()));
	                   }
					}
					model.setMarkers(markers);
				}
				
			}
			
			return new AlertOrgRetDto(dto.getType(),dto.getFrom(),dto.getTo(),dto.getPageSize(),dto.getCurrentPage(),page.getTotalRows(),alertList);
		}else{
			return new AlertOrgRetDto();
		}
	}
	
	public void generateAlertByType(AlertCreateDto alertCreateDto){
		VehicleAlertModel alert = new VehicleAlertModel();
		
		alert.setAlertSpeed(alertCreateDto.getSpeed().toString());
		alert.setAlertTime(new Timestamp(DateUtils.string2Date(alertCreateDto.getTraceTime(), DateUtils.FORMAT_YYYY_MM_DD_HH_MI_SS).getTime()));
		alert.setAlertType(alertCreateDto.getAlertType());
		
		VehicleModel vehicleModel = vehicleAlertJobDao.getVehicleByImei(alertCreateDto.getImei());
		EventConfig config = vehicleAlertJobDao.queryAlarmConfig(vehicleModel.getId(),AlertType.OVERSPEED.toString());
		
		if(null!=vehicleModel&&config!=null&&config.isEnable()){
			alert.setVehicleNumber(vehicleModel.getVehicleNumber());
			alert.setVehicleType(vehicleModel.getVehicleType());
			alert.setCurrentUseOrgId(vehicleModel.getCurrentuseOrgId());
			alert.setRentId(vehicleModel.getRentId());
			alert.setEntId(vehicleModel.getEntId());
			
			//计算超速百分比
			if(StringUtils.isNotEmpty(alert.getAlertSpeed())){
				double os = (double)(alertCreateDto.getSpeed()-vehicleModel.getLatestLimitSpeed())/vehicleModel.getLatestLimitSpeed();
				BigDecimal   b   =   new   BigDecimal(os);
				//CR-1691要求:精确到整数位，小数位四舍五入
				int  percent   =   b.multiply(new BigDecimal(100)).setScale(0,BigDecimal.ROUND_HALF_UP).intValue();
				String overspeedPercent = percent + "%"; 
				alert.setOverspeedPercent(overspeedPercent);
			}
			
			//根据车牌号和行车时间查询当时的驾驶员
			//根据车牌号和行车时间查询当时的驾驶员,如果没有就查询默认司机
			DriverModel driverModel = vehicleAlertJobDao.findDriver(vehicleModel.getVehicleNumber(), DateUtils.string2Date(alertCreateDto.getTraceTime(), DateUtils.FORMAT_YYYY_MM_DD_HH_MI_SS));
			if(null!=driverModel){
				alert.setDriverId(driverModel.getId());
			}else{
				driverModel = vehicleAlertJobDao.findDefaultDriverByVehicleNumber(vehicleModel.getVehicleNumber());
				if(null!=driverModel){
					alert.setDriverId(driverModel.getId());
				}
			}
	
		
		//根据经纬度计算位置
		if(alertCreateDto.getLatitude() != 0 && alertCreateDto.getLatitude() != 0){
			String alertPosition = this.getPosition(alertCreateDto.getLatitude(),alertCreateDto.getLongitude());
			alert.setAlertPosition(alertPosition);
			alert.setAlertCity(this.getCityByPosition(alertCreateDto.getLatitude(),alertCreateDto.getLongitude()));
			alert.setAlertLatitude(alertCreateDto.getLatitude()+"");
			alert.setAlertLongitude(alertCreateDto.getLongitude()+"");
			}
			
			
			vehicleAlertJobDao.create(alert);
			
			List<VehicleAlertModel> savedResult = new ArrayList<>();
			savedResult.add(alert);
			saveAlertToMessage(savedResult,false);
			//sendAlertPush(savedResult,false);
		}else{
			LOG.error("Cont not find vehicle by imei : "+alertCreateDto.getImei());
		}
	}
	
	
   private void sendAlertPush(List<VehicleAlertModel> alertData,boolean isRelease){
		
		for(VehicleAlertModel alert : alertData){
			
			if(alert.getCurrentUseOrgId()!=null){
				
				List<Organization> orgList = organizationService.findUpOrganizationListByOrgId(alert.getCurrentUseOrgId());
				 List<Long> orgIdList = new ArrayList<>();
				 for(Organization org : orgList){
					orgIdList.add(org.getId());
				 }	
				
				
				List<UserModel> admins = userDao.listByOrgId(orgIdList);
				
				if(admins!=null&&admins.size()>0){
					
					List<String> mobiles = new ArrayList<>();
					for(UserModel admin : admins){
						mobiles.add(admin.getPhone());
					}
					
					if(mobiles.size()>0){
					  if(isRelease){
						  
						String res = commService.sendPush(mobiles,Constants.ALERT_NOTIFICATION_TITLES.get(alert.getAlertType()),MessageTemplateUtil.composeReleaseOutBoundAlertMsg(alert),Constants.CARDAY_ADMIN); 
						LOG.info("Mobile gateway Send Realse MSG Result:"+res);
					  }else{
					    String res = commService.sendPush(mobiles,Constants.ALERT_NOTIFICATION_TITLES.get(alert.getAlertType()),MessageTemplateUtil.composeMessageForAlert(alert),Constants.CARDAY_ADMIN);  
					    LOG.info("Mobile gateway Send Result:"+res);
					  }
					}
				}
			}
			
		}
	}
   
	
	private void saveAlertToMessage(List<VehicleAlertModel> alertData,boolean isRealse){
		List<Message> messages = new ArrayList<>();
	    for(VehicleAlertModel alert : alertData){
	    	Message msg = new Message();
	    	msg.setCarNo(alert.getVehicleNumber());
	    	if(alert.getAlertType().equals(AlertType.OUTBOUND.toString())){
	    	  if(isRealse)
	    		  msg.setIsEnd(1);
	    	  else
	    		  msg.setIsEnd(0);
	    	}
	    	msg.setIsNew(1);
	    	msg.setLocation(alert.getAlertPosition());
	    	msg.setOrgId(alert.getCurrentUseOrgId());
	    	msg.setTime(new java.sql.Date(alert.getAlertTime().getTime()));
	    	msg.setType(alert.getAlertType().equals(AlertType.OUTBOUND.toString())?Message.MessageType.OUTBOUND:(alert.getAlertType().equals(AlertType.OVERSPEED.toString())?Message.MessageType.OVERSPEED:Message.MessageType.VEHICLEBACK));
	    	msg.setWarningId(alert.getId());
	    	if(isRealse){
	    		 msg.setMsg(MessageTemplateUtil.composeReleaseOutBoundAlertMsg(alert));
	    	}else{
	    		 msg.setMsg(MessageTemplateUtil.composeMessageForAlert(alert));
	    	}
	    	messages.add(msg);
	    }
	    messageService.saveMessages(messages);
	}
	
	
	public String getPosition(double latitude, double longitude) {
		TransGPStoAddress rResponse = shouqiService.getAddressByPoint(transGpsPoint(longitude,latitude));
        return rResponse.getResult();
	}
	
	public String getCityByPosition(double latitude, double longitude) {
		TransGPStoAddress rResponse = shouqiService.getCityByPoint(transGpsPoint(longitude,latitude));
        return rResponse.getResult();
	}
	
	
	private Point transGpsPoint(double lon, double lat){
		List<Point> list = new ArrayList<Point>();
		list.add(new Point(lon,lat));
		try{
		Map<String, Object> result = new ServiceAdapter(shouqiService).doService(ActionName.TRANSGPSTOBAIDU, new Object[]{list});
		if (result.get("data") != null) {
    		JsonNode jsonNode = MAPPER.readTree(result.get("data").toString());
    		if (jsonNode.get("status").asInt() == 0) {
    			ArrayNode rows = (ArrayNode) jsonNode.get("result");
				if (rows != null && rows.size() > 0) {
					JsonNode resultNode = rows.get(rows.size()-1);
					return new Point(resultNode.get("lon").asDouble(),resultNode.get("lat").asDouble());
				}
    		}
		}
		}catch(Exception e){
			LOG.info("Trans GPS to baidu point error:"+e);
			return null;
		}
		return null;
	}
	
	
}
