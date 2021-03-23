package com.yss.datamiddle.util;

import com.yss.datamiddle.config.MySecurityCipher;
import com.yss.datamiddle.config.SecurityCipher;
import com.yss.datamiddle.po.CipherEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * @description: 密码工具类（对称加密）
 * @author: fangzhao
 * @create: 2020/7/17 16:58
 * @update: 2020/7/17 16:58
 */
public final class CipherSymUtil {

    private static final Logger log = LoggerFactory.getLogger(CipherSymUtil.class);
    private static final int KEY_SIZE = 128;

    /**
     * 加密
     */
    public static String encrypt(String content) {
        CipherEntity entity = new CipherEntity();
        return encrypt(content, entity.getAlgorithm(), entity.getCipherIns(), entity.getSecretKey(), entity.getVectorKey());
    }

    public static String encrypt(String content, String algorithm) {

        SecurityCipher securityCipher = MySecurityCipher.getConnMap().get(algorithm);
        CipherEntity entity = new CipherEntity();
        BeanUtils.copyProperties(securityCipher, entity);
        return encrypt(content, algorithm, entity.getCipherIns(), entity.getSecretKey(), entity.getVectorKey());
    }


    public static String encrypt(String content, String algorithm, String cipherIns, String secretKey, String vectorKey) {

        if (StringUtils.isEmpty(content)) {
            return content;
        }
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(secretKey.getBytes(), algorithm);
            Cipher cipher = Cipher.getInstance(cipherIns);
            // 使用CBC模式，需要一个向量iv，可增加加密算法的强度
            IvParameterSpec iv = new IvParameterSpec(vectorKey.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(content.getBytes());
            // 此处使用BASE64做转码功能，同时能起到2次加密的作用。
            return new String(Base64.getEncoder().encode(encrypted));
        } catch (Exception e) {
            log.error("EncryptUtil encrypt()" + e.getMessage(), e);
        }
        return null;
    }

    /**
     * 解密
     */
    public static String decrypt(String content) {
        CipherEntity entity = new CipherEntity();
        return decrypt(content, entity.getAlgorithm(), entity.getCipherIns(), entity.getSecretKey(), entity.getVectorKey());
    }

    public static String decrypt(String content, String algorithm) {
        SecurityCipher securityCipher = MySecurityCipher.getConnMap().get(algorithm);
        CipherEntity entity = new CipherEntity();
        BeanUtils.copyProperties(securityCipher, entity);
        return decrypt(content, algorithm, entity.getCipherIns(), entity.getSecretKey(), entity.getVectorKey());
    }

    public static String decrypt(String content, String algorithm, String cipherIns, String secretKey, String vectorKey) {

        if (StringUtils.isEmpty(content)) {
            return content;
        }
        try {
            // byte[] raw = secretKey.getBytes(StandardCharsets.US_ASCII);
            SecretKeySpec skeySpec = new SecretKeySpec(secretKey.getBytes(), algorithm);
            Cipher cipher = Cipher.getInstance(cipherIns);
            IvParameterSpec iv = new IvParameterSpec(vectorKey.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            // 先用base64解密
            byte[] decodeBuffer = Base64.getDecoder().decode(content);
            byte[] original = cipher.doFinal(decodeBuffer);
            String originalString = new String(original);
            return originalString;
        } catch (Exception e) {
            log.error("EncryptUtil decrypt()" + e.getMessage(), e);
        }
        return null;
    }

    /**
     * 生成密钥（该方法暂时不用，生成密钥，需要存储以进行解密，不如直接固定一个密钥）
     * 自动生成base64 编码后的AES128位密钥
     */
    private static String getAESKey() throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        /**
         * 要生成多少位，只需要修改这里即可128, 192或256
         */
        kg.init(KEY_SIZE);
        SecretKey sk = kg.generateKey();
        byte[] b = sk.getEncoded();
        return parseByte2HexStr(b);
    }

    /**
     * 将二进制转换成16进制
     */
    private static String parseByte2HexStr(byte[] buf) {
        StringBuffer buffer = new StringBuffer();
        int bufLen = buf.length;
        for (int i = 0; i < bufLen; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (1 == hex.length()) {
                hex = '0' + hex;
            }
            buffer.append(hex.toUpperCase());
        }
        return buffer.toString();
    }

    public static void main(String[] args) {
        String content = "奖励随虫害，车书本一家。胜勋贵就过，家具在中华。丁姐分酋长，开反倒属下。就门缝越好，回收是天涯。";
        String encrypt = encrypt(content);
        log.info("源内容：\n" + content);
        log.info("加密后：\n" + encrypt);
        log.info("解密后：\n" + decrypt(encrypt));
    }
}
