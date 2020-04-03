package com.example.ttett.util;

import java.util.regex.Pattern;

public class RegularUtil {
    /**
     * 手机格式
     * @param value
     * @return
     */
    public static boolean checkIphoneNumber(String value){
        if(value != null && value.length() == 11){
            String pattern = "^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$";
            boolean isMatch = Pattern.matches(pattern,value);
            return isMatch;
        }
        return false;
    }

    /**
     * 邮件格式
     * @param value
     * @return
     */
    public static boolean checkEmail(String value){
        if(value != null){
            String pattern = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
            boolean isMatch = Pattern.matches(pattern,value);
            return isMatch;
        }
        return false;
    }
}
