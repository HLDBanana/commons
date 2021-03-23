package com.yss.datamiddle.config;

import lombok.Data;

/**
 * @description: 数据安全密码信息，同配置文件（application-cipher.yml）对应
 * @author: fangzhao
 * @create: 2020/6/18 16:12
 * @update: 2020/6/18 16:12
 */
@Data
public class SecurityCipher {

    private String algorithm;
    private String cipherIns;
    private String secretKey;
    private String vectorKey;

    private String secureRandomSeed;
}
