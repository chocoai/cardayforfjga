package com.cmdt.carrental.rt.data.database.dao;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.cmdt.carrental.rt.data.database.dao.sessionfactory.CassandraSessionFactory;
import com.cmdt.carrental.rt.data.database.po.ObdHisotryRecordBasic;

/**
 * @author KevinPei
 */
@Repository
public class ObdHistoryRecordDao {
	
	private static Logger LOG = Logger.getLogger(RealtimeDataProcessDao.class);
	
	@Resource(name="cassandraSessionFactory")
    private CassandraSessionFactory sessionFactory;
	
	
	public void save(ObdHisotryRecordBasic record){
		LOG.debug("ObdHistoryRecordDao.save");
	}
	
	public void update(ObdHisotryRecordBasic record){
		LOG.debug("ObdHistoryRecordDao.update");
	}
	
	public List<ObdHisotryRecordBasic> queryAll(){
		LOG.debug("ObdHistoryRecordDao.queryAll");
		return null;
	}
	
	public void delete(ObdHisotryRecordBasic record){
		LOG.debug("ObdHistoryRecordDao.delete");
	}
	

}
