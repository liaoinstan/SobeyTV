package com.sobey.tvcust.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.sobey.common.utils.ACache;
import com.sobey.common.utils.ClearCacheUtil;
import com.sobey.tvcust.R;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.size:
                String size = "null";
                try {
                    size = ClearCacheUtil.getExternalCacheSize(this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(this,size,Toast.LENGTH_SHORT).show();
                break;
            case R.id.clear:
                ClearCacheUtil.clearExternalCache(this);
                break;
            case R.id.go:
                Intent intent = new Intent(this, LoadUpActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.acache:
                ACache.get(this).put("test","asdasdada");
                break;
        }
    }
}
