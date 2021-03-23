package com.yss.datamiddle.po;

import lombok.Data;

/**
 * @description: 密码实体类
 * @author: fangzhao
 * @create: 2020/7/17 16:58
 * @update: 2020/7/17 16:58
 */
@Data
public class CipherEntity {

    /**
     * 加密解密内容
     */
    private String content;
    private String algorithm = "AES";

    /**
     * 算法/模式/补码方式
     */
    private String cipherIns = "AES/CBC/PKCS5Padding";
    /**
     * AES 时必须是 16 个字节，DES 时必须是 8 字节
     */
    private String secretKey = "2B4BAF956F5E7F9DDEBB652981720217";
    private String vectorKey = "2B4BAF956A5E7F9D";

    private String secureRandomSeed = "TPCz0lDvTHybGMsHTJi3mJ7Pt48llJmRHb";
    /**
     * 模式（1-加密，2-解密）
     */
    private int mode;
}
