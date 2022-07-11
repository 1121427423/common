package com.upking.project.common.utils;


/**
 * @author king
 * @version 1.0
 * @className StringUtils
 * @description 字符串工具类
 * @date 2022/6/18
 */
public class StringUtils {

    /**
     * 判断字符串是否为空白字符(null or length == 0 or " " or "")
     * @param str 字符串
     * @return 布尔值
     */
    public static boolean isBlank(String str) {
        int strLen = length(str);
        if (strLen != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return false;
                }
            }

        }
        return true;
    }

    /**
     * 判断字符串为非空白字符
     * @param str 字符串
     * @return 布尔值
     */
    public static boolean isNotBlank(String str) { return !isBlank(str); }

    /**
     * 判断字符串是否为空(null or length == 0)
     * @param str 字符串
     * @return 布尔值
     */
    public static boolean isEmpty(String str) { return str == null || str.length() == 0; }

    /**
     * 判断字符串非空
     * @param str 字符串
     * @return 布尔值
     */
    public static boolean isNotEmpty(String str) { return !isEmpty(str); }

    /**
     * 计算字符串长度
     * @param str 字符串
     * @return 布尔值
     */
    public static int length(String str) { return str == null ? 0 : str.length(); }

    /**
     * s1 == s2 -> 0
     * s1 < s2 -> -1 (a,b)
     * s1 > s2 -> 1 (c,a)
     * @param str1 字符串1
     * @param str2 字符串2
     * @return 如果参数字符串等于该字符串，则值为 0；
     * 如果此字符串按字典顺序小于字符串参数，则值小于 0；
     * 如果此字符串按字典顺序大于字符串参数，则值大于 0;
     */
    public static int compare(String str1, String str2) { return compare(str1, str2, true); }

    public static int compare(String str1, String str2, boolean nullIsLess) {
        if (str1 == str2) {
            return 0;
        } else if (str1 == null) {
            return nullIsLess ? -1 : 1;
        } else if (str2 == null) {
            return nullIsLess ? 1 : -1;
        } else {
            return str1.compareTo(str2);
        }
    }

    /**
     * 判断是否包含字符
     * @param str 字符串
     * @param searchChar 字符
     * @return 布尔值
     */
    public static boolean contains(String str, char searchChar) {
        if (isEmpty(str)) {
            return false;
        } else {
            return str.indexOf(searchChar) >= 0;
        }
    }

    /**
     * 判断是否包含字符串
     * @param str 字符串
     * @param searchStr 字符串
     * @return 布尔值
     */
    public static boolean contains(String str, String searchStr) {
        if (str != null && searchStr != null) {
            return str.contains(searchStr);
        } else {
            return false;
        }
    }


}
