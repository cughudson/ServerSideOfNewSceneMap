package com.example.demo.system.util;

import com.github.pagehelper.util.StringUtil;

public class StringUtils {

    public static boolean isNotEmpty(String str) {
        return StringUtil.isNotEmpty(str);
    }
    public static boolean isEmpty(String str) {
        return StringUtil.isEmpty(str);
    }

    public static boolean equals(String str, String str1) {
        if(str==null)
            return str1==null?true:false;
         return str.equals(str1);

    }
    public static boolean equalsIgnoreCase(String str, String str1) {
        if(str==null)
            return str1==null?true:false;
        return str.equals(str1);

    }

}
