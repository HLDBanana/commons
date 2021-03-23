package com.yss.datamiddle.service;

import com.yss.datamiddle.po.CipherEntity;

/**
 * @description: 加密解密基本接口（非对称加密，如RSA、SHS）
 * @author: fangzhao
 * @create: 2020/7/17 16:58
 * @update: 2020/7/17 16:58
 */
public interface CipherAsymService extends CipherService{

    /**
     * 使用私钥（PrivateKey）对数据进行加密或解密
     */
    String cipherByPrivateKey(String content, int model);

    /**
     * 使用公钥（PublicKey）对数据进行加密或解密
     */
    String cipherByPublicKey(String content, int model);

    String cipherByPrivateKey(CipherEntity entity);

    String cipherByPublicKey(CipherEntity entity);
}
