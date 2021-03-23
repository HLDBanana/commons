package com.yss.datamiddle.config;

import com.yss.datamiddle.constants.Constants;
import com.yss.datamiddle.vo.RedisClusterVO;
import com.yss.datamiddle.vo.RedisConfigVO;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPoolConfig;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: datamiddle-redis
 * @description: 集群redis配置
 * @author: li yaofei
 * @create: 2020-10-20 11:13
 */
public class RedisCConfiguration {

	public static List<RedisClusterVO> redisconfigs;
	static{
		if(!CollectionUtils.isEmpty(RedisConfiguration.redisconfigs)){
			redisconfigs = RedisConfiguration.redisconfigs.stream().filter(redisConfig -> Constants.REDIS_DBTYPE_CS.equals(redisConfig.getDbType())).map(redisConfig -> toRedisClusterVO(redisConfig)).collect(Collectors.toList());
		}
	}
	public static List<JedisPoolConfig> jedisPoolConfig() {
		List<JedisPoolConfig> jedisPoolconfigs = new ArrayList<>();
		if(!CollectionUtils.isEmpty(redisconfigs)){
			redisconfigs.forEach(redisConfig -> {
				JedisPoolConfig pool = new JedisPoolConfig();
				if(redisConfig.getMaxIdle() != 0){
					pool.setMaxIdle(redisConfig.getMaxIdle());
				}
				pool.setBlockWhenExhausted(redisConfig.isBlockWhenExhausted());
				if(redisConfig.getNumTestsPerEvictionRun() != 0){
					pool.setNumTestsPerEvictionRun(redisConfig.getNumTestsPerEvictionRun());
				}
				if(redisConfig.getTimeBetweenEvictionRunsMillis() != 0){
					pool.setTimeBetweenEvictionRunsMillis(redisConfig.getTimeBetweenEvictionRunsMillis());
				}
				if(redisConfig.getMinEvictableIdleTimeMillis() != 0){
					pool.setMinEvictableIdleTimeMillis(redisConfig.getMinEvictableIdleTimeMillis());
				}
				if(redisConfig.getSoftMinEvictableIdleTimeMillis() != 0){
					pool.setSoftMinEvictableIdleTimeMillis(redisConfig.getSoftMinEvictableIdleTimeMillis());
				}
				if(redisConfig.getMaxTotal() != 0){
					pool.setMaxTotal(redisConfig.getMaxTotal());
				}
				if(redisConfig.getMaxWaitMillis() != 0){
					pool.setMaxWaitMillis(redisConfig.getMaxWaitMillis());
				}
				jedisPoolconfigs.add(pool);
			});
		}
		return jedisPoolconfigs;
	}

	public static Map<String, JedisPoolConfig> jedisPoolConfigMap() {
		Map<String, JedisPoolConfig> jedisPoolconfigMap = new HashMap<>();
		if(!CollectionUtils.isEmpty(redisconfigs)){
			redisconfigs.forEach(redisConfig -> {
				JedisPoolConfig pool = new JedisPoolConfig();
				if(redisConfig.getMaxIdle() != 0){
					pool.setMaxIdle(redisConfig.getMaxIdle());
				}
				pool.setBlockWhenExhausted(redisConfig.isBlockWhenExhausted());
				if(redisConfig.getNumTestsPerEvictionRun() != 0){
					pool.setNumTestsPerEvictionRun(redisConfig.getNumTestsPerEvictionRun());
				}
				if(redisConfig.getTimeBetweenEvictionRunsMillis() != 0){
					pool.setTimeBetweenEvictionRunsMillis(redisConfig.getTimeBetweenEvictionRunsMillis());
				}
				if(redisConfig.getMinEvictableIdleTimeMillis() != 0){
					pool.setMinEvictableIdleTimeMillis(redisConfig.getMinEvictableIdleTimeMillis());
				}
				if(redisConfig.getSoftMinEvictableIdleTimeMillis() != 0){
					pool.setSoftMinEvictableIdleTimeMillis(redisConfig.getSoftMinEvictableIdleTimeMillis());
				}
				if(redisConfig.getMaxTotal() != 0){
					pool.setMaxTotal(redisConfig.getMaxTotal());
				}
				if(redisConfig.getMaxWaitMillis() != 0){
					pool.setMaxWaitMillis(redisConfig.getMaxWaitMillis());
				}
				jedisPoolconfigMap.put(redisConfig.getService(), pool);
			});
		}
		return jedisPoolconfigMap;
	}

	public static List<RedisClusterConfiguration> getRedisClusterConfiguration() {
		List<RedisClusterConfiguration> redisClusters = new ArrayList<>();
		if(!CollectionUtils.isEmpty(redisconfigs)){
			redisconfigs.forEach(redisCluster ->{
				RedisClusterConfiguration configuration = new RedisClusterConfiguration();
				Set<RedisNode> nodes = new HashSet<>();
				if(!CollectionUtils.isEmpty(redisCluster.getHostPorts())) {
					redisCluster.getHostPorts().forEach((ip, v) -> v.stream().forEach(port -> nodes.add(new RedisNode(ip, port))));
				}
				configuration.setClusterNodes(nodes);
				if(redisCluster.getMaxRedirects() != 0){
					configuration.setMaxRedirects(redisCluster.getMaxRedirects());
				}
				if(!StringUtils.isEmpty(redisCluster.getPassWord())){
					configuration.setPassword(redisCluster.getPassWord());
				}
				redisClusters.add(configuration);
			});
		}
		return redisClusters;
	}

	public static Map<String, RedisClusterConfiguration> getRedisClusterConfigurationMap() {
		Map<String, RedisClusterConfiguration> redisClusterMap = new HashMap<>();
		if(!CollectionUtils.isEmpty(redisconfigs)){
			redisconfigs.forEach(redisCluster ->{
				RedisClusterConfiguration configuration = new RedisClusterConfiguration();
				Set<RedisNode> nodes = new HashSet<>();
				if(!CollectionUtils.isEmpty(redisCluster.getHostPorts())) {
					redisCluster.getHostPorts().forEach((ip, v) -> v.stream().forEach(port -> nodes.add(new RedisNode(ip, port))));
				}
				configuration.setClusterNodes(nodes);
				if(redisCluster.getMaxRedirects() != 0){
					configuration.setMaxRedirects(redisCluster.getMaxRedirects());
				}
				if(!StringUtils.isEmpty(redisCluster.getPassWord())){
					configuration.setPassword(redisCluster.getPassWord());
				}
				redisClusterMap.put(redisCluster.getService(), configuration);
			});
		}
		return redisClusterMap;
	}

	public static List<JedisConnectionFactory> redisConnectionFactory() {
		List<JedisConnectionFactory> jedisCF = new ArrayList<>();
		if(!CollectionUtils.isEmpty(redisconfigs)){
			int size = redisconfigs.size();
			for (int i = 0; i < size; i++) {
				jedisCF.add(new JedisConnectionFactory(getRedisClusterConfiguration().get(i), jedisPoolConfig().get(i)));
			}
		}
		return jedisCF;
	}

	public static Map<String, JedisConnectionFactory> redisConnectionFactoryMap() {
		Map<String, JedisConnectionFactory> jedisCF = new HashMap<>();
		if(!CollectionUtils.isEmpty(redisconfigs)){
			int size = redisconfigs.size();
			for (int i = 0; i < size; i++) {
				JedisConnectionFactory jedisConnectionFactory =
						new JedisConnectionFactory(getRedisClusterConfiguration().get(i), jedisPoolConfig().get(i));
				jedisCF.put(redisconfigs.get(i).getService(), jedisConnectionFactory);
			}
		}
		return jedisCF;
	}

	private static RedisClusterVO toRedisClusterVO(RedisConfigVO redisConfigVO){
		RedisClusterVO vo = new RedisClusterVO();
		vo.setService(redisConfigVO.getService());
		vo.setBlockWhenExhausted(redisConfigVO.isBlockWhenExhausted());
		vo.setHostPorts(redisConfigVO.getHostPorts());
		vo.setMaxIdle(redisConfigVO.getMaxIdle());
		vo.setMaxRedirects(redisConfigVO.getMaxRedirects());
		vo.setMaxTotal(redisConfigVO.getMaxTotal());
		vo.setMaxWaitMillis(redisConfigVO.getMaxWaitMillis());
		vo.setMinEvictableIdleTimeMillis(redisConfigVO.getMinEvictableIdleTimeMillis());
		vo.setNumTestsPerEvictionRun(redisConfigVO.getNumTestsPerEvictionRun());
		vo.setPassWord(redisConfigVO.getPassWord());
		vo.setSoftMinEvictableIdleTimeMillis(redisConfigVO.getSoftMinEvictableIdleTimeMillis());
		vo.setTimeBetweenEvictionRunsMillis(redisConfigVO.getTimeBetweenEvictionRunsMillis());
		vo.setTimeout(redisConfigVO.getTimeout());
		return vo;
	}

}
