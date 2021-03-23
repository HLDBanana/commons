package com.yss.datamiddle.service;

/**
 * @description: 加密解密接口
 * @author: fangzhao
 * @create: 2020/7/17 16:58
 * @update: 2020/7/17 16:58
 */
public interface CipherService {

    /**
     * 加密
     */
    String encrypt(String content);

    /**
     * 解密
     */
    String decrypt(String content);

}
