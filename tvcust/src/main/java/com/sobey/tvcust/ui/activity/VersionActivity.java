package com.sobey.tvcust.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.shelwee.update.UpdateHelper;
import com.shelwee.update.listener.OnUpdateListener;
import com.shelwee.update.pojo.UpdateInfo;
import com.sobey.common.utils.VersionUtil;
import com.sobey.tvcust.R;

public class VersionActivity extends AppCompatActivity {

    UpdateHelper updateHelper;

    private TextView vname;
    private TextView vcheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initBase();
        initView();
        initData();
        initCtrl();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (updateHelper!=null) updateHelper.onDestory();
    }

    private void initBase() {
        updateHelper = new UpdateHelper.Builder(this)
                .checkUrl("http://xx")
                .isAutoInstall(false) //设置为false需在下载完手动点击安装;默认值为true，下载后自动安装。
//                        .isHintNewVersion(false)
                .build();
    }

    private void initView() {
        vname = (TextView) findViewById(R.id.version_vname);
        vcheck = (TextView) findViewById(R.id.version_check);
    }

    private void initData() {
    }

    private void initCtrl() {
        String version = VersionUtil.getVersion(this);
        vname.setText(version);
        vcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
