package com.upking.project.common.utils;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author king
 * @version 1.0
 * @className SignUtils
 * @description TODO
 * @date 2022/6/19
 */
public class SignUtils {

    /**
     * 将请求参数组装成 name=king&age=18这样的格式，如果high为空则high=
     * @return
     */
    public static String assembleSignContent(Map<String,Object> params) {
        if (params != null && params.size() > 0) {
            StringBuffer buffer = new StringBuffer();
            params.forEach((k,v)->  buffer.append(k).append("=").append(v).append("&"));
            String content = buffer.toString();
            if(content.endsWith("&")) {
                content = content.substring(0, content.lastIndexOf("&"));
            }
            return content.trim();
        }
        return "";
    }

    /**
     * 其他接口加签(排除终端注册接口外)
     * @param map
     * @param privateKey
     * @return
     */
    public static String sign(TreeMap<String, Object> map, String privateKey) {
        try {
            String content = assembleSignContent(map);
            content = SHA256Util.getSHA256(content);
            String certSign = RSAUtils.sign(content.getBytes(StandardCharsets.UTF_8), privateKey);
            return certSign;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 其他接口验签操作(排除终端注册接口外)
     * @param map
     * @param certSign
     * @param publicKey
     * @return
     */
    public static boolean verify(TreeMap<String,Object> map, String certSign, String publicKey) {
        try {
            String content = assembleSignContent(map);
            content = SHA256Util.getSHA256(content);
            boolean result = RSAUtils.verify(content.getBytes(StandardCharsets.UTF_8), publicKey, certSign);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
