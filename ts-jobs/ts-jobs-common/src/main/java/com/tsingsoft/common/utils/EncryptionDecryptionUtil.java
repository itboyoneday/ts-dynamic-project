package com.tsingsoft.common.utils;

import java.security.MessageDigest;

/**
 * 增加多种加密功能Util
 * @author bask
 */
public class EncryptionDecryptionUtil {

    private enum DigestType{
        MD5("MD5")
        ,SHA("SHA")
        ,SHA256("SHA-256")
        ,SHA512("SHA-512");

        private String digestDesc;

        private DigestType(String digestDesc){
            this.digestDesc = digestDesc;
        }

        public String getDigestDesc() {
            return digestDesc;
        }
    }

    private final static String digest(String sourceStr,DigestType type) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
        try {
            byte[] btInput = sourceStr.getBytes();
            // 获得MD5摘要算法的MessageDigest对象
            MessageDigest mdInst = MessageDigest.getInstance(type.digestDesc);
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 将密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public final static String MD5(String s) {
        return digest(s, DigestType.MD5);
    }

    public final static String SHA(String s) {
        return digest(s, DigestType.SHA);
    }

    public final static String SHA256(String s){
        return digest(s, DigestType.SHA256);
    }

    public final static String SHA512(String s){
        return digest(s, DigestType.SHA512);
    }

    // 单测
    public static void main(String[] args) {
        String sourceStr1 = "TSINGSOFT_2021@$88-Z!";
        String sourceStr2 = "zjitc";
/*
        System.out.println(EncryptionDecryptionUtil.MD5(sourceStr1));
        System.out.println(EncryptionDecryptionUtil.MD5(sourceStr2));

        System.out.println(EncryptionDecryptionUtil.SHA(sourceStr1));
        System.out.println(EncryptionDecryptionUtil.SHA(sourceStr2));

        System.out.println(EncryptionDecryptionUtil.SHA256(sourceStr1));
        System.out.println(EncryptionDecryptionUtil.SHA256(sourceStr2));*/


    }
}
