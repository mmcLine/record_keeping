package com.mmc.utils;

import org.springframework.util.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * SHA加解密
 * Secure Hash Algorithm，安全散列算法
 */
public class SHA512 {

    private static final String SALT="46ec8c17f5e@!1";

    /** 传入文本内容，返回SHA-256 串*/
    public static String encry256(final String strText){
        return  SHA(strText,"SHA-256");
    }

    /** 传入文本内容，返回SHA-512 串*/
    public static String encry512(final String strText){
        return SHA(strText,"SHA-512");
    }


    /**
     * 字符串 SHA加密
     */
    private static String SHA(final String strText, final String strType) {
        if (StringUtils.hasText(strText)) {
            String encodeTest=strText+SALT;
            try {
                MessageDigest messageDigest = MessageDigest.getInstance(strType);
                // 传入加密的字符串
                messageDigest.update(encodeTest.getBytes());
                // 得到bytes类型结果
                byte[] byteBuffer = messageDigest.digest();
                //将byte转为string
                StringBuffer strHexString = new StringBuffer();
                for (int i = 0; i < byteBuffer.length; i++) {
                    String hex = Integer.toHexString(0xff & byteBuffer[i]);
                    if (hex.length() == 1) {
                        strHexString.append('0');
                    }
                    strHexString.append(hex);
                }
                // 得到返回的结果
                return strHexString.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return "";
            }


        }
        return "";
    }
}
