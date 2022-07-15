package com.tsingsoft.common.utils;

/**
 * Created by bask on 2017/9/10.
 * @author bask
 */
public class StringHelper {
    public static String getObjectValue(Object obj){
        return obj==null?"":obj.toString();
    }
}
