package com.yss.datamiddle.service.impl;

import com.yss.datamiddle.po.CipherEntity;
import com.yss.datamiddle.service.CipherSymService;
import com.yss.datamiddle.util.CipherSymUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @description: 加密解密实现类（对称加密，默认）
 * @author: fangzhao
 * @create: 2020/7/17 16:58
 * @update: 2020/7/17 16:58
 */
@Service("cipherSymService")
public class CipherSymServiceImpl implements CipherSymService {

    @Override
    public String encrypt(String content) {
        return StringUtils.isEmpty(content) ? content : CipherSymUtil.encrypt(content);
    }

    @Override
    public String decrypt(String content) {
        return StringUtils.isEmpty(content) ? content : CipherSymUtil.decrypt(content);
    }

    @Override
    public String encrypt(CipherEntity cipherEntity) {
        return CipherSymUtil.encrypt(cipherEntity.getContent(), cipherEntity.getAlgorithm());
    }

    @Override
    public String decrypt(CipherEntity cipherEntity) {
        return CipherSymUtil.decrypt(cipherEntity.getContent(), cipherEntity.getAlgorithm());
    }

    @Override
    public String encryptByEntity(CipherEntity cipherEntity) {
        return CipherSymUtil.encrypt(cipherEntity.getContent(), cipherEntity.getSecretKey(), cipherEntity.getVectorKey(), cipherEntity.getCipherIns(), cipherEntity.getAlgorithm());
    }

    @Override
    public String decryptByEntity(CipherEntity cipherEntity) {
        return CipherSymUtil.decrypt(cipherEntity.getContent(), cipherEntity.getSecretKey(), cipherEntity.getVectorKey(), cipherEntity.getCipherIns(), cipherEntity.getAlgorithm());
    }
}
