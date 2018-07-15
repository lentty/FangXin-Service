package com.cqfangxin.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    public static String digest(String password) {
        char hexDigits[] = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        try {
            //获取密码的字节数组
            byte[] btInput = password.getBytes();
            //获取md加密对象
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            //使用指定的字节更新摘要
            md5.update(btInput);
            //获得密文
            byte[] digest = md5.digest();
            //把密文转换成16进制的字符串形式
            int length = digest.length;
            char[] str = new char[length*2];
            int k = 0;
            for (int i = 0; i < length; i++) {
                byte b = digest[i];
                str[k++] = hexDigits[b >>> 4 & 0xf];
                str[k++] = hexDigits[b & 0xf];
            }
            return new String(str);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
