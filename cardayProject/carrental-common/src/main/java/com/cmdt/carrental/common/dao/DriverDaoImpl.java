package com.cmdt.carrental.common.dao;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cmdt.carrental.common.entity.Driver;
import com.cmdt.carrental.common.model.AllocateDepModel;

@Repository
public class DriverDaoImpl implements DriverDao
{
    private static final Logger LOG = LoggerFactory.getLogger(DriverDaoImpl.class);
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Override
    public void updateDriver(Driver driver)
    {
        
        try
        {
            
            String sql =
                "update sys_driver set sex = ?, birthday = ?, license_type = ? ,license_number = ? ,license_begintime= ? , license_expiretime = ? , "
                    + "license_attach = ?,dep_id = ?, station_id = ?, vid= ?, rent_num = ? ,credit_rating = ? where id = ? ";
            
            jdbcTemplate.update(sql,
                driver.getSex(),
                new java.sql.Timestamp(driver.getBirthday().getTime()),
                driver.getLicenseType(),
                driver.getLicenseNumber(),
                new java.sql.Timestamp(driver.getLicenseBegintime().getTime()),
                new java.sql.Timestamp(driver.getLicenseExpiretime().getTime()),
                driver.getLicenseAttach(),
                driver.getDepId(),
                driver.getStationId(),
                driver.getVid(),
                driver.getRentNum(),
                driver.getCreditRating(),
                driver.getId());
            
        }
        catch (Exception e)
        {
            LOG.error("there are some wrongs in updating sys_driver info! " + e.getMessage());
        }
        
    }
    
    @Override
    public void updateDriverCredit(Driver driver)
    {
        try
        {
            
            String sql = "update sys_driver set  rent_num = ? ,credit_rating = ? where id = ? ";
            
            jdbcTemplate.update(sql, driver.getRentNum(), driver.getCreditRating(), driver.getId());
            
        }
        catch (Exception e)
        {
            LOG.error("there are some wrongs in updating sys_driver for credit_rating info! " + e.getMessage());
        }
        
    }
    
    /**
     * 通过四级ID来找到相应的司机的信用等级
     * 
     * @param id
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public Driver queryDriverCreditRatingById(Long id)
    {
        
        try
        {
            String sql =
                "select t.rent_num as rentNum, t.credit_rating as creditRating from sys_driver t where t.id = ? ";
            List<Driver> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Driver.class), id);
            if (null != list && !list.isEmpty())
            {
                return list.get(0);
            }
            return null;
        }
        catch (Exception e)
        {
            LOG.error("there are some wrongs in updating sys_driver for credit_rating info! " + e.getMessage());
            return null;
        }
    }
    
    
    @Override
    public void updateDepToDriver(AllocateDepModel model) {
        try {
        	StringBuffer sql = new StringBuffer();
        	List<Object> params=new ArrayList<Object>();
        	StringBuffer preparams = new StringBuffer();
        	
        	sql.append("update sys_driver");
            
        	if(null != model.getAllocateDepId()){
        		sql.append(" set dep_id = ?");
           		params.add(model.getAllocateDepId());
           	}
            
            for(int i=0,num=model.getIdArray().length;i<num;i++){
            	if(i==num-1){
            		preparams.append("?");
            	}else{
            		preparams.append("?,");
            	}
            	params.add(model.getIdArray()[i]);
            }
            sql.append(" where id in (").append(preparams).append(")");
            jdbcTemplate.update(sql.toString(), params.toArray());
        }
        catch (Exception e) {
            LOG.error("there are some wrongs in updating sys_driver for update depId! " + e.getMessage());
        }
    }

	@Override
	public void updateDriver(Long driverId, Integer drvStatus, Integer tripQuantity, Long tripMileage) {
		String sql="update sys_driver set drv_status=?, trip_quantity=?, trip_mileage=? where id=?";
		jdbcTemplate.update(sql,drvStatus,tripQuantity,tripMileage,driverId);
		
	}

	@Override
	public Driver findById(Long driverId) {
		String sql="select * from sys_driver where id=?";
		List<Driver> list=jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Driver.class), driverId);
		return list.isEmpty() ? null : list.get(0);
	}
	
    
}
