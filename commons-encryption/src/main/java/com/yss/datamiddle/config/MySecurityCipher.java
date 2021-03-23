package com.yss.datamiddle.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 数据安全密码信息，同配置文件（application-cipher.yml）对应
 * @author: fangzhao
 * @create: 2020/6/18 16:12
 * @update: 2020/6/18 16:12
 */
@Component
@ConfigurationProperties(prefix = "security.cipher")
public class MySecurityCipher {

    private static List<SecurityCipher> list;

    public static Map<String, SecurityCipher> getConnMap() {
        Map<String, SecurityCipher> map = new HashMap<>();
        if (!list.isEmpty()) {
            for (SecurityCipher securityCipher : list) {
                map.put(securityCipher.getAlgorithm(), securityCipher);
            }
        }
        return map;
    }

    public List<SecurityCipher> getList() {
        return list;
    }

    public void setList(List<SecurityCipher> list) {
        MySecurityCipher.list = list;
    }

}
