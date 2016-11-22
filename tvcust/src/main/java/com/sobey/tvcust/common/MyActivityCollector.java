package com.sobey.tvcust.common;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * 维护一个activity集合，包含app运行过程中所有的活动状态的Activity
 * @author Administrator
 *
 */
public class MyActivityCollector {  
	  
    public static List<Activity> activities = new ArrayList<Activity>();
  
    public static void addActivity(Activity activity) {
        activities.add(activity);  
    }  
  
    public static void removeActivity(Activity activity) {
        //activities.remove(activity);
    }  
  
    public static void finishAll() {
        for (Activity activity : activities) {
            if (activity != null && !activity.isFinishing()) {
                activity.finish();
            }
        }
        activities.clear();
    }  
  
}  
