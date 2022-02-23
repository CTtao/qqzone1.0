package com.ct.myssm.utils;

/**
* @Description: 判断字符串是否为空
* @Param:
* @return:
* @Author: CT
* @Date: 2022/1/6 11:09
*/
public class StringUtil {
    public static boolean isEmpty(String str){
        return str==null || "".equals(str);
    }
    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }
}
