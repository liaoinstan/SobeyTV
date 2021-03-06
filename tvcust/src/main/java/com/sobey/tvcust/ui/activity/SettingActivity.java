package com.sobey.tvcust.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.shelwee.update.UpdateHelper;
import com.sobey.share.sharesdk.dialog.ShareDialog;
import com.sobey.tvcust.common.CancelableCollector;
import com.sobey.tvcust.common.CommonNet;
import com.sobey.common.utils.ClearCacheUtil;
import com.shelwee.update.utils.VersionUtil;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.AppData;
import com.sobey.tvcust.common.MyActivityCollector;
import com.sobey.tvcust.entity.CommonEntity;
import com.sobey.tvcust.ui.dialog.DialogLoading;
import com.sobey.tvcust.ui.dialog.DialogSure;
import com.sobey.tvcust.utils.AppHelper;
import com.sobey.tvcust.utils.UrlUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

public class SettingActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private TextView text_setting_version;
    private TextView text_setting_clear;

    private DialogSure dialogSureClear;
    private DialogSure dialogSureLogout;
    private DialogLoading dialogLoading;


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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialogSureClear != null) dialogSureClear.dismiss();
        if (dialogSureLogout != null) dialogSureLogout.dismiss();
        if (dialogLoading != null) dialogLoading.dismiss();
    }

    private void initBase() {
        dialogLoading = new DialogLoading(this, "正在注销");
        dialogSureClear = new DialogSure(this, "该操作会清除使用过程中录制的音频、视频等，且不可恢复，你确认继续？");
        dialogSureClear.setOnOkListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearCacheUtil.clearSobeyCache(SettingActivity.this);
                String size = "";
                try {
                    size = ClearCacheUtil.getSobeyCacheSize(SettingActivity.this);
                    Toast.makeText(SettingActivity.this, "清理完毕", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                text_setting_clear.setText(size);
                dialogSureClear.hide();
            }
        });

        dialogSureLogout = new DialogSure(this, "确认退出当前账号？");
        dialogSureLogout.setOnOkListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSureLogout.hide();
                netLogout();
            }
        });
    }

    private void initView() {
        findViewById(R.id.item_setting_about).setOnClickListener(this);
        findViewById(R.id.item_setting_share).setOnClickListener(this);
        findViewById(R.id.item_setting_clause).setOnClickListener(this);
        findViewById(R.id.item_setting_version).setOnClickListener(this);
        findViewById(R.id.item_setting_feedbank).setOnClickListener(this);
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
            size = ClearCacheUtil.getSobeyCacheSize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        text_setting_clear.setText(size);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.item_setting_about:
                intent.setClass(this, WebActivity.class);
                intent.putExtra("title", "关于我们");
                intent.putExtra("url", AppData.Url.pageAbout);//https://github.com    //http://cn.bing.com
                startActivity(intent);
                break;
            case R.id.item_setting_share:
                ShareDialog shareDialog = new ShareDialog(this);
                shareDialog.setShareData("口袋小贝", "索贝用心为你服务", AppData.Url.shareApp, AppData.Url.AppLogo);
                shareDialog.show();
                break;
            case R.id.item_setting_clause:
                intent.setClass(this, WebActivity.class);
                intent.putExtra("title", "服务条款");
                intent.putExtra("url", AppData.Url.pageClause);//https://github.com    //http://cn.bing.com
                startActivity(intent);
                break;
            case R.id.item_setting_version:
                intent.setClass(this, VersionActivity.class);
                startActivity(intent);
                break;
            case R.id.item_setting_feedbank:
                intent.setClass(this, FeedBackActivity.class);
                startActivity(intent);
                break;
            case R.id.item_setting_safe:
                intent.setClass(this, SettingSecurityActivity.class);
                startActivity(intent);
                break;
            case R.id.item_setting_clear:
                dialogSureClear.show();
                break;
            case R.id.item_setting_logout: {
                dialogSureLogout.show();
                break;
            }
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

    private void netLogout() {
        RequestParams params = new RequestParams(AppData.Url.logout);
        params.addHeader("token", AppData.App.getToken());
        CommonNet.samplepost(params, CommonEntity.class, new CommonNet.SampleNetHander() {
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                Toast.makeText(SettingActivity.this, text, Toast.LENGTH_SHORT).show();
                dialogSureClear.hide();
                //退出成功后清空所有正在请求的链接
                CancelableCollector.CancleAll();
                AppData.App.removeUser();
                AppData.App.removeToken();

                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                startActivity(intent);
                MyActivityCollector.finishAll();
            }

            @Override
            public void netSetError(int code, String text) {
                Toast.makeText(SettingActivity.this, text, Toast.LENGTH_SHORT).show();
                dialogSureClear.hide();
            }

            @Override
            public void netStart(int code) {
                dialogLoading.show();
            }

            @Override
            public void netEnd(int code) {
                dialogLoading.hide();
            }
        });
    }
}
