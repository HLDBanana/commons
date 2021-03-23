package com.yss.datamiddle.config;

import com.yss.datamiddle.constants.Constants;
import com.yss.datamiddle.vo.RedisConfigVO;
import com.yss.datamiddle.vo.RedisSingleVO;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: datamiddle-redis
 * @description: 单节点redis配置
 * @author: li yaofei
 * @create: 2020-10-20 11:13
 */
public class RedisSConfiguration {
	public static List<RedisSingleVO> redisconfigs;
	static{
		if(!CollectionUtils.isEmpty(RedisConfiguration.redisconfigs)){
			redisconfigs = RedisConfiguration.redisconfigs.stream().filter(redisConfig -> Constants.REDIS_DBTYPE_MS.equals(redisConfig.getDbType())).map(redisConfig -> toRedisSingleVO(redisConfig)).collect(Collectors.toList());
		}
	}
	/**
	 * 生成配置工厂集合
	 * @return
	 */
	public static List<JedisConnectionFactory> redisConnectionFactory(){
		List<JedisConnectionFactory> conFac = new ArrayList<>();
		if(!CollectionUtils.isEmpty(redisconfigs)){
			redisconfigs.forEach(redisConfig -> {
				JedisPoolConfig pool = new JedisPoolConfig();
				if(redisConfig.getMaxTotal() != 0){
					pool.setMaxTotal(redisConfig.getMaxTotal());
				}
				if(redisConfig.getMaxIdle() != 0){
					pool.setMinIdle(redisConfig.getMaxIdle());
				}
				if(redisConfig.getMaxWaitMillis() != 0){
					pool.setMaxWaitMillis(redisConfig.getMaxWaitMillis());
				}
				pool.setTestOnBorrow(true);
				pool.setTestOnReturn(true);
				JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(pool);
				jedisConnectionFactory.setHostName(redisConfig.getHost());
				if(!redisConfig.getPassWord().isEmpty()){
					jedisConnectionFactory.setPassword(redisConfig.getPassWord());
				}
				jedisConnectionFactory.setPort(redisConfig.getPort());
				jedisConnectionFactory.setDatabase(redisConfig.getDbIndex());
				conFac.add(jedisConnectionFactory);
			});
		}
		return conFac;
	}

	/**
	 * 生成配置工厂集合
	 * @return
	 */
	public static Map<String, JedisConnectionFactory> redisConnectionFactoryMap(){
		Map<String, JedisConnectionFactory> conFac = new HashMap<>();
		if(!CollectionUtils.isEmpty(redisconfigs)){
			redisconfigs.forEach(redisConfig -> {
				JedisPoolConfig pool = new JedisPoolConfig();
				if(redisConfig.getMaxTotal() != 0){
					pool.setMaxTotal(redisConfig.getMaxTotal());
				}
				if(redisConfig.getMaxIdle() != 0){
					pool.setMinIdle(redisConfig.getMaxIdle());
				}
				if(redisConfig.getMaxWaitMillis() != 0){
					pool.setMaxWaitMillis(redisConfig.getMaxWaitMillis());
				}
				pool.setTestOnBorrow(true);
				pool.setTestOnReturn(true);
				JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(pool);
				jedisConnectionFactory.setHostName(redisConfig.getHost());
				if(!redisConfig.getPassWord().isEmpty()){
					jedisConnectionFactory.setPassword(redisConfig.getPassWord());
				}
				jedisConnectionFactory.setPort(redisConfig.getPort());
				jedisConnectionFactory.setDatabase(redisConfig.getDbIndex());
				conFac.put(redisConfig.getService(), jedisConnectionFactory);
			});
		}
		return conFac;
	}

	private static RedisSingleVO toRedisSingleVO(RedisConfigVO redisConfigVO){
		RedisSingleVO vo = new RedisSingleVO();
		vo.setService(redisConfigVO.getService());
		vo.setHost(redisConfigVO.getHost());
		vo.setPort(redisConfigVO.getPort());
		vo.setMaxIdle(redisConfigVO.getMaxIdle());
		vo.setMaxTotal(redisConfigVO.getMaxTotal());
		vo.setMaxWaitMillis(redisConfigVO.getMaxWaitMillis());
		vo.setPassWord(redisConfigVO.getPassWord());
		vo.setTimeout(redisConfigVO.getTimeout());
		vo.setDbIndex(redisConfigVO.getDbIndex());
		return vo;
	}

}
