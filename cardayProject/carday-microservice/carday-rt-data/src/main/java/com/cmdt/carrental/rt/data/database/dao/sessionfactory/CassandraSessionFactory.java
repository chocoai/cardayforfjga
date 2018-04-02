package com.cmdt.carrental.rt.data.database.dao.sessionfactory;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Repository;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.HostDistance;
import com.datastax.driver.core.PoolingOptions;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.policies.DefaultRetryPolicy;

@Repository("cassandraSessionFactory")
public class CassandraSessionFactory {
	private static final Logger LOG = LogManager.getLogger(CassandraSessionFactory.class);
	
	private Cluster cluster;
	
	private Session tspSession;
	
	
//	@Resource(name = "cassandraProperties")
//	@Qualifier("cassandraProperties")
	private static Properties cassandraProp;
	
	
	static{
	    try {
            if(cassandraProp == null){
                Resource resource = new ClassPathResource("/application.properties");
                Properties props = PropertiesLoaderUtils.loadProperties(resource);
                cassandraProp = props;
            }
        } catch (IOException e) {
            LOG.error("Cassandra Connection Exception", e);
        }
	}
	
	
	public CassandraSessionFactory() {
		//connect();
	}
	
	
	/**
	 * Build Cassandra cluster instance for getting session.
	 *  <br><b>Note: Do not call this method when this class is used in web container!! 
	 * It will be called automatically by container.</b>
	 */
	@PostConstruct
	public void connect() {
		cluster = Cluster.builder()
				.addContactPoints(getContactPoints())
				.withPort(Integer.valueOf(cassandraProp.getProperty("cassandra.port")))
				.withCredentials(cassandraProp.getProperty("cassandra.username"), cassandraProp.getProperty("cassandra.passwd"))
				.withPoolingOptions(getPoolingOptions())
				.withRetryPolicy(DefaultRetryPolicy.INSTANCE)
				// Default load balancing policy
				//.withLoadBalancingPolicy(
                //        new TokenAwarePolicy(new DCAwareRoundRobinPolicy()))
				.build();
	}
	
	/**
	 * get Session object for keyspace yudo_tsp.s
	 * @return Session 
	 */
	public Session getSession() {
		if (tspSession == null) {
			if (cluster==null || cluster.isClosed()) {
				connect();
			}
			tspSession = cluster.connect("crt_tsp"); 
		}
		return tspSession;
	}
	
	public Session getSession(String keyspace) {
		if (cluster==null || cluster.isClosed()) {
			connect();
		}
		return cluster.connect(keyspace);
	}
	
	private PoolingOptions getPoolingOptions() {
		int heartbeatInterval = Integer.valueOf(cassandraProp.getProperty("cassandra.heartbeatintervalseconds"));
		int coreLocal = Integer.valueOf(cassandraProp.getProperty("cassandra.connectionsperhost.local.core"));
		int maxLocal = Integer.valueOf(cassandraProp.getProperty("cassandra.connectionsperhost.local.max"));
		
		PoolingOptions poolingOptions = new PoolingOptions();
		poolingOptions
			.setHeartbeatIntervalSeconds(heartbeatInterval) // default 30
			.setConnectionsPerHost(HostDistance.LOCAL, coreLocal, maxLocal);
			//.setConnectionsPerHost(HostDistance.REMOTE, 2,	4);
		return poolingOptions;
	}
	
	private String[] getContactPoints() {
		String strContactPoints = cassandraProp.getProperty("cassandra.contactpoints");
		/*StringTokenizer tz = new StringTokenizer(strContactPoints, ",");
		List<String> lstContactPoints = new ArrayList<String>();
		while(tz.hasMoreTokens()) {
			lstContactPoints.add(tz.nextToken());
		}
		String[] arryContactPoints = lstContactPoints.toArray(new String[0]);*/
		String[] arryContactPoints = strContactPoints.split(",");
		
		return arryContactPoints;
	}
	
	/**
	 * Close all sessions created from the cluster.
	 *  <br><b>Note: Do not call this method when this class is used in web container!! 
	 * It will be called automatically by container.</b>
	 */
	@PreDestroy
	public void close() {
		LOG.info("Disposing cluster " + cluster.getClusterName());
		if (tspSession != null) {
			tspSession.close();
			tspSession = null;
		}

		if (cluster != null) {
			cluster.close();
			cluster = null;
		}
	}
}
