package com.sobey.tvcust.im;

import com.hyphenate.easeui.domain.EaseUser;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/3 0003.
 */
public class UserProvider {
    protected static Map<String,EaseUser> usermap = new HashMap<>();

    public static void put(String key,EaseUser user){
        usermap.put(key,user);
    }
    public static EaseUser get(String key){
        return usermap.get(key);
    }
}
