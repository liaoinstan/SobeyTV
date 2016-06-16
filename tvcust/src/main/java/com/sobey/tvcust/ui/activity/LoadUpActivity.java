package com.sobey.tvcust.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.sobey.common.utils.ACache;
import com.sobey.common.utils.ClearCacheUtil;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.AppData;


public class LoadUpActivity extends AppCompatActivity {

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadup);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                goNext();
            }
        }, 2000);

        loadData();
    }

    private void loadData() {
        //初始化加载资源
    }


    private void goNext(){
        final Intent intent = new Intent();
        if (AppData.App.getStartUp()) {
            intent.setClass(LoadUpActivity.this, HomeActivity.class);
        }else {
            intent.setClass(LoadUpActivity.this, StartUpActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
