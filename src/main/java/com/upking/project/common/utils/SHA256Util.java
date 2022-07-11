package com.upking.project.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author king
 */
public class SHA256Util {
	/**
	 * 对消息做SHA-256摘要
	 * @param source 摘要原文
	 * @return
	 */
	public static String getSHA256(String source) {
		byte[] pwd = null;
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		try {
			assert md != null;
			md.update(source.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		byte[] digest = md.digest();
		pwd = new byte[digest.length];
		System.arraycopy(digest, 0, pwd, 0, digest.length);
		return byteToHexString(pwd);
	}

    /**
     * 二进制转十六进制
     * @param b
     * @return
     */
    private static String byteToHexString(byte[] b) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            hexString.append(hex.toUpperCase());
        }
        return hexString.toString();
    }
}
