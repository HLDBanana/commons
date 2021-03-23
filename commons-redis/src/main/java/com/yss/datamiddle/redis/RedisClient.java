package com.yss.datamiddle.redis;

import com.yss.datamiddle.aspect.SpringContext;
import com.yss.datamiddle.config.RedisCConfiguration;
import com.yss.datamiddle.config.RedisSConfiguration;
import com.yss.datamiddle.constants.ServiceEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: datamiddle-redis
 * @description: redis客户端
 * @author: li yaofei
 * @create: 2020-10-20 11:13
 */
@Slf4j
@Component
public class RedisClient implements ApplicationRunner {

    public static RedisTemplate build(ServiceEnum serviceEnum) {
        return (RedisTemplate) SpringContext.getBean("redisTemplate" + serviceEnum.getName());
    }

    public static RedisTemplate buildQuery() {
        return (RedisTemplate) SpringContext.getBean("redisTemplatequery");
    }

    public static RedisTemplate buildModelEngine() {
        return (RedisTemplate) SpringContext.getBean("redisTemplatemodelEngine");
    }

    public static RedisTemplate buildQueryEngine() {
        return (RedisTemplate) SpringContext.getBean("redisTemplatequeryEngine");
    }

    /**
     * 注册bean
     * @param args
     */
    @Override
    public void run(ApplicationArguments args){
        //获取上下文路径
        ApplicationContext webctx = SpringContext.getApplicationContext();

        //jedis factory 加载
        Map<String, JedisConnectionFactory> jedisConnectionFactoryMap = this.getJedisConnectionFactoryMap();
        if (jedisConnectionFactoryMap != null && jedisConnectionFactoryMap.size() > 0) {
            jedisConnectionFactoryMap.forEach((k, v) -> {
                BeanDefinitionBuilder beanDefinitionBuilderTest = BeanDefinitionBuilder.genericBeanDefinition(RedisTemplate.class, () -> {
                    RedisTemplate<Object, Object> template = new RedisTemplate<>();
                    template.setConnectionFactory(v);
                    return template;
                });
                DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory)webctx.getAutowireCapableBeanFactory();
                defaultListableBeanFactory.registerBeanDefinition("redisTemplate"+k, beanDefinitionBuilderTest.getBeanDefinition());
            });
        }
    }

    /**
     * 加载对应配置的连接工厂
     * @return
     */
    private List<JedisConnectionFactory> getJedisConnectionFactoryList(){
        List<JedisConnectionFactory> jedisConnectionFactories = new ArrayList<>();
        List<JedisConnectionFactory> clist = RedisCConfiguration.redisConnectionFactory();
        if(!CollectionUtils.isEmpty(clist)){
            jedisConnectionFactories.addAll(clist);
        }
        List<JedisConnectionFactory> slist = RedisSConfiguration.redisConnectionFactory();
        if(!CollectionUtils.isEmpty(slist)){
            jedisConnectionFactories.addAll(slist);

        }
        if(jedisConnectionFactories == null){
            log.error("Redis Configuration error");
        }
        return jedisConnectionFactories;
    }

    /**
     * 加载对应配置的连接工厂
     * @return
     */
    private Map<String, JedisConnectionFactory> getJedisConnectionFactoryMap(){
        Map<String, JedisConnectionFactory> jedisConnectionFactories = new HashMap<>();
        Map<String, JedisConnectionFactory> clist = RedisCConfiguration.redisConnectionFactoryMap();
        if(!CollectionUtils.isEmpty(clist)){
            jedisConnectionFactories.putAll(clist);
        }
        Map<String, JedisConnectionFactory> slist = RedisSConfiguration.redisConnectionFactoryMap();
        if(!CollectionUtils.isEmpty(slist)){
            jedisConnectionFactories.putAll(slist);

        }
        if(jedisConnectionFactories == null){
            log.error("Redis Configuration error");
        }
        return jedisConnectionFactories;
    }
}
