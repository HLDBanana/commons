package com.yss.datamiddle.tools;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * @description: 加密工具类
 * @author: fangzhao
 * @create: 2020/3/24 13:09
 * @update: 2020/3/24 13:09
 */
public final class EncryptUtil {

    private static final String UNDERLINE = "_";
    /**
     * 描述：使用SHA和MD5加密
     *
     * @param s
     * @param key
     * @return java.lang.String
     * @author fangzhao at 2020/4/7 16:07
     */
    public static String getCode(String s, String key) {
        try {
            if (null != md5(sha1(s + UNDERLINE + key))) {
                return md5(sha1(s + UNDERLINE + key));
            } else {
                return "";
            }
        } catch (NullPointerException e) {
            return "";
        }
    }

    /**
     * 描述：MD5加密
     *
     * @param s
     * @return java.lang.String
     * @author fangzhao at 2020/4/7 16:08
     */
    public static String md5(String s) {
        try {
            byte[] btInput = s.getBytes(StandardCharsets.UTF_8);
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            StringBuilder builder = new StringBuilder();
            for (int b : md) {
                int val = b & 0xff;
                if (16 > val) {
                    builder.append("0");
                }
                builder.append(Integer.toHexString(val));
            }
            return builder.toString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 描述：使用SH1加密
     *
     * @param s
     * @return java.lang.String
     * @author fangzhao at 2020/4/7 16:08
     */
    public static String sha1(String s) {
        try {
            byte[] btInput = s.getBytes(StandardCharsets.UTF_8);
            MessageDigest mdInst = MessageDigest.getInstance("SHA1");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            StringBuilder builder = new StringBuilder();
            for (int b : md) {
                if (0 > b) {
                    b += 256;
                }
                if (16 > b) {
                    builder.append("0");
                }
                builder.append(Integer.toHexString(b));
            }
            return builder.toString();
        } catch (Exception e) {
            return null;
        }
    }

    /*public static void main(String[] args) {
        String key = getCode("oxcQvs3Uzw7B5L6pY1e2rhGMb4iE", "123");
        System.out.println(key);
        System.out.println("747846fb07");
    }*/
}