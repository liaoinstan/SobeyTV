package com.sobey.tvcust.utils;

import android.util.Log;

import com.sobey.common.utils.MD5Util;

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
            }
        }
        return url + "?" + getafturl(para);
    }

    private static String getafturl(String pp) {
        String timestamp = System.currentTimeMillis() + "";
        String para = pp + "app=" + app + "&timestamp=" + timestamp + "&type=" + type;
        String usign = (secretKey + para).toLowerCase();
        Log.e("before md5->", usign);
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
}
