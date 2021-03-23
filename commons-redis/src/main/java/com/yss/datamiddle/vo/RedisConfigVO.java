package com.yss.datamiddle.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @program: datamiddle-redis
 * @description: redis集群配置bean
 * @author: li yaofei
 * @create: 2020-10-20 13:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedisConfigVO {
    private String host;
    private int port;
    private String service;
    private String dbType;
    private String passWord;
    private int timeout;
    private int maxTotal;
    private int maxIdle;
    private int maxWaitMillis;
    private int dbIndex;
    private int numTestsPerEvictionRun;
    private boolean blockWhenExhausted;
    private int timeBetweenEvictionRunsMillis;
    private int minEvictableIdleTimeMillis;
    private int softMinEvictableIdleTimeMillis;
    private int maxRedirects;
    private Map<String,List<Integer>> hostPorts;
}
