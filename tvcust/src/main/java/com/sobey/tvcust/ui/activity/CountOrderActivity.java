package com.sobey.tvcust.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.sobey.tvcust.R;
import com.sobey.tvcust.entity.TabEntity;

import java.util.ArrayList;

public class CountOrderActivity extends AppCompatActivity {

    private WebView webView;
    private CommonTabLayout mTabLayout_8;

    private String[] mTitles = {"8月", "9月", "10月", "11月", "12月"};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countorder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
        initData();
        initCtrl();
    }

    private void initView() {
        mTabLayout_8 = (CommonTabLayout) findViewById(R.id.tl_8);
        webView = (WebView) findViewById(R.id.web_countorder);
    }

    private void initData() {
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i]));
        }
    }

    private void initCtrl() {
        mTabLayout_8.setTabData(mTabEntities);
        mTabLayout_8.setCurrentTab(2);

        //////////////////////////////
        //打开本包内asset目录下的index.html文件
        //wView.loadUrl(" file:///android_asset/index.html ");
        //打开本地sd卡内的index.html文件
        //wView.loadUrl("content://com.android.htmlfileprovider/sdcard/index.html");
        //打开指定URL的html文件
        //wView.loadUrl(" http://m.oschina.net");

        WebSettings setting = webView.getSettings();
        setting.setJavaScriptEnabled(true);

        setting.setLoadWithOverviewMode(true);
        setting.setUseWideViewPort(true);

//        setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        webView.loadUrl("file:///android_asset/count_test.html");
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
