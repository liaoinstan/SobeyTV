package com.sobey.tvcust.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.sobey.tvcust.common.AppData;

/**
 * Created by Administrator on 2016/8/10.
 */
public class MyInstalledReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {  // install
            String packageName = intent.getDataString();
            Log.e("homer", "安装了 :" + packageName);
            if ("com.sobey.tvcust".equals(packageName)){
                AppData.App.removeStartUp();
            }
        }
        if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) { // uninstall
            String packageName = intent.getDataString();
            Log.e("homer", "卸载了 :" + packageName);
            if ("com.sobey.tvcust".equals(packageName)){
                AppData.App.removeStartUp();
            }
        }
    }
}
