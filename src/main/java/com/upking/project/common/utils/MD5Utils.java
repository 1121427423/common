package com.upking.project.common.utils;

import com.upking.project.common.constant.Charset;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;

/**
 * @author king
 * @version 1.0
 * @className MD5
 * @description TODO
 * @date 2022/6/18
 */
public class MD5Utils {

    /**
     * 16位取中间的16位 9～25
     * @param text 需要加密的字符串
     * @return 加密后字符串
     */
    public static String encrypt16(String text) {
        return encrypt32(text, "", Charset.UTF_8).substring(8,24);
    }

    /**
     * 16位取中间的16位 9～25
     * @param text 需要加密的字符串
     * @param charset 编码格式
     * @return 加密后字符串
     */
    public static String encrypt16(String text, String charset) {
        return encrypt32(text, "", charset).substring(8,24);
    }

    /**
     * 16位取中间的16位 9～25
     * @param text 需要加密的字符串
     * @param salt 盐值
     * @param charset 编码格式
     * @return 加密后字符串
     */
    public static String encrypt16(String text, String salt, String charset) {
        return encrypt32(text, salt, charset).substring(8,24);
    }

    /**
     * 加密
     * @param text 需要加密的字符串
     * @return 加密后字符串
     */
    public static String encrypt32(String text) {
        return encrypt32(text, "", Charset.UTF_8);
    }

    /**
     * 加密
     * @param text 需要加密的字符串
     * @param charset 编码格式
     * @return 加密后字符串
     */
    public static String encrypt32(String text, String charset) {
        return encrypt32(text, "", charset);
    }

    /**
     * 加密
     * @param text 需要加密的字符串
     * @param salt 盐值
     * @param charset 编码格式
     * @return 加密后字符串
     */
    public static String encrypt32(String text, String salt, String charset) {
        text = text + salt;
        return DigestUtils.md5Hex(getContentBytes(text, charset));
    }

    /**
     *
     * @param text 需要签名的字符串
     * @param salt 盐值
     * @param charset 编码格式
     * @return 签名结果
     */
    public static String sign(String text, String salt, String charset) {
        text = text + salt;
        return DigestUtils.md5Hex(getContentBytes(text, charset));
    }
    /**
     * 签名字符串
     * @param text 需要签名的字符串
     * @param sign 签名结果
     * @param salt 盐值
     * @param charset 编码格式
     * @return 签名结果
     */
    public static boolean verify(String text, String sign, String salt, String charset) {
        text = text + salt;
        String mysign = DigestUtils.md5Hex(getContentBytes(text, charset));
        if(mysign.equals(sign)) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * 字符串转字节数组
     * @param content 字符串
     * @param charset 编码格式
     * @return 字节数组
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }
}
