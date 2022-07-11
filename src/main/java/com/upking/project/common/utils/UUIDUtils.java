package com.upking.project.common.utils;

import com.upking.project.common.constant.DateFormat;

import java.math.BigInteger;
import java.util.UUID;

/**
 * @author king
 * @version 1.0
 * @className UUIDUtils
 * @description UUID工具类
 * @date 2022/6/18
 */
public class UUIDUtils {

    public static String[] chars = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    /**
     * 32位默认长度的uuid
     *
     * @return UUID
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 获取指定长度uuid
     */
    public static String getUUID(int len) {
        StringBuilder shortBuffer = new StringBuilder();
        String uuid = getUUID();
        for (int i = 0; i < len; i++) {
            String str = uuid.substring(i * (32 / len), i * (32 / len) + (32 / len));
            BigInteger x = new BigInteger(str, 16);
            BigInteger y = new BigInteger("3e", 16);
            shortBuffer.append(chars[x.mod(y).intValue()]);
        }
        return shortBuffer.toString();
    }

    /**
     * 获取随机UUId  20位 (10日期+10位数字)
     * @return
     */
    public static String getRandomIdByUUId() {
        String time = DateUtils.getCurrentTime(DateFormat.YYYYMMDDHH);
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if (hashCodeV < 0) {
            hashCodeV = -hashCodeV;
        }
        return time + String.format("%010d", hashCodeV);
    }

    /**
     * 获取UUID带前缀
     * @param frontString
     * @return
     */
    public static String getIdHasFront(String frontString) {
        return frontString + getRandomIdByUUId();
    }

}
