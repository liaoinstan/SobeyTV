package com.sobey.tvcust.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sobey.tvcust.common.MyActivityCollector;

/**
 * Created by Administrator on 2016/7/1 0001.
 */
public class BaseAppCompatActicity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyActivityCollector.removeActivity(this);
    }
}
