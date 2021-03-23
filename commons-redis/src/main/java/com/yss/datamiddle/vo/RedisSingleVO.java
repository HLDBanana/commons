package com.yss.datamiddle.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: datamiddle-redis
 * @description: redis单点配置类
 * @author: li yaofei
 * @create: 2020-10-20 09:44
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedisSingleVO {
    private String service;
    private String host;
    private int port;
    private String passWord;
    private int timeout;
    private int maxTotal;
    private int maxIdle;
    private int maxWaitMillis;
    private int dbIndex;
}
