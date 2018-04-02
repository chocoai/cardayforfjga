package com.cmdt.carrental.common.cache;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.JedisCluster;

@Service
public class RedisService
{
    private Logger LOG = Logger.getLogger(RedisService.class);
    
    // @Autowired
    // private ShardedJedisPool shardedJedisPool;
    
    @Autowired
    private JedisCluster jedisCluster;
    //
    // private <T> T execute(Function<T, ShardedJedis> fun)
    // {
    // ShardedJedis shardedJedis = null;
    // try
    // {
    // // 从连接池中获取到jedis分片对象
    // shardedJedis = shardedJedisPool.getResource();
    // return fun.callback(shardedJedis);
    // }
    // finally
    // {
    // if (null != shardedJedis)
    // {
    // // 关闭，检测连接是否有效，有效则放回到连接池中，无效则重置状态
    // shardedJedis.close();
    // }
    // // shardedJedisPool.destroy();
    // }
    // }
    
    /**
     * 执行set操作
     * 
     * @param key
     * @param value
     * @return
     */
    public String set(final String key, final String value)
    {
        LOG.info("Redis: start to set object . key : " + key + " value : " + value);
        return jedisCluster.set(key, value);
        // return this.execute(new Function<String, ShardedJedis>()
        // {
        // @Override
        // public String callback(ShardedJedis e)
        // {
        // LOG.info("Redis: start to set object . key : " + key + " value : " + value);
        // return e.set(key, value);
        // }
        // });
    }
    
    /**
     * 执行get操作
     * 
     * @param key
     * @return
     */
    public String get(final String key)
    {
        String value = jedisCluster.get(key);
        LOG.info("Redis: start to get object . value : " + value);
        return value;
        // return this.execute(new Function<String, ShardedJedis>()
        // {
        // @Override
        // public String callback(ShardedJedis e)
        // {
        // String value = e.get(key);
        // LOG.info("Redis: start to get object . value : " + value);
        // return value;
        // }
        // });
    }
    
    /**
     * 执行delete操作
     * 
     * @param key
     * @return
     */
    public Long del(final String key)
    {
        return jedisCluster.del(key);
        // return this.execute(new Function<Long, ShardedJedis>()
        // {
        //
        // @Override
        // public Long callback(ShardedJedis e)
        // {
        // return e.del(key);
        // }
        // });
    }
    
}
