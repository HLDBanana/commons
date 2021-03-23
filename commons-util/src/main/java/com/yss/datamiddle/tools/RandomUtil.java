package com.yss.datamiddle.tools;

import java.util.Random;

/**
  * @description: 生成随机数
  * @author: fangzhao
  * @create: 2020/4/8 13:59
  * @update: 2020/4/8 13:59
  */
public final class RandomUtil {

    /**
     * 获取随机的数值。
     *
     * @param length 长度
     * @return
     */
    public static String getRandom(Integer length) {
        StringBuilder result = new StringBuilder();
        Random rand = new Random();
        int n = 20;

        boolean[] booleans = new boolean[n];
        int randInt;
        if (null != length && 0 < length) {
            n = length;
            for (int i = 0; i < length; i++) {
                do {
                    randInt = rand.nextInt(n);
                } while (booleans[randInt]);

                booleans[randInt] = true;
                result.append(String.valueOf(randInt));
            }
        }
        return result.toString();
    }
}
