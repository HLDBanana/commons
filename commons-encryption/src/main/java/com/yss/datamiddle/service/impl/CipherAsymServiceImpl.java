package com.yss.datamiddle.service.impl;

import com.yss.datamiddle.po.CipherEntity;
import com.yss.datamiddle.service.CipherAsymService;
import com.yss.datamiddle.util.CipherAsymUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;

/**
 * @description: 加密解密实现类（非对称）
 * @author: fangzhao
 * @create: 2020/7/17 16:58
 * @update: 2020/7/17 16:58
 */
@Service("cipherRsaService")
public class CipherAsymServiceImpl implements CipherAsymService {

    @Override
    public String encrypt(String content) {
        return StringUtils.isEmpty(content) ? content : CipherAsymUtil.cipherByPublicKey(content, Cipher.ENCRYPT_MODE);
    }

    @Override
    public String decrypt(String content) {
        return StringUtils.isEmpty(content) ? content : CipherAsymUtil.cipherByPrivateKey(content, Cipher.DECRYPT_MODE);
    }

    @Override
    public String cipherByPrivateKey(String content, int model) {
        return CipherAsymUtil.cipherByPrivateKey(content, model);
    }

    @Override
    public String cipherByPublicKey(String content, int model) {
        return CipherAsymUtil.cipherByPublicKey(content, model);
    }

    @Override
    public String cipherByPrivateKey(CipherEntity entity) {
        return CipherAsymUtil.cipherByPrivateKey(entity.getContent(), entity.getMode(), entity.getSecureRandomSeed());
    }

    @Override
    public String cipherByPublicKey(CipherEntity entity) {
        return CipherAsymUtil.cipherByPublicKey(entity.getContent(), entity.getMode(), entity.getSecureRandomSeed());
    }
}
