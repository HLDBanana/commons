package com.yss.datamiddle.redis;

import com.yss.datamiddle.config.RedisCConfiguration;
import com.yss.datamiddle.config.RedisConfiguration;
import com.yss.datamiddle.constants.Constants;
import com.yss.datamiddle.constants.ServiceEnum;
import com.yss.datamiddle.vo.RedisConfigVO;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPoolConfig;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;

@Component
public class IDGenerator {

    private static final String Prefix = "Test";

    private ThreadLocal<SimpleDateFormat> simpleDateFormatThreadLocal = new ThreadLocal<SimpleDateFormat>(){
        @Override
        public SimpleDateFormat get() {
            return new SimpleDateFormat("yyyyMMdd");
        }
    };

    public String nextID(ServiceEnum serviceEnum){
        String key = Prefix+simpleDateFormatThreadLocal.get().format(new Date());
        Long existedID = this.getRedisTemplate(serviceEnum).opsForValue().get(key);
        if(existedID!=null){
            this.getRedisTemplate(serviceEnum).opsForValue().set(key,existedID+1);
            return key+String.format("%04d",existedID+1);
        }else{
            this.getRedisTemplate(serviceEnum).opsForValue().set(key,1L);
            return key+"0001";
        }
    }

    public String currentID(ServiceEnum serviceEnum){
        String key = Prefix+simpleDateFormatThreadLocal.get().format(new Date());
        return key+String.format("%04d",this.getRedisTemplate(serviceEnum).opsForValue().get(key));
    }

    private RedisTemplate<String,Long> getRedisTemplate(ServiceEnum serviceEnum) {
        RedisTemplate<String, Long> template = new RedisTemplate<String, Long>();
        List<RedisConfigVO> redisConfigVOS = RedisConfiguration.redisconfigs;
        RedisConfigVO redisConfigVO = redisConfigVOS.stream()
                .filter(redisConfigVo -> serviceEnum.getName().equals(redisConfigVo.getService()))
                .findFirst()
                .orElse(null);
        if (Constants.REDIS_DBTYPE_MS.equals(redisConfigVO.getDbType())) {
            JedisPoolConfig poolConfig = new JedisPoolConfig();
            // 单点redis
            poolConfig.setMaxTotal(redisConfigVO.getMaxTotal());
            poolConfig.setMaxIdle(redisConfigVO.getMaxIdle());
            poolConfig.setMaxWaitMillis(redisConfigVO.getMaxWaitMillis());
            RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
            redisConfig.setHostName(redisConfigVO.getHost());
            redisConfig.setPassword(RedisPassword.of(redisConfigVO.getPassWord()));
            redisConfig.setPort(redisConfig.getPort());
            redisConfig.setDatabase(redisConfigVO.getDbIndex());
            JedisClientConfiguration clientConfig = JedisClientConfiguration.builder()
                    .usePooling().poolConfig(poolConfig).and().readTimeout(Duration.ofMillis(redisConfigVO.getTimeout())).build();
            template.setConnectionFactory(new JedisConnectionFactory(redisConfig,clientConfig));
        } else {
            // 集群redis
            JedisPoolConfig poolConfig = RedisCConfiguration.jedisPoolConfigMap().get(serviceEnum.getName());
            JedisClientConfiguration clientConfig = JedisClientConfiguration.builder()
                    .usePooling().poolConfig(poolConfig).and().readTimeout(Duration.ofMillis(redisConfigVO.getTimeout())).build();
            RedisClusterConfiguration redisConfig = RedisCConfiguration.getRedisClusterConfigurationMap().get(serviceEnum.getName());
            template.setConnectionFactory(new JedisConnectionFactory(redisConfig,clientConfig));
        }
        // 哨兵redis
        // RedisSentinelConfiguration redisConfig = new RedisSentinelConfiguration();
        RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        RedisSerializer<Long> longRedisSerializer = new LongRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setValueSerializer(longRedisSerializer);
        template.setHashKeySerializer(stringSerializer);
        template.setHashValueSerializer(longRedisSerializer);
        // 在properties注入之后启用
        template.afterPropertiesSet();
        return template;
    }
}
