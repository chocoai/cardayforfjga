package com.cmdt.carrental.platform.service.api.portal;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmdt.carrental.common.entity.RuleAddress;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.RuleData;
import com.cmdt.carrental.common.model.RuleEditData;
import com.cmdt.carrental.common.model.RuleGetOffData;
import com.cmdt.carrental.common.model.RuleGetOnData;
import com.cmdt.carrental.common.model.RuleTimeData;
import com.cmdt.carrental.common.model.RuleTimeDateItemData;
import com.cmdt.carrental.common.model.RuleTimeHolidayItemData;
import com.cmdt.carrental.common.model.RuleTimeWeeklyItemData;
import com.cmdt.carrental.common.model.VehicleRuleDisplayModel;
import com.cmdt.carrental.common.model.VehicleRuleGetOnAndOffDisplayModel;
import com.cmdt.carrental.common.service.OrganizationService;
import com.cmdt.carrental.common.service.RuleService;
import com.cmdt.carrental.common.service.UserService;
import com.cmdt.carrental.platform.service.api.BaseApi;
import com.cmdt.carrental.platform.service.biz.service.PlatformRuleService;
import com.cmdt.carrental.platform.service.common.Constants;
import com.cmdt.carrental.platform.service.exception.ServerException;
import com.cmdt.carrental.platform.service.model.request.rule.RuleAddressDto;
import com.cmdt.carrental.platform.service.model.request.rule.RuleBdDto;
import com.cmdt.carrental.platform.service.model.request.rule.RuleDateTimeDto;
import com.cmdt.carrental.platform.service.model.request.rule.RuleDeleteDto;
import com.cmdt.carrental.platform.service.model.request.rule.RuleDto;
import com.cmdt.carrental.platform.service.model.request.rule.RuleHolidayDto;
import com.cmdt.carrental.platform.service.model.request.rule.RulePageDto;
import com.cmdt.carrental.platform.service.model.request.rule.RuleWeeklyDto;

@Produces(MediaType.APPLICATION_JSON)
public class RuleApi extends BaseApi {
    private static final Logger LOG = LoggerFactory.getLogger(RuleApi.class);
    
    @Autowired
    RuleService ruleService;
    
    @Autowired
    PlatformRuleService platformRuleService;

    /**
     * 根据位置名查询用车位置
     *
     * @return
     */
    @POST
    @Path("/findLocationByLocationName")
    public PagModel list(RulePageDto rulePageDto) {
        try {
            return platformRuleService.findLocationByLocationName(getUserById(rulePageDto.getUserId()), rulePageDto);
        } catch (Exception e) {
            LOG.error("RuleApi list by locationName error, cause by: ", e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
        }
    }

    /**
     * 新增用车位置
     *
     * @return
     */
	  @POST
	  @Path("/create")
	  public String addCarLocation(RuleAddressDto ruleDto) {
	      try {
	          return platformRuleService.createLocation(getUserById(ruleDto.getUserId()),ruleDto);
	      } catch (Exception e) {
	          LOG.error("RuleApi create error, cause by: ", e);
	          throw new ServerException(Constants.API_MESSAGE_FAILURE);
	      }
	  }

    /**
     * 修改用车位置信息
     *
     * @return
     */
    @POST
    @Path("/update")
    public String updateCarLocation(RuleAddressDto ruleAddressDto) {
        try {
            return platformRuleService.updateLocation(getUserById(ruleAddressDto.getUserId()),ruleAddressDto);
        } catch (Exception e) {
            LOG.error("RuleApi updateCarLocation error, cause by: ", e);
            throw new ServerException(Constants.API_MESSAGE_FAILURE);
        }
    }

    /**
     * 删除用车位置
     *
     * @param id
     * @return
     */

    @POST
    @Path("/delete")
    public String delete(@Valid @NotNull RuleDeleteDto ruleDeleteDto) {
    	try {
    		platformRuleService.deleteLocation(ruleDeleteDto);
    		return Constants.API_MESSAGE_SUCCESS;
    	} catch (Exception e) {
            LOG.error("RuleApi delete error, cause by: ", e);
            throw new ServerException(Constants.API_MESSAGE_FAILURE);
        }
    }


    /**
	 * 规则列表查询
	 * @return
	 */
    @POST
    @Path("/ruleList")
    public List<VehicleRuleDisplayModel> ruleList(RuleDto ruleDto) {
        try {
        	return platformRuleService.getRuleListByOrgId(getUserById(ruleDto.getUserId()));
		} catch (Exception e) {
			LOG.error("RuleApi ruleList error, cause by: ", e);
            throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
    }

    /**
	 * 规则移除
	 */
    @POST
    @Path("/removeRule")
    public String removeRule(RuleDto ruleDto) {
    	try {
    		platformRuleService.removeRule(getUserById(ruleDto.getUserId()), ruleDto);
    		return Constants.API_MESSAGE_SUCCESS;
		} catch (Exception e) {
			LOG.error("RuleApi removeRule error, cause by: ", e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
    }

    /**
	 * 规则新增
	 * @return
	 */
    @POST
    @Path("/addRule")
    public String addRule(RuleDto ruleDto) {
        try {
            platformRuleService.addRule(getUserById(ruleDto.getUserId()), ruleDto);
            return Constants.API_MESSAGE_SUCCESS;
        } catch (Exception e) {
            LOG.error("RuleApi addRule error, cause by: ", e);
            throw new ServerException(Constants.API_MESSAGE_FAILURE);
        }
    }
    
    /**
	 * 根据id查询rule
	 * @return
	 */
    @POST
    @Path("/findRuleById")
    public RuleEditData findRuleById(RuleDto ruleDto) {
        try {
        	return platformRuleService.findRuleById(getUserById(ruleDto.getUserId()), ruleDto);
        } catch (Exception e) {
            LOG.error("RuleApi getRule error, cause by: ", e);
            throw new ServerException(Constants.API_MESSAGE_FAILURE);
        }
    }
    
    /**
	 * 规则更新
	 * @return
	 */
    @POST
    @Path("/updateRule")
    public String updateRule(RuleDto ruleDto) {
    	try {
    		platformRuleService.updateRule(getUserById(ruleDto.getUserId()), ruleDto);
    		return Constants.API_MESSAGE_SUCCESS; 
		} catch (Exception e) {
			LOG.info("RuleApi updateRule no authorization");
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
    }
     
    
	/**
	 * 上车位置查询
	 * @return
	 */
    @POST
    @Path("/getOnAddressListForAdd")
    public List<VehicleRuleGetOnAndOffDisplayModel> getOnAddressListForAdd(RuleDto ruleDto) {
        try {
        	return platformRuleService.getOnAddressListForAdd(getUserById(ruleDto.getUserId()));
		} catch (Exception e) {
			LOG.info("RuleApi getOnAddressListForAdd no authorization");
            throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
    }
    
    /**
	 * 下车位置查询
	 * @return
	 */
    @POST
    @Path("/getOffAddressListForAdd")
    public List<VehicleRuleGetOnAndOffDisplayModel> getOffAddressListForAdd(RuleDto ruleDto) {
    	try {
        	return platformRuleService.getOffAddressListForAdd(getUserById(ruleDto.getUserId()));
		} catch (Exception e) {
			LOG.info("RuleApi getOffAddressListForAdd no authorization");
            throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
        
    }
    
    
    /**
	 * 上车位置查询(merged,显示所有位置,已选中的上车位置已标注)
	 * @param loginUser
	 * @param json
	 * @return
	 */
    @POST
    @Path("/getOnAddressListForEdit")
    public List<VehicleRuleGetOnAndOffDisplayModel> getOnAddressListForEdit(RuleDto ruleDto) {
        try {
        	return platformRuleService.getOnAddressListForEdit(getUserById(ruleDto.getUserId()), ruleDto);
		} catch (Exception e) {
			LOG.info("RuleApi getOnAddressListForEdit no authorization");
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
    }
    
    /**
	 * 下车位置查询(merged,显示所有位置,已选中的下车位置已标注)
	 * @param loginUser
	 * @param json
	 * @return
	 */
    @POST
    @Path("/getOffAddressListForEdit")
    public List<VehicleRuleGetOnAndOffDisplayModel> getOffAddressListForEdit(RuleDto ruleDto) {
    	try {
        	return platformRuleService.getOffAddressListForEdit(getUserById(ruleDto.getUserId()), ruleDto);
		} catch (Exception e) {
			LOG.info("RuleApi getOffAddressListForEdit no authorization");
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
    }
    
    /**
	 * 规则列表查询(用户已绑定规则)
	 * @param loginUser
	 * @param json
	 * @return
	 */
    @POST
    @Path("/ruleBindingList")
    public List<VehicleRuleDisplayModel> ruleBindingList(RuleDto ruleDto) {
        try {
        	return platformRuleService.ruleBindingList(getUserById(ruleDto.getUserId()));
		} catch (Exception e) {
			LOG.info("Enter RuleApi ruleBindingList", e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
    }
    
    /**
	 * 规则列表查询(用户未绑定规则)
	 * @param loginUser
	 * @param json
	 * @return
	 */
    @POST
    @Path("/ruleNotBindingList")
    public List<VehicleRuleDisplayModel> ruleNotBindingList(RuleDto ruleDto) {
        try {
        	return platformRuleService.ruleNotBindingList(getUserById(ruleDto.getUserId()), ruleDto);
		} catch (Exception e) {
			LOG.info("Enter RuleApi ruleNotBindingList", e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
    }
    
    /**
	 * 用户解绑规则
	 * @param loginUser
	 * @param json
	 * @return
	 */
    @POST
    @Path("/removeBindingRule")
    public String removeBindingRule(RuleDto ruleDto) {
    	try {
        	platformRuleService.removeBindingRule(getUserById(ruleDto.getUserId()), ruleDto);
        	return Constants.API_MESSAGE_SUCCESS;
		} catch (Exception e) {
			LOG.info("Enter RuleApi removeBindingRule", e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
    }
    
    /**
	 * 用户绑定规则
	 * @param loginUser
	 * @param json
	 * @return
	 */
    @POST
    @Path("/userBindingRule")
    public String userBindingRule(RuleBdDto ruleBdDto) {
    	try {
        	platformRuleService.userBindingRule(getUserById(ruleBdDto.getUserId()), ruleBdDto);
        	return Constants.API_MESSAGE_SUCCESS;
		} catch (Exception e) {
			LOG.info("Enter RuleApi userBindingRule", e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
    }
    
    /**
	 * balance check
	 * @param loginUser
	 * @param json
	 * @return
	 */
    @POST
    @Path("/balanceCheck")
    public String balanceCheck(RuleDto ruleDto) {
    	try {
        	return platformRuleService.balanceCheck(getUserById(ruleDto.getUserId()));
		} catch (Exception e) {
			LOG.info("Enter RuleApi userBindingRule", e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
    }

    /**
     * OR mapping from RUleDto to RuleData
     *
     * @param ruleDto
     * @return
     */

    private RuleData convertRuleDtoToRuleData(RuleDto ruleDto) {
        LOG.info("Enter RuleApi convertRuleDtoToRuleData");
        RuleData ruleData = new RuleData();
        ruleData.setRuleId(ruleDto.getId());
        ruleData.setRuleName(ruleDto.getName());
        ruleData.setRuleType(ruleDto.getType());
        ruleData.setUseLimit(ruleDto.getUseLimit());
        ruleData.setVehicleType(ruleDto.getVehicleType());
        RuleGetOffData offData = new RuleGetOffData();

        offData.setGetOffType(ruleDto.getGetOffType());
        if (ruleDto.getGetOffData() != null) {
            List<String> offDataList = new ArrayList<>();
            for (String str : ruleDto.getGetOffData()) {
                offDataList.add(str);
            }
            offData.setGetOffdata(offDataList);
        } else {
            offData.setGetOffdata(null);
        }

        ruleData.setGetOffList(offData);

        RuleGetOnData onData = new RuleGetOnData();

        onData.setGetOnType(ruleDto.getGetOnType());
        if (ruleDto.getGetOnData() != null) {
            List<String> onDataList = new ArrayList<>();
            for (String str : ruleDto.getGetOffData()) {
                onDataList.add(str);
            }
            onData.setGetOndata(onDataList);
        } else {
            onData.setGetOndata(null);
        }
        ruleData.setGetOnList(onData);

        RuleTimeData ruleTimeData = new RuleTimeData();
        ruleTimeData.setTimeRangeType(ruleDto.getTimeRangeType());
        ruleTimeData.setDateData(convertRuleTimeDateItemData(ruleDto.getDateTimeDto()));
        ruleTimeData.setHolidayData(convertRuleTimeHolidayItemData(ruleDto.getHolidayDto()));
        ruleTimeData.setWeeklyData(convertRuleTimeWeeklyItemData(ruleDto.getWeeklyDto()));

        ruleData.setTimeList(ruleTimeData);
        return ruleData;

    }

    /**
     * OR mapping from RuleWeeklyDto to RUleTimeWeeklyItemData
     *
     * @param weeklyDto
     * @return
     */

    private List<RuleTimeWeeklyItemData> convertRuleTimeWeeklyItemData(List<RuleWeeklyDto> weeklyDto) {
        LOG.info("Enter RuleApi convertRuleTimeWeeklyItemData");
        List<RuleTimeWeeklyItemData> list = null;
        if (weeklyDto != null) {
            list = new ArrayList<>();
            for (RuleWeeklyDto dto : weeklyDto) {
                RuleTimeWeeklyItemData data = new RuleTimeWeeklyItemData();
                data.setEndTime(dto.getEndTime());
                data.setStartTime(dto.getStartTime());
                data.setWeeklyType(dto.getWeeklyType());
                list.add(data);
            }
        }
        return list;
    }

    /**
     * OR mapping from RuleHolidayDto to RUleTimeHolidayItemData
     *
     * @param holidayDto
     * @return
     */

    private List<RuleTimeHolidayItemData> convertRuleTimeHolidayItemData(List<RuleHolidayDto> holidayDto) {
        LOG.info("Enter RuleApi convertRuleTimeHolidayItemData");
        List<RuleTimeHolidayItemData> list = null;
        if (holidayDto != null) {
            list = new ArrayList<>();
            for (RuleHolidayDto dto : holidayDto) {
                RuleTimeHolidayItemData data = new RuleTimeHolidayItemData();
                data.setEndTime(dto.getEndTime());
                data.setHolidayType(dto.getHolidayType());
                data.setStartTime(dto.getStartTime());
                list.add(data);
            }
        }
        return list;
    }

    /**
     * OR mapping from RUleDataTimeDto to RUleTimeDateTimeData
     *
     * @param timeDto
     * @return
     */

    private List<RuleTimeDateItemData> convertRuleTimeDateItemData(List<RuleDateTimeDto> timeDto) {
        LOG.info("Enter RuleApi convertRuleTimeDateItemData");
        List<RuleTimeDateItemData> list = null;
        if (timeDto != null) {
            list = new ArrayList<>();
            for (RuleDateTimeDto dto : timeDto) {
                RuleTimeDateItemData data = new RuleTimeDateItemData();
                data.setStartDay(dto.getStartDay());
                data.setEndDay(dto.getEndDay());
                data.setStartTime(dto.getStartTime());
                data.setEndTime(dto.getEndTime());
                list.add(data);
            }
        }
        return list;
    }

    /**
     * OR mapping from RuleAddressDto to RuleAddress
     *
     * @param ruleAddress
     * @param organizationId
     * @param ruleAddressDto
     * @return
     */

    private RuleAddress convertRuleAddressDtoToRuleAddress(RuleAddress ruleAddress, Long organizationId, RuleAddressDto ruleAddressDto) {
        LOG.info("Enter RuleApi RuleDtoToRuleAddress");
        ruleAddress.setLocationName(ruleAddressDto.getLocationName());
        ruleAddress.setCity(ruleAddressDto.getCity());
        ruleAddress.setPosition(ruleAddressDto.getPosition());
        ruleAddress.setLongitude(ruleAddressDto.getLongitude());
        ruleAddress.setLatitude(ruleAddressDto.getLatitude());
        ruleAddress.setRadius(ruleAddressDto.getRadius());
        ruleAddress.setOrganizationId(organizationId);

        return ruleAddress;
    }


}
