package com.sobey.tvcust.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.shelwee.update.UpdateHelper;
import com.shelwee.update.listener.OnUpdateListener;
import com.shelwee.update.pojo.UpdateInfo;
import com.sobey.common.utils.ACache;
import com.sobey.common.utils.ClearCacheUtil;
import com.sobey.tvcust.R;

public class TestActivity extends AppCompatActivity {
    UpdateHelper updateHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        updateHelper = new UpdateHelper.Builder(this)
                .checkUrl("http://xx")
                        .isAutoInstall(false) //设置为false需在下载完手动点击安装;默认值为true，下载后自动安装。
//                        .isHintNewVersion(false)
                .build();
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
            case R.id.version:
                updateHelper.check(new OnUpdateListener() {
                    @Override
                    public void onStartCheck() {
                        Log.e("liao","onStartCheck");
                    }
                    @Override
                    public void onFinishCheck(UpdateInfo info) {
                        Log.e("liao","onFinishCheck");
                    }
                    @Override
                    public void onStartDownload() {
                        Log.e("liao","onStartDownload");
                    }
                    @Override
                    public void onInstallApk() {
                        Log.e("liao","onInstallApk");
                    }
                    @Override
                    public void onFinshDownload() {
                        Log.e("liao","onFinshDownload");
                    }
                    @Override
                    public void onDownloading(int progress) {
//                        Log.e("liao","onDownloading:"+progress);
                    }
                });
                break;
        }
    }
}
