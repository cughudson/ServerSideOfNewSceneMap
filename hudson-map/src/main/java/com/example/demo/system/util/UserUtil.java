package com.example.demo.system.util;

import org.springframework.util.DigestUtils;

public class UserUtil {

    public static final int salt=961486743;
    public static String getPassword(int salt, String password) {
        return getMd5(password + salt);
    }
    public static String getPassword(  String password) {
        String md5= getMd5(password + salt);
        System.out.println(md5);
        return md5;
    }

    public static Integer getSalt() {
        return Constant.random.nextInt(100);
    }
    public static String getMd5(String str){
        return DigestUtils.md5DigestAsHex(str.getBytes());
    }
    public static String getMd5(byte[] bytes){
        return DigestUtils.md5DigestAsHex(bytes);
    }
}
