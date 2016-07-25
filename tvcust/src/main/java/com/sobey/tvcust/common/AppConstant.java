package com.sobey.tvcust.common;

import android.util.Log;

/**
 * Created by Administrator on 2016/7/1 0001.
 */
public class AppConstant {

    public static final Integer FLAG_UPDATE_ME = 0xfe01;
    public static final Integer EVENT_UPDATE_ORDERLIST = 0xfe02;
    public static final Integer EVENT_UPDATE_ORDERDESCRIBE = 0xfe03;


    public static final String FLAG_UPDATE_ME_SIGN = "FLAG_UPDATE_ME_SIGN";

    private static final String FLAGMODE = "SOBEY&INS";
    public static String makeFlagStr(String flag, String str) {
        return flag + FLAGMODE + str;
    }
    public static String getFlag(String strSpc){
        int i = strSpc.indexOf(FLAGMODE);
        return strSpc.substring(0,i);
    }
    public static String getStr(String strSpc){
        int i = strSpc.indexOf(FLAGMODE);
        return strSpc.substring(i+FLAGMODE.length());
    }
}
