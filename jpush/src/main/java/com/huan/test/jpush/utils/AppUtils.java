package com.huan.test.jpush.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;

/**
 * Created by Administrator on 2016/7/6 0006.
 */
public class AppUtils {
    /**
     *
     *  @Description    : 这个包名的程序是否在运行
     *  @Method_Name    : isRunningApp
     *  @param context 上下文
     *  @param packageName 判断程序的包名
     *  @return 必须加载的权限
     *      <uses-permission android:name="android.permission.GET_TASKS">
     *  @return         : boolean
     *  @Creation Date  : 2014-10-31 下午1:14:15
     *  @version        : v1.00
     *  @Author         : JiaBin

     *  @Update Date    :
     *  @Update Author  : JiaBin
     */
    public static boolean isRunningApp(Context context, String packageName) {
        boolean isAppRunning = false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(packageName) && info.baseActivity.getPackageName().equals(packageName)) {
                isAppRunning = true;
                Log.i("apputils",info.topActivity.getPackageName() + " info.baseActivity.getPackageName()="+info.baseActivity.getPackageName());
                break;
            }
        }
        return isAppRunning;
    }

    /*
	 * 启动一个app
	 */
    public static void startAPP(Context context, String appPackageName){
        try{
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(appPackageName);
            context.startActivity(intent);
        }catch(Exception e){
            Log.e("liao","没有安装"+appPackageName);
        }
    }
}
