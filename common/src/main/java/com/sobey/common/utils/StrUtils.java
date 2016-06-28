package com.sobey.common.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by lsy on 15-6-14.
 */
public class StrUtils {
    public static boolean isEmpty(String str) {
        if (str!=null&&!"".equals(str)){
            return false;
        }else {
            return true;
        }
    }
}
