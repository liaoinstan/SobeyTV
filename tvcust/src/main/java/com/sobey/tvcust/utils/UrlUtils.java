package com.sobey.tvcust.utils;

import android.util.Log;

import com.sobey.common.utils.MD5Util;
import com.sobey.common.utils.TimeUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/13 0013.
 */
public class UrlUtils {

    private static final String secretKey = "893ff8a2ab604d8bab9407bd91376ee3";
    private static final String app = "Care_app";
    private static final String type = "json";

    public static String geturl(HashMap<String, String> map, String url) {
        String para = "";
        if (map != null && map.size() > 0) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                para += key + "=" + value + "&";

                Log.e("UrlUtils", key + ":" + value);
            }
        }
        return url + "?" + getafturl(para);
    }

    private static String getafturl(String pp) {
        String timestamp = System.currentTimeMillis() + "";
        String para = pp + "app=" + app + "&timestamp=" + timestamp + "&type=" + type;
        String usign = (secretKey + para).toLowerCase();
        String sign = MD5Util.md5(usign);

        String afturl = para + "&sign=" + sign;
        return afturl;
    }

    /**
     * String url= "http://120.76.165.97/sobey/center/openservice/stats/station";
     * HashMap<String, String> map = new HashMap<>();
     * map.put("station","CCTV");
     * String newurl= UrlUtils.geturl(map, url);
     */

    public static void main(String[] arg){
//        HashMap<String, String> map = new HashMap<>();
////        Date date = new Date();
//        Date date = TimeUtil.getDateByStr("yyyy-MM-dd HH:mm:ss","2016-07-31 09:34:10");
//        map.put("time",date.getTime()+"");
//        map.put("count",12000+"");
//        map.put("station","tctc_20160527");
//        String url = geturl(map, AppData.Url.news);
//        System.out.println(url);

//        //测试数组
//        int[] a = new int[]{2,3,5,3,8};
//        //输入的数
//        int num = 3;
//        //临时存储的数组,和a的大小一样大
//        int[] temp = new int[a.length];
//        int index = 0;
//
//        for (int i=0;i<a.length;i++){
//            if (a[i]==num){
//                temp[index] = a[i];
//            }
//        }
//        //b 就是结果数组
//        Integer[] b = (Integer[]) list.toArray(new Integer[]{});
    }
}
