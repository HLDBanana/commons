package com.yss.datamiddle.config;

import com.yss.datamiddle.vo.RedisConfigVO;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

/**
 * @program: yss-datamiddle-redis
 * @description: redis配置
 * @author: li yaofei
 * @create: 2020-10-20 17:41
 */
@Configuration
@ConfigurationProperties(prefix = "datamiddleredis")
@PropertySource(value = {"classpath:redis-${spring.profiles.active}.yml"}, factory = YamlPropertyResourceFactory.class)
public class RedisConfiguration {

    public static List<RedisConfigVO> redisconfigs;

    public void setRedisconfigs(List<RedisConfigVO> redisconfigs) {
        RedisConfiguration.redisconfigs = redisconfigs;
    }

    public static List<RedisConfigVO> get() {
        return redisconfigs;
    }

}
