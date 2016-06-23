package com.sobey.tvcust.ui.activity;

import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.shelwee.update.UpdateHelper;
import com.shelwee.update.listener.OnUpdateListener;
import com.shelwee.update.pojo.UpdateInfo;
import com.sobey.common.utils.ClearCacheUtil;
import com.sobey.common.utils.VersionUtil;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.LoadingViewUtil;
import com.sobey.tvcust.entity.TestEntity;
import com.sobey.tvcust.ui.adapter.RecycleAdapterMsg;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{

//    private View item_setting_about;
//    private View item_setting_clause;
//    private View item_setting_version;
//    private View item_setting_safe;
//    private View item_setting_clear;
//    private View item_setting_logout;
    private TextView text_setting_version;
    private TextView text_setting_clear;

    UpdateHelper updateHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initBase();
        initView();
        initData();
        initCtrl();
    }

    private void initBase() {
        updateHelper = new UpdateHelper.Builder(this)
                .checkUrl("http://xx")
                .isAutoInstall(false) //设置为false需在下载完手动点击安装;默认值为true，下载后自动安装。
//                        .isHintNewVersion(false)
                .build();
    }

    private void initView() {
        findViewById(R.id.item_setting_about).setOnClickListener(this);
        findViewById(R.id.item_setting_clause).setOnClickListener(this);
        findViewById(R.id.item_setting_version).setOnClickListener(this);
        findViewById(R.id.item_setting_safe).setOnClickListener(this);
        findViewById(R.id.item_setting_clear).setOnClickListener(this);
        findViewById(R.id.item_setting_logout).setOnClickListener(this);
        text_setting_version = (TextView) findViewById(R.id.text_setting_version);
        text_setting_clear = (TextView) findViewById(R.id.text_setting_clear);
    }

    private void initData() {
    }

    private void initCtrl() {
        String version = VersionUtil.getVersion(this);
        text_setting_version.setText(version);
        String size = "";
        try {
            size = ClearCacheUtil.getExternalCacheSize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        text_setting_clear.setText(size);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.item_setting_about:
                break;
            case R.id.item_setting_clause:
                break;
            case R.id.item_setting_version:
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
            case R.id.item_setting_safe:
                break;
            case R.id.item_setting_clear:
                ClearCacheUtil.clearExternalCache(this);
                String size = "";
                try {
                    size = ClearCacheUtil.getExternalCacheSize(this);
                    Toast.makeText(this,"清理完毕",Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                text_setting_clear.setText(size);
                break;
            case R.id.item_setting_logout:
                break;
        }
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
