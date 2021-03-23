package com.yss.datamiddle.service;

import com.yss.datamiddle.po.CipherEntity;

/**
 * @description: 加密解密基本接口（对称加密，如DES、AES）
 * @author: fangzhao
 * @create: 2020/7/17 16:58
 * @update: 2020/7/17 16:58
 */
public interface CipherSymService extends CipherService {

    /**
     * 指定密钥和向量进行加密操作
     */
    String encrypt(CipherEntity cipherEntity);

    /**
     * 指定密钥和向量进行解密操作
     */
    String decrypt(CipherEntity cipherEntity);

    /**
     * 内容 密码实例 密钥 向量 算法
     */
    String encryptByEntity(CipherEntity cipherEntity);

    /**
     * 内容 密码实例 密钥 向量 算法
     */
    String decryptByEntity(CipherEntity cipherEntity);
}
