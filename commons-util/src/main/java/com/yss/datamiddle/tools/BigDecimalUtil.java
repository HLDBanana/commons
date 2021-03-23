package com.yss.datamiddle.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * @description: BigDecimal工具类
 * @author: fangzhao
 * @create: 2020/3/24 13:09
 * @update: 2020/3/24 13:09
 */
@Slf4j
public final class BigDecimalUtil {

    /**
     * 描述：相加
     *
     * @param val
     * @return java.math.BigDecimal
     * @author fangzhao at 2020/4/7 15:55
     */
    public static BigDecimal add(BigDecimal... val) {
        BigDecimal initBigDecimal = BigDecimal.ZERO;

        if (!Objects.isNull(val)) {
            for (BigDecimal bigDecimal : val) {
                initBigDecimal = initBigDecimal.add(bigDecimal);
            }
        }

        return initBigDecimal;
    }

    /**
     * 描述：字符串类型的数据相加
     *
     * @param val
     * @return java.math.BigDecimal
     * @author fangzhao at 2020/4/7 15:55
     */
    public static BigDecimal add(String... val) {
        BigDecimal initBigDecimal = BigDecimal.ZERO;

        if (!Objects.isNull(val)) {
            for (String s : val) {
                initBigDecimal = initBigDecimal.add(new BigDecimal(s));
            }
        }

        return initBigDecimal;
    }

    /**
     * 描述：如果为空返回0
     *
     * @param s
     * @return java.math.BigDecimal
     * @author fangzhao at 2020/4/7 15:55
     */
    public static BigDecimal zeroIfBlank(String a) {
        if (StringUtils.isEmpty(a)) {
            return BigDecimal.ZERO;
        }

        return new BigDecimal(a);
    }

    /**
     * 描述：除法
     *
     * @param v1
     * @param v2
     * @param newScale
     * @param roundingMode
     * @return java.math.BigDecimal
     * @author fangzhao at 2020/4/7 15:58
     */
    public static BigDecimal divide(String v1, String v2, int newScale, RoundingMode roundingMode) {
        try {
            if (StringUtils.isEmpty(v1) || StringUtils.isEmpty(v2)) {
                return BigDecimal.ZERO;
            }
            return new BigDecimal(v1).divide(new BigDecimal(v2), newScale, roundingMode);
        } catch (Exception e) {
            log.error("BigDecimalUtil->除法异常! v1:{}, v2:{}, newScale:{}, roundingMode:{}",
                    v1, v2, newScale, roundingMode, e);
        }

        return BigDecimal.ZERO;
    }

    /**
     * 描述：除法，保留两位小数点
     *
     * @param v1
     * @param v2
     * @return java.lang.String
     * @author fangzhao at 2020/4/7 15:58
     */
    public static String divide(String v1, String v2) {
        return divide(v1, v2, 2, RoundingMode.HALF_UP).toString();
    }

}
