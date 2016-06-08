package com.sobey.common.utils;

import android.content.Context;

/**
 * Author: wyouflf
 * Date: 13-11-12
 */
public class ApplicationHelp {
    private ApplicationHelp() {
    }

    private static Context context;

    public static Context getApplicationContext(Context appContext) {
        if (context == null) {
        	ApplicationHelp.context = appContext;
        }
        return context;
    }
    
    public static Context getApplicationContext() {
        return context;
    }
}
